package it.theboys.project0002api.paths;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.lang.System.Logger;
import java.net.URLDecoder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.http.cookie.Cookie;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import it.theboys.project0002api.singletons.Preferences;

public class WebManifestPath implements HttpHandler {
    private static final Logger logger = Logger.getLogger(WebManifestPath.class);
    private final JsonObject baseManifest;
    private final String baseManifestString;

    public WebManifestPath(Preferences preferences) throws FileNotFoundException {
        File manifest = new File(preferences.getString("webContent", "./WebContent"), "manifest.json");
        if (!manifest.exists() && !manifest.canRead()) {
            logger.error("Missing base manifest file. Loading empty.");
            baseManifest = new JsonObject();
        } else {
            baseManifest = new JsonParser().parse(new InputStreamReader(new FileInputStream(manifest))).getAsJsonObject();
        }

        baseManifestString = baseManifest.toString();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        exchange.startBlocking();
        if (exchange.isInIoThread()) {
            exchange.dispatch(this);
            return;
        }

        exchange.getResponseHeaders().add(Headers.CONTENT_TYPE, "application/json");

        Cookie primaryColor = exchange.getRequestCookies().get("PYX-Theme-Primary");
        if (primaryColor == null) {
            exchange.getResponseSender().send(baseManifestString);
        } else {
            JsonObject manifest = baseManifest.deepCopy();
            manifest.addProperty("theme_color", URLDecoder.decode(primaryColor.getValue(), "UTF-8"));
            exchange.getResponseSender().send(manifest.toString());
        }
    }
}