package it.theboys.project0002api.handlers;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.cardcast.FailedLoadingSomeCardcastDecks;
import it.theboys.project0002api.cards.CardSet;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import it.theboys.project0002api.singletons.PreparingShutdown;
import io.undertow.server.HttpServerExchange;

import java.util.List;

import org.jetbrains.annotations.NotNull;


public class StartGameHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.START_GAME.toString();

    public StartGameHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (game.getHost() != user) throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_GAME_HOST);
        if (game.getState() != Consts.GameState.LOBBY)
            throw new BaseCahHandler.CahException(Consts.ErrorCode.ALREADY_STARTED);

        PreparingShutdown.get().check();

        try {
            if (!game.hasEnoughCards()) {
                List<CardSet> cardSets = game.loadCardSets();
                JsonWrapper obj = new JsonWrapper();
                obj.add(Consts.GeneralGameData.BLACK_CARDS_PRESENT, game.blackCardsCount(cardSets));
                obj.add(Consts.GeneralGameData.BLACK_CARDS_REQUIRED, game.getRequiredBlackCardCount());
                obj.add(Consts.GeneralGameData.WHITE_CARDS_PRESENT, game.whiteCardsCount(cardSets));
                obj.add(Consts.GeneralGameData.WHITE_CARDS_REQUIRED, game.getRequiredWhiteCardCount());
                throw new BaseCahHandler.CahException(Consts.ErrorCode.NOT_ENOUGH_CARDS, obj);
            } else {
                game.start();
                return JsonWrapper.EMPTY;
            }
        } catch (FailedLoadingSomeCardcastDecks ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.CARDCAST_CANNOT_FIND,
                    new JsonWrapper(Consts.GeneralKeys.CARDCAST_ID, ex.getFailedJson()));
        }
    }
}