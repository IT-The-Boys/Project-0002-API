package it.theboys.project0002api.task;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import it.theboys.project0002api.singletons.ConnectedUsers;


public class UserPingTask extends SafeTimerTask {
    private final ConnectedUsers users;
    private final ScheduledThreadPoolExecutor globalTimer;

    public UserPingTask(ConnectedUsers users, ScheduledThreadPoolExecutor globalTimer) {
        this.users = users;
        this.globalTimer = globalTimer;
    }

    @Override
    public void process() {
        users.checkForPingAndIdleTimeouts();
        globalTimer.purge();
    }
}