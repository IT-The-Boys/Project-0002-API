package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import it.theboys.project0002api.singletons.PreparingShutdown;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class SpectateGameHandler extends GameHandler {
    public static final String OP = Consts.Operation.SPECTATE_GAME.toString();

    public SpectateGameHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        PreparingShutdown.get().check();

        if (!game.isPasswordCorrect(params.getString(Consts.GameOptionsData.PASSWORD)))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.WRONG_PASSWORD);

        game.addSpectator(user);
        return JsonWrapper.EMPTY;
    }
}