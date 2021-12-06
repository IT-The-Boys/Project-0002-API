package it.theboys.project0002api.server;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.web.servlet.function.ServerRequest.Headers;

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