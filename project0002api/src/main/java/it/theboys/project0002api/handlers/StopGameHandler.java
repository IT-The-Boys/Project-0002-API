package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import io.undertow.server.HttpServerExchange;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class StopGameHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.STOP_GAME.toString();
    protected final Logger logger = Logger.getLogger(GameWithPlayerHandler.class);

    public StopGameHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (game.getHost() != user) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_GAME_HOST);
        } else if (game.getState() == Consts.GameState.LOBBY) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.ALREADY_STOPPED);
        } else {
            logger.info(String.format("Game %d stopped by host %s. Players: %s", game.getId(), user, game.getPlayers()));
            game.resetState(false);
            return JsonWrapper.EMPTY;
        }
    }
}