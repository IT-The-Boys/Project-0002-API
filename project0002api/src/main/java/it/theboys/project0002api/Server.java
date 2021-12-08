package it.theboys.project0002api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.jetbrains.annotations.NotNull;

import ch.qos.logback.classic.BasicConfigurator;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import it.theboys.project0002api.cardcast.CardcastService;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.paths.AjaxPath;
import it.theboys.project0002api.paths.EventsPath;
import it.theboys.project0002api.paths.WebManifestPath;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Annotations.SocialLogin;
import it.theboys.project0002api.server.Annotations.UsersWithAccount;
import it.theboys.project0002api.server.CustomResourceHandler;
import it.theboys.project0002api.server.HttpsRedirect;
import it.theboys.project0002api.server.Provider;
import it.theboys.project0002api.singletons.BanList;
import it.theboys.project0002api.singletons.ConnectedUsers;
import it.theboys.project0002api.singletons.Emails;
import it.theboys.project0002api.singletons.GamesManager;
import it.theboys.project0002api.singletons.LoadedCards;
import it.theboys.project0002api.singletons.Preferences;
import it.theboys.project0002api.singletons.PreparingShutdown;
import it.theboys.project0002api.singletons.Providers;
import it.theboys.project0002api.singletons.ServerDatabase;
import it.theboys.project0002api.task.BroadcastGameListUpdateTask;
import it.theboys.project0002api.task.UserPingTask;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class);
    private static final long PING_START_DELAY = TimeUnit.SECONDS.toMillis(60);
    private static final long PING_CHECK_DELAY = TimeUnit.SECONDS.toMillis(5);
    private static final long BROADCAST_UPDATE_START_DELAY = TimeUnit.SECONDS.toMillis(60);
    private static final long BROADCAST_UPDATE_DELAY = TimeUnit.SECONDS.toMillis(60);

    public static void main(String[] args) throws IOException, SQLException, UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.INFO);


        Preferences preferences = Preferences.load(args);
        ServerDatabase serverDatabase = new ServerDatabase(preferences);

        Providers.add(Annotations.Preferences.class, (Provider<Preferences>) () -> preferences);

        LoadedCards loadedCards = new LoadedCards(preferences);
        Providers.add(Annotations.LoadedCards.class, (Provider<LoadedCards>) () -> loadedCards);

        UsersWithAccount accounts = new UsersWithAccount(serverDatabase);
        Providers.add(Annotations.UsersWithAccount.class, (Provider<UsersWithAccount>) () -> accounts);

        Emails emails = new Emails(serverDatabase, preferences, accounts);
        Providers.add(Annotations.Emails.class, (Provider<Emails>) () -> emails);

        BanList banList = new BanList(serverDatabase);
        Providers.add(Annotations.BanList.class, (Provider<BanList>) () -> banList);

        ConnectedUsers connectedUsers = new ConnectedUsers(banList, preferences);
        Providers.add(Annotations.ConnectedUsers.class, (Provider<ConnectedUsers>) () -> connectedUsers);

        ScheduledThreadPoolExecutor globalTimer = new ScheduledThreadPoolExecutor(2 * Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
            private final AtomicInteger threadCount = new AtomicInteger();

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("timer-task-" + threadCount.incrementAndGet());
                return t;
            }
        });

        BroadcastGameListUpdateTask updateGameListTask = new BroadcastGameListUpdateTask(connectedUsers);
        globalTimer.scheduleAtFixedRate(updateGameListTask, BROADCAST_UPDATE_START_DELAY, BROADCAST_UPDATE_DELAY, TimeUnit.MILLISECONDS);

        UserPingTask userPingTask = new UserPingTask(connectedUsers, globalTimer);
        globalTimer.scheduleAtFixedRate(userPingTask, PING_START_DELAY, PING_CHECK_DELAY, TimeUnit.MILLISECONDS);

        CardcastService cardcastService = new CardcastService();
        Providers.add(Annotations.CardcastService.class, (Provider<CardcastService>) () -> cardcastService);

        GamesManager gamesManager = new GamesManager((manager, options) -> new Game(GamesManager.generateGameId(), options, connectedUsers, manager, loadedCards, globalTimer, preferences, cardcastService), preferences, updateGameListTask);
        Providers.add(Annotations.GameManager.class, (Provider<GamesManager>) () -> gamesManager);

        EncodingHandler resourceHandler = new EncodingHandler(new ContentEncodingRepository()
                .addEncodingHandler("gzip", new GzipEncodingProvider(), 1))
                .setNext(new CustomResourceHandler(preferences));

        PathHandler pathHandler = new PathHandler(resourceHandler);
        pathHandler.addExactPath("/AjaxServlet", new AjaxPath())
                .addExactPath("/Events", Handlers.websocket(new EventsPath()));

        RoutingHandler router = new RoutingHandler();
        router.setFallbackHandler(pathHandler)
                .get("/game/{gid}/", exchange -> {
                    exchange.setRelativePath("/game.html");
                    resourceHandler.handleRequest(exchange);
                });

        Undertow.Builder builder = Undertow.builder()
                .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                .setHandler(router);

        int port = preferences.getInt("port", 80);
        String host = preferences.getString("host", "0.0.0.0");

        if (preferences.getBoolean("secure", false)) {
            builder.addHttpListener(port, host, new HttpsRedirect())
                    .addHttpsListener(preferences.getInt("securePort", 443), host, getSSLContext(
                            new File(preferences.getString("keyStorePath", "")), preferences.getString("keyStorePassword", ""),
                            new File(preferences.getString("trustStorePath", "")), preferences.getString("trustStorePassword", "")));
        } else {
            builder.addHttpListener(port, host);
        }

        Undertow server = builder.build();
        PreparingShutdown.setup(server, globalTimer, connectedUsers, socialLogin, loadedCards, serverDatabase);
        server.start();
        logger.info("Server started!");
    }

    private static SSLContext getSSLContext(File keyStorePath, String keyStorePassword, File trustStorePath, String trustStorePassword) throws IOException, CertificateException, NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(clientStore, keyStorePassword.toCharArray());
        KeyManager[] kms = kmf.getKeyManagers();

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trustStorePath), trustStorePassword.toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        TrustManager[] tms = tmf.getTrustManagers();

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kms, tms, new SecureRandom());

        return sslContext;
    }
}
