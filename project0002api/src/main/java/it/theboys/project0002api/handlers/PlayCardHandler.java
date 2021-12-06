package it.theboys.project0002api.handlers;

import javax.security.auth.login.Configuration.Parameters;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.util.Annotations;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;

public class PlayCardHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.PLAY_CARD.toString();

    public PlayCardHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        String cardIdStr = params.getStringNotNull(Consts.GeneralKeys.CARD_ID);
        if (cardIdStr.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

        int cardId;
        try {
            cardId = Integer.parseInt(cardIdStr);
        } catch (NumberFormatException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.INVALID_CARD, ex);
        }

        String text = params.getString(Consts.GeneralKeys.WRITE_IN_TEXT);
        if (text != null && text.contains("<")) text = StringEscapeUtils.escapeXml11(text);

        return game.playCard(user, cardId, text);
    }
}