package it.theboys.project0002api.handlers;

import java.util.Map;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.game.SuggestedGameOptions;

public class GetSuggestedGameOptionsHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.GET_SUGGESTED_GAME_OPTIONS.toString();

    public GetSuggestedGameOptionsHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (user != game.getHost()) throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_GAME_HOST);

        JsonWrapper obj = new JsonWrapper();
        JsonArray array = new JsonArray();
        for (Map.Entry<String, SuggestedGameOptions> entry : game.getSuggestedGameOptions().entrySet())
            array.add(entry.getValue().toJson(entry.getKey(), true).obj());

        obj.add(Consts.GameSuggestedOptionsData.OPTIONS, array);
        return obj;
    }
}