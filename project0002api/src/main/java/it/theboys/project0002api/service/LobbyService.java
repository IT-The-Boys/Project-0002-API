package it.theboys.project0002api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import it.theboys.project0002api.model.Game;
import it.theboys.project0002api.model.Lobby;
import it.theboys.project0002api.storage.GameStorage;

@Service
public class LobbyService {
    private List<String> serverList;
    // private List<String> userList;

    public Map<Lobby, List<?>> joinLobby(String user) {
        serverList.add(user);
        return updateLobby();
    }

    private Map<Lobby, List<?>> updateLobby() {
        Map<Lobby, List<?>> response = new HashMap<>();
        // response.put("gameserverList", serverList);
        // response.put("userList", userList);
        return response;
    }

    public Map<Lobby, List<?>> addGameServer(Game game) {
        return updateLobby();
    }

    public Map<String, Game> showGameServerList(){
        Map<String, Game> gameList = GameStorage.getInstance().getGames();
        return gameList;
    }
}
