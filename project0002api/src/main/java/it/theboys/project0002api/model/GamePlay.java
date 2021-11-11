package it.theboys.project0002api.model;

import lombok.Data;

@Data
public class GamePlay {
    
    private TicToe type;
    private Integer coordinateX;
    private Integer coordinateY;
    private String gameId;
}
