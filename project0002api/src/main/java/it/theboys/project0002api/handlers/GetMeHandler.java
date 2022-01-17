package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.server.Parameters;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

/**
 * Class to get user's nick for the game.html page - not safe to store/retrieve as a cookie.
 * This class returns a JSON string containing the user's nick to the client through AJAX.
 * More data will be added/used once user accounts are added.
 **/
public class GetMeHandler extends BaseHandler {
    public static final String OP = Consts.Operation.ME.toString();

    public GetMeHandler() {
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        JsonWrapper obj = new JsonWrapper();
        obj.add(Consts.UserData.NICKNAME, user.getNickname());
        if (user.getAccount() != null) obj.add(Consts.GeneralKeys.ACCOUNT, user.getAccount().toJson());

        if (user.getGame() != null) obj.add(Consts.GeneralKeys.GAME_ID, user.getGame().getId());
        else obj.add(Consts.GeneralKeys.GAME_ID, -1);

        return obj;
    }
}