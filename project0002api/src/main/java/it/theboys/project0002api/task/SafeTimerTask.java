package it.theboys.project0002api.task;

import org.slf4j.Logger;

public abstract class SafeTimerTask implements Runnable {
    private static final Logger logger = Logger.getLogger(SafeTimerTask.class);

    @Override
    public final void run() {
        try {
            process();
        } catch (final Exception e) {
            logger.error("Exception running SafeTimerTask", e);
        }
    }

    public abstract void process();
}