package it.theboys.project0002api.controller;

import it.theboys.project0002api.enums.GameName;
import it.theboys.project0002api.service.cardgame.GameMainframeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/game/{gameName}/")
@AllArgsConstructor
public class GameController {
    private final GameMainframeService gameService;

    @PostMapping("/launch")
    public ResponseEntity<?> launchGameServer(
            @PathVariable GameName gameName,
            @RequestBody GameServerLaunchRequestDto<?> request) {
        return new ResponseEntity<>(gameService.launchGameServer(request), HttpStatus.OK);
    }
    @GetMapping("/config")
    public ResponseEntity<?> getGameDefaultConfig(
            @PathVariable GameName gameName) {
        try {
        return new ResponseEntity<>(gameService.getGameDefaultConfig(gameName), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> joinGameServer(String user, String password, String gameServerId) {
        return new ResponseEntity<>(gameService.joinGameServer(user, password, gameServerId), HttpStatus.OK);
    }

    public ResponseEntity<?> leaveGameServer(String user, String gameServerId) {
        return new ResponseEntity<>(gameService.leaveGameServer(user, gameServerId), HttpStatus.OK);
    }

    public ResponseEntity<?> terminateGameServer(String user, String gameServerId) {
        return new ResponseEntity<>(gameService.terminateGameServer(user, gameServerId), HttpStatus.OK);
    }

    public ResponseEntity<?> messageToServer(String user, String gameServerId) {
        return new ResponseEntity<>(gameService.messageToServer(user, gameServerId), HttpStatus.OK);
    }


}
