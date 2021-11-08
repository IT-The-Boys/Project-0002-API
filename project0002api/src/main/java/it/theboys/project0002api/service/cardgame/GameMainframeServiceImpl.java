package it.theboys.project0002api.service.cardgame;

import it.theboys.project0002api.dto.http.request.GameServerLaunchRequestDto;
import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.model.database.cah.CahGameConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameMainframeServiceImpl implements GameMainframeService {

    @Autowired
    private CardService cardService;

    @Override
    public String launchGameServer(GameServerLaunchRequestDto<?> request) {
        return "Created server with id \"sdfsdfsdf\"";
    }

    @Override
    public String joinGameServer(String player, String gamePassword, String gameServerId) {
        if (gamePassword.equals("pass")) {
        return String.format("Successfully joined to server %s", gameServerId );
        }
        return "Password error";
    }

    @Override
    public String leaveGameServer(String user, String gameServerId) {
        return "Success";
    }

    @Override
    public String terminateGameServer(String user, String gameServerId) {
        if (!gameServerId.equals("test2")) {
            return String.format("Could not found server  with id%s", gameServerId );
        }
        return String.format("Successfully terminated server %s", gameServerId );
    }

    @Override
    public String messageToServer(String user, String gameServerId) {
        return "received";
    }

    @Override
    public CahGameConfig getGameDefaultConfig(GameName gameName) {
        CahGameConfig response = new CahGameConfig();
        response.setSetList(cardService.getAllSets(gameName));
        return response;
    }
}
