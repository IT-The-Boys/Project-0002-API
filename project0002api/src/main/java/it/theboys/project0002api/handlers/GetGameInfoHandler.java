package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public class GetGameInfoHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.GET_GAME_INFO.toString();

    public GetGameInfoHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) {
        JsonWrapper obj = new JsonWrapper();
        obj.add(Consts.GameInfoData.INFO, game.getInfoJson(user, true));
        obj.add(Consts.GamePlayerInfo.INFO, game.getAllPlayersInfoJson());
        return obj;
    }
}