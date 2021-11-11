package it.theboys.project0002api.storage;

import java.util.HashMap;
import java.util.Map;
import it.theboys.project0002api.model.Lobby;

public class LobbyStorage {
    
    private static Map<String, Lobby> lobbies;
    private static LobbyStorage instance;

    private LobbyStorage(){
        lobbies = new HashMap<>();
    }

    public static synchronized LobbyStorage getInstance(){
        if(instance == null){
            instance = new LobbyStorage();
        }
        return instance;
    }

    public Map<String, Lobby> getLobbies(){
        return lobbies;
    }

    public void setLobby(Lobby lobby){
        lobbies.put(lobby.getLobbyId(), lobby);
    }
}
