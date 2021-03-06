package it.theboys.project0002api.controller;

import it.theboys.project0002api.enums.db.GameName;
import it.theboys.project0002api.exception.LobbyException;
import it.theboys.project0002api.model.MainLobby;
import it.theboys.project0002api.service.MainLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1")
public class MainLobbyController {
    @Autowired
    private MainLobbyService lobbyService;

    @GetMapping("/lobby")
    public ResponseEntity<?> getLobbyList(){
        return new ResponseEntity<>(lobbyService.getLobbyList(), HttpStatus.OK);
    }
    @GetMapping("/{gameName}/lobby")
    public ResponseEntity<MainLobby> getLobbyInfo(@PathVariable GameName gameName){
        return new ResponseEntity<>(lobbyService.getLobby(gameName), HttpStatus.OK);
    }

    @PostMapping("/{gameName}/lobby/join")
    public ResponseEntity<?> joinLobby(
            @PathVariable GameName gameName,
            Principal principal) throws LobbyException {
        try {
            return new ResponseEntity<>(lobbyService.joinLobby(gameName, principal.getName()), HttpStatus.OK);
        } catch (LobbyException le){
            return new ResponseEntity<>(le.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @PostMapping("/{gameName}/lobby/leave")
    public ResponseEntity<?> leaveLobby(
            @PathVariable GameName gameName,
            Principal principal) throws LobbyException {
        try {
            return new ResponseEntity<>(lobbyService.leaveLobby(gameName, principal.getName()), HttpStatus.OK);
        } catch (LobbyException le){
            return new ResponseEntity<>(le.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}
