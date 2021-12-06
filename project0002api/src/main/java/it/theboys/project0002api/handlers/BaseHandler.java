package it.theboys.project0002api.handlers;

import java.security.Policy.Parameters;

import javax.validation.constraints.NotNull;

import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

public abstract class BaseHandler {
    @NotNull
    public abstract JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseJsonHandler.StatusException;
}