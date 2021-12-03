package it.theboys.project0002api.tasks;

import java.util.TimerTask;

import it.theboys.project0002api.enums.cah.GameServerStatus;
import it.theboys.project0002api.model.cah.GameEngine;

public class StartGame extends TimerTask {
    private final GameEngine cah;

    public StartGame(final GameEngine cah) {
        this.cah = cah;
    }

    @Override
    public void run() {
        if (this.cah.gameStatus != GameServerStatus.START) {
            if (this.cah.players.size() >= 3) {
                try {
                    this.cah.start();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                System.out.println("There are not enough players to start the game. There are " + this.cah.players.size() + " players when a minimum of 3 are needed. Will retry in 1 minute.");
            }
        }
    }
}