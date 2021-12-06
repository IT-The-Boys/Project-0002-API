package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public class GameListHandler extends BaseHandler {
    public static final String OP = Consts.Operation.GAME_LIST.toString();
    private final GamesManager gamesManager;

    public GameListHandler(@Annotations.GameManager GamesManager gamesManager) {
        this.gamesManager = gamesManager;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        JsonWrapper json = new JsonWrapper();

        JsonArray infoArray = new JsonArray();
        for (Game game : gamesManager.getGameList()) {
            JsonWrapper info = game.getInfoJson(user, false);
            if (info != null) infoArray.add(info.obj());
        }

        json.add(Consts.GeneralKeys.GAMES, infoArray);
        json.add(Consts.GeneralKeys.MAX_GAMES, gamesManager.getMaxGames());
        return json;
    }
}