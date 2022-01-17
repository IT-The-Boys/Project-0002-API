package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class LikeGameHandler extends GameHandler {
    public static final String OP = Consts.Operation.LIKE.toString();

    public LikeGameHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Game game, Parameters params, HttpServerExchange exchange) {
        game.toggleLikeGame(user);
        return game.getLikesInfoJson(user);
    }
}