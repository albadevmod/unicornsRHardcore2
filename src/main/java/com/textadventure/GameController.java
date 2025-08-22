package com.textadventure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080") 
public class GameController {

    private Game game;

    @PostMapping("/start")
    public String startGame(@RequestBody String playerName) {
        return game.startGame(playerName); // pass name directly
    }

    @PostMapping("/command")
    public String handleCommand(@RequestBody String command) {
        return game.handleInput(command);
    }

    @PostMapping("/map")
    public String getCurrentMapBackground() {
        Level currentLevel = game.gameEventHandler.activeChapter.getAssociatedLevel();
        if (currentLevel == null) return "#2b2b2b"; // fallback

        Player player = game.player;
        if (player.hasMapForLevel(currentLevel)) {
            return currentLevel.getLevelMap().getImageDir(); // e.g. "/images/maps/candy_map.png"
        }
        return "#2b2b2b";
    }
}