package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.cardcast.CardcastDeck;
import it.theboys.project0002api.cardcast.CardcastService;
import it.theboys.project0002api.cardcast.FailedLoadingSomeCardcastDecks;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import com.google.gson.JsonArray;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class CardcastListCardsetsHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.CARDCAST_LIST_CARDSETS.toString();
    private final CardcastService cardcastService;

    public CardcastListCardsetsHandler(@Annotations.GameManager GamesManager gamesManager, @Annotations.CardcastService CardcastService cardcastService) {
        super(gamesManager);
        this.cardcastService = cardcastService;
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        JsonArray array = new JsonArray();

        FailedLoadingSomeCardcastDecks cardcastException = null;
        for (String deckId : game.getCardcastDeckCodes().toArray(new String[0])) {
            CardcastDeck deck = cardcastService.loadSet(deckId);
            if (deck == null) {
                if (cardcastException == null) cardcastException = new FailedLoadingSomeCardcastDecks();
                cardcastException.failedDecks.add(deckId);
            }

            if (deck != null) array.add(deck.getClientMetadataJson().obj());
        }

        if (cardcastException != null) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.CARDCAST_CANNOT_FIND,
                    new JsonWrapper(Consts.GeneralKeys.CARDCAST_ID, cardcastException.getFailedJson()));
        } else {
            return new JsonWrapper(Consts.GameOptionsData.CARD_SETS, array);
        }
    }
}