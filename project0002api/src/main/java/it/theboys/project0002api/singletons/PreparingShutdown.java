package it.theboys.project0002api.singletons;

import java.io.IOException;
import java.lang.System.Logger;
import java.sql.SQLException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import org.springframework.boot.autoconfigure.web.ServerProperties.Undertow;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.EventWrapper;
import it.theboys.project0002api.data.QueuedMessage;
import it.theboys.project0002api.server.Annotations.SocialLogin;
import it.theboys.project0002api.server.BaseCahHandler;

public final class PreparingShutdown {
    private static final Logger logger = Logger.getLogger(PreparingShutdown.class);
    private static PreparingShutdown instance;
    private final Undertow server;
    private final ScheduledThreadPoolExecutor globalTimer;
    private final ConnectedUsers users;
    private final SocialLogin socials;
    private final LoadedCards loadedCards;
    private final ServerDatabase serverDatabase;
    private final Object waitForEventsToBeSent = new Object();
    private boolean value = false;
    private long shutdownTime = -1;

    private PreparingShutdown(Undertow server, ScheduledThreadPoolExecutor globalTimer, ConnectedUsers users, SocialLogin socials, LoadedCards loadedCards, ServerDatabase serverDatabase) {
        this.server = server;
        this.globalTimer = globalTimer;
        this.users = users;
        this.socials = socials;
        this.loadedCards = loadedCards;
        this.serverDatabase = serverDatabase;
    }

    @NotNull
    public static PreparingShutdown get() {
        if (instance == null) throw new IllegalStateException();
        return instance;
    }

    public static void setup(Undertow server, ScheduledThreadPoolExecutor globalTimer, ConnectedUsers users, SocialLogin socials, LoadedCards loadedCards, ServerDatabase serverDatabase) {
        instance = new PreparingShutdown(server, globalTimer, users, socials, loadedCards, serverDatabase);
    }

    public synchronized void check() throws BaseCahHandler.CahException {
        if (value) throw new BaseCahHandler.CahException(Consts.ErrorCode.PREPARING_SHUTDOWN);
    }

    public synchronized boolean is() {
        return value;
    }

    public synchronized void set(boolean set) {
        if (!value) {
            value = set;
            shutdownTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10);

            globalTimer.schedule(this::kickAndShutdown, shutdownTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            users.broadcastToAll(QueuedMessage.MessageType.SERVER, getEvent());

            logger.info("Shutdown will happen at " + shutdownTime);
        }
    }

    public EventWrapper getEvent() {
        EventWrapper ev = new EventWrapper(Consts.Event.PREPARING_SHUTDOWN);
        ev.add(Consts.GeneralKeys.BEFORE_SHUTDOWN, shutdownTime - System.currentTimeMillis());
        return ev;
    }

    public void allEventsSent() {
        synchronized (waitForEventsToBeSent) {
            waitForEventsToBeSent.notifyAll();
        }
    }

    private void kickAndShutdown() {
        users.preShutdown();
        shutdownNow();
    }

    /**
     * Shutdown the server if it can be done safely
     */
    public void tryShutdown() {
        if (value && users.canShutdown()) {
            shutdownNow();
        }
    }

    /**
     * Shutdown the server independently of its status
     */
    private void shutdownNow() {
        logger.info("Waiting for events to be sent...");

        synchronized (waitForEventsToBeSent) {
            try {
                waitForEventsToBeSent.wait();
            } catch (InterruptedException ignored) {
            }
        }

        logger.info("Shutting down the server!");

        try {
            server.stop();
            socials.close();
            loadedCards.close();
            serverDatabase.close();
            System.exit(0);
        } catch (SQLException | IOException ex) {
            logger.error("Shutdown wasn't clear.", ex);
            System.exit(1);
        }
    }
}