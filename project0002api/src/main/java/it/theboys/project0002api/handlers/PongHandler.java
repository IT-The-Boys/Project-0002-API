package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.Parameters;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class PongHandler extends BaseHandler {
    public static final String OP = Consts.Operation.PONG.toString();

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        user.userReceivedEvents();
        return JsonWrapper.EMPTY;
    }

    @Override
    public @NotNull JsonWrapper handle(User user, java.security.Policy.Parameters params, HttpServerExchange exchange)
            throws StatusException {
        // TODO Auto-generated method stub
        return null;
    }
}