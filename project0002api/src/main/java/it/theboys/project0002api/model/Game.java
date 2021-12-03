package it.theboys.project0002api.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Game {

    private String gameId;
    private Chat chat;
    private final ArrayList<Player> players = new ArrayList<Player>();
    private GameStatus status;

    public void addPlayer(Player player) {
        this.players.add(player);
    }

}
