package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.ConnectedUsers;
import it.theboys.project0002api.singletons.Sessions;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class LogoutHandler extends BaseHandler {
    public final static String OP = Consts.Operation.LOG_OUT.toString();
    private final ConnectedUsers users;

    public LogoutHandler(@Annotations.ConnectedUsers ConnectedUsers users) {
        this.users = users;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        user.noLongerValid();
        users.removeUser(user, Consts.DisconnectReason.MANUAL);
        Sessions.get().invalidate(user.getSessionId());
        return JsonWrapper.EMPTY;
    }
}