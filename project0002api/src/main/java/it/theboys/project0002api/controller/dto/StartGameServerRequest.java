package it.theboys.project0002api.controller.dto;

import it.theboys.project0002api.model.Player;
import lombok.Data;

@Data
public class StartGameServerRequest {
    private Player player;
    private String gameName;
}
