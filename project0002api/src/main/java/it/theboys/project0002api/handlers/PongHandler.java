package it.theboys.project0002api.handlers;


import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

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