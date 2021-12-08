package it.theboys.project0002api.paths;

import com.google.gson.JsonElement;

import org.jetbrains.annotations.Nullable;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.Handlers;

public class AjaxPath extends BaseCahHandler {

    @Override
    protected JsonElement handleRequest(@Nullable String op, @Nullable User user, Parameters params, HttpServerExchange exchange) throws StatusException {
        if (user != null) user.userDidSomething();
        if (op == null || op.isEmpty()) throw new CahException(Consts.ErrorCode.OP_NOT_SPECIFIED);
        return Handlers.obtain(op).handle(user, params, exchange).obj();
    }
}