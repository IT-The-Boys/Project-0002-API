package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public class JoinGameHandler extends GameHandler {
    public static final String OP = Consts.Operation.JOIN_GAME.toString();

    public JoinGameHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        PreparingShutdown.get().check();

        if (!game.isPasswordCorrect(params.getString(Consts.GameOptionsData.PASSWORD)))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.WRONG_PASSWORD);

        game.addPlayer(user);
        return JsonWrapper.EMPTY;
    }
}