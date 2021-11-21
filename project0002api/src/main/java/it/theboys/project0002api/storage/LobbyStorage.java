package it.theboys.project0002api.storage;

import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.model.Lobby;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LobbyStorage {
    private static Map<GameName, Lobby> lobbyList;
    private static LobbyStorage instance;


    private LobbyStorage() {
        lobbyList = new HashMap<>();
        for (GameName game : GameName.values()) {
            lobbyList.put(game, new Lobby(game));
        }

    }

    public static synchronized LobbyStorage getInstance() {
        if (instance == null) {
            instance = new LobbyStorage();
        }
        return instance;
    }

    public List<Lobby> getLobbyList() {

        return new ArrayList<Lobby>(lobbyList.values());
    }

    public Map<GameName, Lobby> getLobbyMap(){
        return lobbyList;
    };
    public void setLobby(Lobby lobby) {

        lobbyList.put(lobby.getLobbyId(), lobby);
    }
}
