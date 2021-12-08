package it.theboys.project0002api.handlers;


import it.theboys.project0002api.Consts;
import it.theboys.project0002api.cards.PyxCardSet;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;
import it.theboys.project0002api.game.GameOptions;
import it.theboys.project0002api.server.Annotations;
import it.theboys.project0002api.server.Parameters;
import it.theboys.project0002api.singletons.Emails;
import it.theboys.project0002api.singletons.LoadedCards;
import it.theboys.project0002api.singletons.Preferences;
import com.google.gson.JsonArray;
import io.undertow.server.HttpServerExchange;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class FirstLoadHandler extends BaseHandler {
    public static final String OP = Consts.Operation.FIRST_LOAD.toString();
    private final JsonWrapper defaultGameOptions;
    private final JsonArray cards;
    private final JsonWrapper authConfig;
    private final String serverStatusPage;

    public FirstLoadHandler(@Annotations.LoadedCards LoadedCards loadedCards,
                            @Annotations.Emails Emails emails,
                            @Annotations.Preferences Preferences preferences) {
        serverStatusPage = preferences.getStringNotEmpty("serverStatusPage", null);

        Set<PyxCardSet> cardSets = loadedCards.getLoadedSets();
        cards = new JsonArray(cardSets.size());
        for (PyxCardSet cardSet : cardSets) cards.add(cardSet.getClientMetadataJson().obj());

        defaultGameOptions = GameOptions.getOptionsDefaultsJson(preferences);

        authConfig = new JsonWrapper();
        if (emails.enabled()) authConfig.add(Consts.AuthType.PASSWORD, emails.senderEmail());
    }

    @NotNull
    @Override
    public JsonWrapper handle(User user, Parameters params, HttpServerExchange exchange) {
        JsonWrapper obj = new JsonWrapper();

        if (user == null) {
            obj.add(Consts.GeneralKeys.IN_PROGRESS, Boolean.FALSE)
                    .add(Consts.GeneralKeys.NEXT, Consts.Operation.REGISTER.toString());
        } else {
            // They already have a session in progress, we need to figure out what they were doing
            // and tell the client where to continue from.
            obj.add(Consts.GeneralKeys.IN_PROGRESS, Boolean.TRUE)
                    .add(Consts.UserData.NICKNAME, user.getNickname());

            if (user.getGame() != null) {
                obj.add(Consts.GeneralKeys.NEXT, Consts.ReconnectNextAction.GAME.toString())
                        .add(Consts.GeneralKeys.GAME_ID, user.getGame().getId());
            } else {
                obj.add(Consts.GeneralKeys.NEXT, Consts.ReconnectNextAction.NONE.toString());
            }
        }

        obj.add(Consts.GeneralKeys.AUTH_CONFIG, authConfig);
        obj.add(Consts.GameOptionsData.CARD_SETS, cards);
        obj.add(Consts.GameOptionsData.DEFAULT_OPTIONS, defaultGameOptions);
        obj.add(Consts.GeneralKeys.SERVER_STATUS_PAGE, serverStatusPage);

        return obj;
    }
}