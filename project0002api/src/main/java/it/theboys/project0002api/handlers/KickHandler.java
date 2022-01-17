package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.ConnectedUsers;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class KickHandler extends BaseHandler {
    public static final String OP = Consts.Operation.KICK.toString();
    private final ConnectedUsers connectedUsers;

    public KickHandler(@Annotations.ConnectedUsers ConnectedUsers connectedUsers) {
        this.connectedUsers = connectedUsers;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (!user.isAdmin()) throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_ADMIN);

        String nickname = params.getStringNotNull(Consts.UserData.NICKNAME);
        if (nickname.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

        User target = connectedUsers.getUser(nickname);
        if (target == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.NO_SUCH_USER);
        connectedUsers.kickUser(target);

        return JsonWrapper.EMPTY;
    }
}