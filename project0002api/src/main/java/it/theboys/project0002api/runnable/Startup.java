package it.theboys.project0002api.runnable;
import it.theboys.project0002api.model.cah.GameEngine;

public class Startup extends Thread {
    private final GameEngine cah;

    public Startup(final GameEngine cah) {
        this.cah = cah;
    }

    @Override
    public void run() {
        System.out.println("Shutting down...");
        String[] users = new String[this.cah.players.size()];
        for (int i = 0; i < this.cah.players.size(); i++) {
            users[i] = this.cah.players.get(i).getName();
        }
        System.out.println("Nearly done!");
        try {
            Thread.sleep(1000);
        } catch (final InterruptedException e) {
        }
        try {
            Thread.sleep(3000);
        } catch (final InterruptedException e) {
        }
        System.exit(1);
    }
}