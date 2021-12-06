package it.theboys.project0002api.game;

import java.util.prefs.Preferences;

import javax.validation.constraints.NotNull;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.JsonWrapper;
import it.theboys.project0002api.data.User;

public class SuggestedGameOptions extends GameOptions {
    private final User suggester;

    public SuggestedGameOptions(Preferences preferences, @NotNull User user, String text) {
        super(preferences, text);
        suggester = user;
    }

    public User getSuggester() {
        return suggester;
    }

    public JsonWrapper toJson(String id, boolean includePassword) {
        JsonWrapper wrapper = new JsonWrapper();
        wrapper.add(Consts.GameOptionsData.OPTIONS, super.toJson(includePassword));
        wrapper.add(Consts.GameSuggestedOptionsData.ID, id);
        wrapper.add(Consts.GameSuggestedOptionsData.SUGGESTER, suggester.getNickname());
        return wrapper;
    }
}