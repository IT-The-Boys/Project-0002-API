package it.theboys.project0002api.server;

import java.lang.System.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.data.mongodb.core.mapping.Unwrapped.Nullable;
import org.springframework.web.servlet.function.ServerRequest.Headers;

import it.theboys.project0002api.Utils;
import it.theboys.project0002api.server.Annotations.Preferences;

public class CustomResourceHandler extends ResourceHandler {
    private final boolean cacheEnabled;

    public CustomResourceHandler(Preferences preferences) {
        super(buildResourceManager(preferences));

        cacheEnabled = preferences.getBoolean("cacheEnabled", true);
        setCachable(value -> cacheEnabled);
    }

    private static ResourceManager buildResourceManager(Preferences preferences) {
        Path root = Paths.get(preferences.getString("webContent", "./WebContent")).toAbsolutePath();
        boolean cacheEnabled = preferences.getBoolean("cacheEnabled", true);

        return PathResourceManager.builder()
                .setBase(root)
                .setAllowResourceChangeListeners(false)
                .setETagFunction(new ETagSupplier(cacheEnabled, Utils.generateAlphanumericString(5)))
                .build();
    }

    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        super.handleRequest(exchange);

        HeaderMap headers = exchange.getResponseHeaders();
        if (cacheEnabled) headers.add(Headers.CACHE_CONTROL, "private, no-cache");
        else headers.add(Headers.CACHE_CONTROL, "private, no-store, no-cache");
    }

    private static class ETagSupplier implements PathResourceManager.ETagFunction {
        private static final Logger logger = Logger.getLogger(ETagSupplier.class);
        private final boolean cacheEnabled;
        private final String serverTag;

        ETagSupplier(boolean cacheEnabled, String serverTag) {
            this.cacheEnabled = cacheEnabled;
            this.serverTag = serverTag;

            logger.info("ETag supplier: " + serverTag);
        }

        @Override
        @Nullable
        public ETag generate(Path path) {
            if (!cacheEnabled) return null;
            return new ETag(false, serverTag);
        }
    }
}