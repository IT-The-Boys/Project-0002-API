package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.GameOptions;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.BaseJsonHandler;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.GamesManager;
import it.theboys.project0002api.singletons.Preferences;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

public class CreateGameHandler extends BaseHandler {
    public static final String OP = Consts.Operation.CREATE_GAME.toString();
    private final Preferences preferences;
    private final GamesManager gamesManager;

    public CreateGameHandler(@Annotations.Preferences Preferences preferences, @Annotations.GameManager GamesManager gamesManager) {
        this.preferences = preferences;
        this.gamesManager = gamesManager;
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) throws BaseJsonHandler.StatusException {
        String value = params.getStringNotNull(Consts.GameOptionsData.OPTIONS);
        GameOptions options = new GameOptions(preferences, value);
        return new JsonWrapper(Consts.GeneralKeys.GAME_ID, gamesManager.createGameWithPlayer(user, options).getId());
    }
}