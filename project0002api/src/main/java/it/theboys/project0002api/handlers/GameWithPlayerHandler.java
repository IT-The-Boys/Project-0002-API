package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public abstract class GameWithPlayerHandler extends GameHandler {

    public GameWithPlayerHandler(GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public final JsonWrapper handle(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (user.getGame() != game) throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_IN_THAT_GAME);
        else return handleWithUserInGame(user, game, params, exchange);
    }

    @NotNull
    public abstract JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException;
}