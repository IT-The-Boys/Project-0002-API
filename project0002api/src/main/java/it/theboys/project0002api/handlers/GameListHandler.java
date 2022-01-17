package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import com.google.gson.JsonArray;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

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