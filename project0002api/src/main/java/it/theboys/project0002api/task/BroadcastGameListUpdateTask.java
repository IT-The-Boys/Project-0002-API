package it.theboys.project0002api.task;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.EventWrapper;
import it.theboys.project0002api.data.QueuedMessage.MessageType;
import it.theboys.project0002api.singletons.ConnectedUsers;

public class BroadcastGameListUpdateTask extends SafeTimerTask {
    private final ConnectedUsers users;
    private volatile boolean needsUpdate = false;

    public BroadcastGameListUpdateTask(final ConnectedUsers users) {
        this.users = users;
    }

    public void needsUpdate() {
        needsUpdate = true;
    }

    @Override
    public void process() {
        if (needsUpdate) {
            users.broadcastToAll(MessageType.GAME_EVENT, new EventWrapper(Consts.Event.GAME_LIST_REFRESH));
            needsUpdate = false;
        }
    }
}