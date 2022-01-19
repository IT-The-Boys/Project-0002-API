package it.theboys.project0002api.storage;

import java.util.HashMap;
import java.util.Map;
import it.theboys.project0002api.model.Lobby;
import it.theboys.project0002api.model.WebSocketChannel;

public class LobbyStorage {

    private static Map<String, WebSocketChannel<Lobby>> lobbies;
    private static LobbyStorage instance;
    private static WebSocketChannel<?> webSocketChannel;

    private LobbyStorage() {
        lobbies = new HashMap<>();
    }

    public static synchronized LobbyStorage getInstance() {
        if (instance == null) {
            instance = new LobbyStorage();
        }
        return instance;
    }

    public Map<String, WebSocketChannel<Lobby>> getLobbies() {
        return lobbies;
    }

    public void setLobby(Lobby lobby) {
        webSocketChannel = new WebSocketChannel<Lobby>();
        lobbies.put(lobby.getLobbyId(), (WebSocketChannel<Lobby>) webSocketChannel);
    }
}
