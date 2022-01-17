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

public class BanHandler extends BaseHandler {
    public static final String OP = Consts.Operation.BAN.toString();
    private final ConnectedUsers connectedUsers;

    public BanHandler(@Annotations.ConnectedUsers ConnectedUsers connectedUsers) {
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
        connectedUsers.banUser(target);

        return JsonWrapper.EMPTY;
    }
}