package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public abstract class GameHandler extends BaseHandler {
    protected final GamesManager gamesManager;

    GameHandler(GamesManager gamesManager) {
        this.gamesManager = gamesManager;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        String gameIdStr = params.getStringNotNull(Consts.GeneralKeys.GAME_ID);
        if (gameIdStr.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

        int gameId;
        try {
            gameId = Integer.parseInt(gameIdStr);
        } catch (NumberFormatException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.INVALID_GAME, ex);
        }

        final Game game = gamesManager.getGame(gameId);
        if (game == null) throw new BaseCahHandler.CahException(Consts.ErrorCode.INVALID_GAME);

        return handle(user, game, params, exchange);
    }

    @NotNull
    public abstract JsonWrapper handle(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException;
}