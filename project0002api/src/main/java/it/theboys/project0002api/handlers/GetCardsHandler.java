package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class GetCardsHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.GET_CARDS.toString();

    public GetCardsHandler(@Annotations.GameManager GamesManager gamesManager) {
        super(gamesManager);
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) {
        JsonWrapper obj = new JsonWrapper();
        obj.add(Consts.OngoingGameData.HAND, game.getHandJson(user));
        obj.addAll(game.getPlayerToPlayCards(user));
        obj.add(Consts.OngoingGameData.BLACK_CARD, game.getBlackCardJson());
        obj.add(Consts.OngoingGameData.WHITE_CARDS, game.getWhiteCardsJson(user));
        obj.add(Consts.GeneralKeys.GAME_ID, game.getId());

        return obj;
    }
}