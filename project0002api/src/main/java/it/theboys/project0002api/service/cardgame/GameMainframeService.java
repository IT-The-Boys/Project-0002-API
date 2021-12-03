package it.theboys.project0002api.service.cardgame;


import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.model.database.cah.CahGameConfig;

public interface GameMainframeService {

    public String launchGameServer(GameServerLaunchRequestDto<?> request);

    public String joinGameServer(String user ,String password, String gameServerId);

    public String leaveGameServer(String user, String gameServerId);

    public String terminateGameServer(String user, String gameServerId);

    public String messageToServer(String user, String gameServerId);

    public CahGameConfig getGameDefaultConfig(GameName gameName);
}
