package it.theboys.project0002api.model;

import it.theboys.project0002api.enums.db.GameName;
import it.theboys.project0002api.model.database.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MainLobby {
    private GameName lobbyId;
    private String lobbyName;
    private String lobbyImg;
    private List<String> playerList;
    private List<String> gameList;

    public MainLobby(GameName gameName) {
        lobbyId = gameName;
        lobbyName = gameName.getDesc();
        lobbyImg = String.format("https://itboys-project0002.s3.ap-northeast-1.amazonaws.com/img/lobby/%s.jpg", lobbyId.toString().toLowerCase());
        playerList = new ArrayList<>();
        gameList = new ArrayList<>();
    }

    public void addPlayer(String userId) {
        playerList.add(userId);
    }

    public void removePlayer(String userId) {
        playerList.remove(userId);
    }

    public void addServer(String server) {
        gameList.add(server);
    }

    public void removeServer(String server) {
        gameList.add(server);
    }
}
