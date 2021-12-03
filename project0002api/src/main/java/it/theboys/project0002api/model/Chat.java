package it.theboys.project0002api.model;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Chat {
    
    private String chatId;
    private final ArrayList<Player> players = new ArrayList<Player>();

    public void addPlayer(Player player){
        this.players.add(player);
    }
}
