package it.theboys.project0002api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.theboys.project0002api.enums.GameName;

@RestController
@RequestMapping("/api/v1")
public class LobbyController<LobbyService, Lobby> {
    @Autowired
    private LobbyService lobbyService;

    @GetMapping("/lobby")
    public ResponseEntity<?> getLobbyList(){
        return new ResponseEntity<>(lobbyService.getLobbyList(), HttpStatus.OK);
    }
    @GetMapping("/{gameName}/lobby")
    public ResponseEntity<Lobby> getLobbyInfo(@PathVariable GameName gameName){
        return new ResponseEntity<>(lobbyService.getLobby(gameName), HttpStatus.OK);
    }

    @PostMapping("/{gameName}/lobby/join")
    public ResponseEntity<?> joinLobby(
            @PathVariable GameName gameName,
            @RequestBody String request) throws LobbyException {
        try {
            return new ResponseEntity<>(lobbyService.joinLobby(gameName, request), HttpStatus.OK);
        } catch (LobbyException le){
            return new ResponseEntity<>(le.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @PostMapping("/{gameName}/lobby/leave")
    public ResponseEntity<?> leaveLobby(
            @PathVariable GameName gameName,
            @RequestBody String request) throws LobbyException {
        try {
            return new ResponseEntity<>(((Object) lobbyService).leaveLobby(gameName, request), HttpStatus.OK);
        } catch (LobbyException le){
            return new ResponseEntity<>(le.getMessage(),HttpStatus.NOT_FOUND);
        }
    }
}