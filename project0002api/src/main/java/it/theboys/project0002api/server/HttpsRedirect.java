package it.theboys.project0002api.server;

import org.apache.http.client.utils.URIBuilder;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import io.undertow.util.StatusCodes;

public class HttpsRedirect implements HttpHandler {
    @Override
    public void handleRequest(HttpServerExchange exchange) throws Exception {
        URIBuilder builder = new URIBuilder(exchange.getRequestURL());
        builder.setScheme("https");

        exchange.setStatusCode(StatusCodes.MOVED_PERMANENTLY);
        exchange.getResponseHeaders().add(Headers.LOCATION, builder.toString());
        exchange.endExchange();
    }
}