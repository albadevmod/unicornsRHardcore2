package com.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chapter {

    // We need to pass the Player & GameEventHandler instantiated in Game to the class Chapter
    Player player;
    Item item;
    NPC currentNPC;
    String chapterName;

    // Attach String direction (key) to Chapter in HashMap
    // to add items, use .put()
    // to access value, use .get()
    // find out how many items there are, use .size()
    // for-each, keySet() method for keys only, values() method for values only
    Map<String, Chapter> nextChapters = new HashMap<>();
    ArrayList<Item> chapterItemList = new ArrayList<>();
    ArrayList <String> storytext = new ArrayList<>();
    ArrayList <NPC> chapterNPCList = new ArrayList<>();

    /********************************************
     *            CHAPTER FUNCTIONS             *
    ********************************************/

    // Constructor
    public Chapter(Player player, String chapterName, Level associatedLevel){
        this.player = player;
        this.chapterName = chapterName;
        if(!chapterItemList.isEmpty()){
            item = chapterItemList.get(0);
        }
        this.associatedLevel = associatedLevel;
    }

    // fill string Array storytext with actual text
    public void addStoryText(String text) {
        storytext.add(text);
    }

    // add function addNextChapter(), call direction(key) to nextChapter
    public void addNextChapter(String direction, Chapter chapter){
        nextChapters.put(direction, chapter);
    }

    public void addNextChapters(Chapter directionNorth, Chapter directionEast, Chapter directionSouth, Chapter directionWest){
        addNextChapter("north", directionNorth);
        addNextChapter("east", directionEast);
        addNextChapter("south", directionSouth);
        addNextChapter("west", directionWest);
    }

    // starts new chapter
    public String startChapter(){
        StringBuilder chapterIntro = new StringBuilder();
        chapterIntro.append(String.join("\n", storytext)).append("\n");
        chapterIntro.append("-----------------------\n");
        chapterIntro.append("What do you want to do?\n");
        chapterIntro.append("-----------------------\n");
        return chapterIntro.toString();
    }

    /********************************************
     *            ITEM FUNCTIONS                *
     ********************************************/

    public void addItem(Item item){
        chapterItemList.add(item);
        if(this.item == null){
            this.item = item;
        }
    }

    public Item getItem(){
        return item;
    }

    public void removeItem(Item item){
        chapterItemList.remove(item);
    }

    // returns a string with all items in the chapter
    public String getChapterItemListText() {
        if (chapterItemList.isEmpty()) {
            return "There are no items here.\n";
        }
        StringBuilder sb = new StringBuilder("Items in this area:\n");
        for (Item item : chapterItemList) {
            sb.append("- ").append(item.itemName).append("\n");
        }
        return sb.toString();
    }

    // returns a string with all possible directions to next chapters
    public String getAvailableDirectionsText() {
        if (nextChapters.isEmpty()) {
            return "There are no exits from this area.\n";
        }
        StringBuilder sb = new StringBuilder("Exits:\n");
        for (String direction : nextChapters.keySet()) {
            sb.append("- ").append(direction).append("\n");
        }
        return sb.toString();
    }

    // retrieve items attached to chapter by name
    public Item getItemByName(String itemName) {
        for (Item item : chapterItemList) {
            if (item.itemName.equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }
    
    /********************************************
     *            NPC FUNCTIONS                 *
     ********************************************/

    // target a single NPC in the chapter
    public void setCurrentNPC(NPC npc){
        this.currentNPC = npc;
    }

    public NPC getCurrentNPC(){
        return currentNPC;
    }

    //add all npcs in a chapter to a list so we can target them
    public ArrayList addNPCsToChapter(List<NPC> npcs) {
        chapterNPCList.addAll(npcs);
        return chapterNPCList;
    }

    public NPC getChapterNPCByName(String name) {
        for (NPC npc : chapterNPCList) {
            if (npc.npcName.equalsIgnoreCase(name)) return npc;
        }
        return null;
    }

    /********************************************
     *               CHAPTER HAS MAP?           *
     ********************************************/

    boolean hasLevelMapReward;
    Level associatedLevel;

    public boolean hasMapReward(){
        return hasLevelMapReward;
    }

    public void setMapReward(Level level){
        this.associatedLevel = level;
        this.hasLevelMapReward = true;
    }

    public Level getAssociatedLevel(){
        return associatedLevel;
    }

}