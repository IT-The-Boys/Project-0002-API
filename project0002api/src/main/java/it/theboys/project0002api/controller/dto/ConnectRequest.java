package it.theboys.project0002api.controller.dto;

import it.theboys.project0002api.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
    private String chatId;
}
