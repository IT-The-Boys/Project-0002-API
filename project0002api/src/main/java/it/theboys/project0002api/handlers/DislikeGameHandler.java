package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;
import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public class DislikeGameHandler extends GameHandler {
    public static final String OP = Consts.Operation.DISLIKE.toString();

    public DislikeGameHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Game game, Parameters params, HttpServerExchange exchange) {
        game.toggleDislikeGame(user);
        return game.getLikesInfoJson(user);
    }
}