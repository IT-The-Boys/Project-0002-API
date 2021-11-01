package it.theboys.project0002api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.theboys.project0002api.model.cah.GameEngine;

@RestController
@RequestMapping
public class GameEngineContoller {

    private GameEngine game;
    
    @GetMapping("/start")
    public GameEngine startserver(@RequestBody String settengs){
        game = new GameEngine();
        return game;
    }

    @GetMapping("/")
    public GameEngine game(@RequestBody String settengs){
        return game;
    }
}
