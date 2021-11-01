package it.theboys.project0002api.service;

import it.theboys.project0002api.model.Player;
import it.theboys.project0002api.model.ServerHost;
import it.theboys.project0002api.model.ServerStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ServerService {

    public ServerHost hostServer(Player host){
        ServerHost server=new ServerHost();
        server.setServerId(UUID.randomUUID().toString());
        server.setServerHostUsername(host.getPlayername());
        server.setServerStatus(ServerStatus.NEW);
        return server;
    }
}
