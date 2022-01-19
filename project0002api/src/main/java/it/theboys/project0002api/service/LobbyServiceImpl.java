package it.theboys.project0002api.service;

import it.theboys.project0002api.enums.db.GameName;
import it.theboys.project0002api.exception.LobbyException;
import it.theboys.project0002api.model.MainLobby;
import it.theboys.project0002api.storage.LobbyStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LobbyServiceImpl implements MainLobbyService {

    @Override
    public List<MainLobby> getLobbyList() {
        return LobbyStorage.getInstance().getLobbyList();
    }

    @Override
    public MainLobby getLobby(GameName gameName) {
        return LobbyStorage.getInstance().getLobbyMap().get(gameName);
    }

    @Override
    public MainLobby joinLobby(GameName gameName, String userId) throws LobbyException {
        if (!LobbyStorage.getInstance().getLobbyMap().containsKey(gameName)) {
            throw new LobbyException(LobbyException.NotFoundException(gameName));
        }
        MainLobby mainLobby = LobbyStorage.getInstance().getLobbyMap().get(gameName);
        //check if mainLobby contains player from request
        if (mainLobby.getPlayerList().contains(userId)) {
            return mainLobby;
        } else {
            mainLobby.addPlayer(userId);
        }
        LobbyStorage.getInstance().setLobby(mainLobby);
        return mainLobby;
    }

    @Override
    public MainLobby leaveLobby(GameName gameName, String userId) throws LobbyException {
        if (!LobbyStorage.getInstance().getLobbyMap().containsKey(gameName)) {
            throw new LobbyException(LobbyException.NotFoundException(gameName));
        }
        MainLobby mainLobby = LobbyStorage.getInstance().getLobbyMap().get(gameName);
        //check if mainLobby contains player from request
        if (!mainLobby.getPlayerList().contains(userId)) {
            throw new LobbyException(LobbyException.UserNotFoundException(gameName, userId));
        } else {
            mainLobby.removePlayer(userId);
        }
        LobbyStorage.getInstance().setLobby(mainLobby);
        return mainLobby;
    }


}
