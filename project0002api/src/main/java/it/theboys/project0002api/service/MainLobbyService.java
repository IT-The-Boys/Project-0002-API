package it.theboys.project0002api.service;

import it.theboys.project0002api.enums.db.GameName;
import it.theboys.project0002api.exception.LobbyException;
import it.theboys.project0002api.model.MainLobby;

import java.util.List;

public interface MainLobbyService {
    List<MainLobby> getLobbyList();
    MainLobby getLobby(GameName gameName);
    MainLobby joinLobby(GameName gameName, String userId) throws LobbyException;

    MainLobby leaveLobby(GameName gameName, String userId) throws LobbyException;
}
