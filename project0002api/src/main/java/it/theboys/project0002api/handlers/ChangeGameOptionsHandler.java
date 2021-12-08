package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.Game;
import it.theboys.project0002api.game.GameOptions;
import it.theboys.project0002api.game.SuggestedGameOptions;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseCahHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import it.theboys.project0002api.singletons.Preferences;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class ChangeGameOptionsHandler extends GameWithPlayerHandler {
    public static final String OP = Consts.Operation.CHANGE_GAME_OPTIONS.toString();
    private final Preferences preferences;

    public ChangeGameOptionsHandler(@Annotations.GameManager GamesManager gamesManager, @Annotations.Preferences Preferences preferences) {
        super(gamesManager);
        this.preferences = preferences;
    }

    @NotNull
    @Override
    public JsonWrapper handleWithUserInGame(User user, Game game, Parameters params, HttpServerExchange exchange) throws BaseCahHandler.CahException {
        if (game.getState() != Consts.GameState.LOBBY)
            throw new BaseCahHandler.CahException(Consts.ErrorCode.ALREADY_STARTED);

        User host = game.getHost();
        if (host == null) return JsonWrapper.EMPTY;

        String value = params.getStringNotNull(Consts.GameOptionsData.OPTIONS);
        if (value.isEmpty()) throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST);

        if (host == user) {
            game.updateGameSettings(new GameOptions(preferences, value));
            return JsonWrapper.EMPTY;
        } else {
            game.suggestGameOptionsModification(new SuggestedGameOptions(preferences, user, value));
            return new JsonWrapper(Consts.GameInfoData.HOST, host.getNickname());
        }
    }
}