package com.textadventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Level {

    String levelName;
    Player player;
    LevelMap levelMap;
    Level currentLevel;

    public Chapter initialChapter;
    ArrayList<Chapter> chapterList = new ArrayList<>();

    // Constructor for Level
    public Level(String levelName, Player player){
        this.levelName = levelName;
        this.player = player;
    }

    public String startLevel(){
        return initialChapter.startChapter();
    }

    public LevelMap getLevelMap() {
        return levelMap;
    }

    public String getLevelName() {
        return levelName;
    }

        // Add chapters to chapterList
    public void addChapterToList(Chapter chapter){
        chapterList.add(chapter);
    }

    public ArrayList addChaptersToList (List<Chapter> chapters) {
        chapterList.addAll(chapters);
        return chapterList;
    }
    
    public Chapter getChapter(String chapterName){
        for (Chapter chapter : chapterList) {
            if (chapter.chapterName.equals(chapterName)) return chapter;
        }
        return null;
    }

    public void removeNPC(NPC npc, Chapter chapter) {
        chapter.chapterNPCList.remove(npc);
    }

    public void moveNPC(NPC npc, Chapter originChapter, Chapter destinationChapter){
        removeNPC(npc, originChapter);
        destinationChapter.addNPCsToChapter(Arrays.asList(npc));
    }

}
