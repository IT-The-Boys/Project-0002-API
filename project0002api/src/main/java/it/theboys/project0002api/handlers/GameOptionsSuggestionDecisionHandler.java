package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public class GameOptionsSuggestionDecisionHandler extends GameWithPlayerHandler {
    public final static String OP = Consts.Operation.GAME_OPTIONS_SUGGESTION_DECISION.toString();

    public GameOptionsSuggestionDecisionHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (game.getHost() != user) throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_GAME_HOST);

        String suggestedId = params.getStringNotNull(Consts.GameSuggestedOptionsData.ID);
        if (suggestedId.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

        if (!params.has(Consts.GameSuggestedOptionsData.DECISION))
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

        if (params.getBoolean(Consts.GameSuggestedOptionsData.DECISION, false))
            game.applySuggestedOptions(suggestedId);
        else
            game.declineSuggestedOptions(suggestedId);

        return JsonWrapper.EMPTY;
    }
}