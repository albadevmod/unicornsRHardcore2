package com.textadventure;

public class LevelMap {

    private String mapName;
    private String imageDir;

    public LevelMap(String mapName, String imageDir) {
        this.mapName = mapName;
        this.imageDir = imageDir;
    }

    public String getImageDir() {
        return imageDir;
    }
    
}