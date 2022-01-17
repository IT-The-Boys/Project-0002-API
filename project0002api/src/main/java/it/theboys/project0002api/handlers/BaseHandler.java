package it.theboys.project0002api.handlers;

import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.BaseJsonHandler;
import it.theboys.project0002api.server.Parameters;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public abstract class BaseHandler {
    @NotNull
    public abstract JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseJsonHandler.StatusException;
}