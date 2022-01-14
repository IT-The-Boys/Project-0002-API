package it.theboys.project0002api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.theboys.project0002api.controller.dto.ConnectRequest;
import it.theboys.project0002api.controller.dto.StartGameServerRequest;
import it.theboys.project0002api.exception.InvalidChatException;
import it.theboys.project0002api.exception.InvalidGameException;
import it.theboys.project0002api.exception.InvalidParamException;
import it.theboys.project0002api.exception.NotFoundException;
import it.theboys.project0002api.model.Game;
import it.theboys.project0002api.model.GamePlay;
import it.theboys.project0002api.model.Player;
import it.theboys.project0002api.service.GameServerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameServerController {

    private final GameServerService gameServerService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    //Start tested and working
    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody StartGameServerRequest request) {
        log.info("start game request: {}", request);
        return ResponseEntity.ok(gameServerService.createGame(request.getPlayer(), request.getGameName()));
    }

    @PostMapping("/connect")
    public ResponseEntity<Game> connect(@RequestBody ConnectRequest request)
            throws InvalidParamException, InvalidGameException, InvalidChatException {
        log.info("connect request: {}", request);
        return ResponseEntity.ok(gameServerService.connectToGame(request.getPlayer(), request.getGameId(), request.getChatId()));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
        log.info("gameplay: {}", request);
        Game game = gameServerService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }

}
