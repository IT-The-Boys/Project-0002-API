package it.theboys.project0002api.storage;

import it.theboys.project0002api.enums.db.GameName;
import it.theboys.project0002api.model.MainLobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainLobbyStorage {
    private static Map<GameName, MainLobby> lobbyList;
    private static MainLobbyStorage instance;


    private MainLobbyStorage() {
        lobbyList = new HashMap<>();
        for (GameName game : GameName.values()) {
            lobbyList.put(game, new MainLobby(game));
        }

    }

    public static synchronized MainLobbyStorage getInstance() {
        if (instance == null) {
            instance = new MainLobbyStorage();
        }
        return instance;
    }

    public List<MainLobby> getLobbyList() {

        return new ArrayList<MainLobby>(lobbyList.values());
    }

    public Map<GameName, MainLobby> getLobbyMap(){
        return lobbyList;
    };
    public void setLobby(MainLobby mainLobby) {
        lobbyList.put(mainLobby.getLobbyId(), mainLobby);
    }
}
