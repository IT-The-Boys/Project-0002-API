package it.theboys.project0002api.model;

import lombok.Data;

@Data
public class Chat {
    
    private String chatId;
    private Player player1;
    private Player player2;
}
