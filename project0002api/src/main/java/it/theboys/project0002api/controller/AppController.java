package it.theboys.project0002api.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class AppController {

    @GetMapping("/")
    public ResponseEntity<String> getServers() {
        // todo GetChatService
        // todo Get LobbyList
        return null;
    }
}
