package it.theboys.project0002api.data;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.game.Game;


public class EventWrapper extends JsonWrapper {

    public EventWrapper(Game game, Consts.Event event) {
        this(event);
        add(Consts.GeneralKeys.GAME_ID, game.getId());
    }

    public EventWrapper(Consts.Event event) {
        add(Consts.GeneralKeys.EVENT, event.toString());
    }
}