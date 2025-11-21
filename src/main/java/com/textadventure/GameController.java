package com.textadventure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:8080") 
public class GameController {

    @PostMapping("/start")
    public String startGame(@RequestBody String playerName) {
        return Game.startGame(playerName); // pass name directly
    }

    @PostMapping("/command")
    public String handleCommand(@RequestBody String command) {
        return Game.handleInput(command);
    }

    @PostMapping("/map")
    public String getCurrentMapBackground() {
        Level currentLevel = Game.gameEventHandler.activeChapter.getAssociatedLevel();
        if (currentLevel == null) return "#2b2b2b"; // fallback

        Player player = Game.player;
        if (player.hasMapForLevel(currentLevel)) {
            return currentLevel.getLevelMap().getImageDir(); // e.g. "/images/maps/candy_map.png"
        }
        return "#2b2b2b";
    }
    
    @PostMapping("/mapinfo")
    public String getMapInfo() {
        Level currentLevel = Game.gameEventHandler.activeChapter.getAssociatedLevel();
        String chapterName = Game.gameEventHandler.activeChapter.chapterName;
        
        if (currentLevel == null) return "#2b2b2b|unknown"; // fallback

        Player player = Game.player;
        if (player.hasMapForLevel(currentLevel)) {
            String mapUrl = currentLevel.getLevelMap().getImageDir();
            return mapUrl + "|" + chapterName;
        }
        return "#2b2b2b|" + chapterName;
    }
    
    @PostMapping("/gameover")
    public String isGameOver() {
        return String.valueOf(Game.player.isDead());
    }
}