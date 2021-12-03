package it.theboys.project0002api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.theboys.project0002api.model.Game;
import it.theboys.project0002api.model.Lobby;
import it.theboys.project0002api.service.LobbyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/lobby")
public class LobbyController {

    private final LobbyService lobbyService;
    // private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Map<Lobby, List<?>>> start(@RequestBody Game game) {
        log.info("start lobby request: {}", game);
        return ResponseEntity.ok(lobbyService.addGameServer(game));
    }

}
