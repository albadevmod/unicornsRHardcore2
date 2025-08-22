package com.textadventure;
import java.util.ArrayList;

public class GameEventHandler {

    String inputString;
    Player player;
    Chapter activeChapter;
    DialogueManager dialogueManager;
    Arena arena;

    public GameEventHandler(Player player, Chapter activeChapter, DialogueManager dialogueManager, Arena arena){
        this.player = player;
        this.activeChapter = activeChapter;
        this.dialogueManager = dialogueManager;
        this.arena = arena;
    }


    // Method to process input from the player
    public String processInput (String inputString){
        StringBuilder response = new StringBuilder();
        inputString = inputString.toLowerCase();

        if (inputString.equals("north") || inputString.equals("south") ||
            inputString.equals("west") || inputString.equals("east")) {
            if(activeChapter.nextChapters.get(inputString) == null){
                response.append("You can't go that way.");
            } else {
                response.append(movePlayer(inputString));
            }

        } else if(inputString.equals("exit")) {
            response.append("Exiting game...");

        } else if (inputString.startsWith("inspect ")) {
            String itemName = inputString.substring(8).trim();
            response.append(inspectItem(itemName));

        } else if (inputString.startsWith("inspect")) {
            response.append(inspect());

        } else if(inputString.equals("inventory")) {
            response.append(displayInventory());

        } else if (inputString.startsWith("talk ")) {
            String inputStringName = inputString.substring(5).trim();
            NPC currentNPC = null;
            for(NPC npc : activeChapter.chapterNPCList){
                if(inputStringName.equalsIgnoreCase(npc.npcName)){
                    currentNPC = npc;
                    System.out.println("Current NPC: " + currentNPC.npcName);
                    break;
                }
            }
            if (currentNPC != null){
                response.append(dialogueManager.talkToNPC(currentNPC));
                currentNPC = null;
                System.out.println("Dialogue finished, NPC cleared.");
            } else {
                response.append("This doesn't seem to be anybody's name here.");
            }
        } else {
            response.append("Invalid input. You did a backflip. Try again.");
        }

        return response.toString();
    }

    // Method to move the player to a new chapter based on direction
    public String movePlayer(String direction){
        StringBuilder response = new StringBuilder();
        if (activeChapter.nextChapters.containsKey(direction)){
            Chapter nextChapter = activeChapter.nextChapters.get(direction);
            response.append(player.playerName + " moved " + direction + " to a new area.\n");
            activeChapter = nextChapter;
            //Output chapter name as current location indicator
            Level mapLevel = activeChapter.getAssociatedLevel();
            if (player.hasMapForLevel(mapLevel)) {
            response.append(activeChapter.chapterName + "\n");
            }
            response.append(nextChapter.startChapter());
        }
        return response.toString();
    }

    public String getPlayerInput (){
        //inputString = sc.nextLine().toLowerCase();
        processInput(inputString);
        return inputString;
    }

    public NPC getChapterNPC() {
        NPC currentNPC = activeChapter.getCurrentNPC();
        activeChapter.setCurrentNPC(currentNPC);
        return currentNPC;
    }

    // Method to inspect items in the current chapter
    public String inspect (){
    StringBuilder response = new StringBuilder();

    if(!activeChapter.chapterItemList.isEmpty()){
        response.append("-----------------------\n");
        response.append("You found some items. You added them to your *inventory*.\n");

        for(Item item : activeChapter.chapterItemList){
            response.append("- ").append(item.itemName).append("\n");
            player.addItemToInventory(item);
        }

        activeChapter.chapterItemList.clear();

        response.append("-----------------------\n");

        } else {
            response.append("-----------------------\n");
            response.append("There is nothing to be found...\n");
            response.append("-----------------------\n");
        }

        // 🔥 Check for map reward
        if (activeChapter.hasMapReward()) {
            Level mapLevel = activeChapter.getAssociatedLevel();
            if (!player.hasMapForLevel(mapLevel)) {
                player.discoverMap(mapLevel);
                response.append("\nYou unfold a mysterious map! It has been added to your knowledge.\n");
                activeChapter.hasLevelMapReward = false;
                // 🔔 Trigger UI update (via Game or GameEventHandler callback)
                //GameUI.showMap(mapLevel.getLevelMap().getImageDir()); // or use an observer pattern if needed
        }
    }

        return response.toString();
    }

    // Method to inspect a specific item in the players inventory
    public String inspectItem(String itemName) {
        ArrayList<Item> inventory = player.getInventory();
        for (Item item : inventory) {
            if (item.itemName.equalsIgnoreCase(itemName)) {
                StringBuilder response = new StringBuilder();
                response.append("-----------------------\n");
                response.append("Item: ").append(item.itemName).append("\n");
                response.append(item.getItemDescriptions()); // this method should return a String
                //response.append("Combinable?: ").append(item.combinationPossible).append("\n");
                return response.toString();
            }
        }
        return "Not an item in your inventory. Try again.";
    }

    // Method to display the player's inventory
    public String displayInventory(){
        StringBuilder response = new StringBuilder();
        ArrayList<Item> inventory = player.getInventory();

        if (inventory.isEmpty()){
            response.append("-----------------------\n");
            response.append("Your pockets are empty.\n");
            response.append("-----------------------\n");
            return response.toString();
        }

        response.append("-----------------------\n");
        response.append("Your inventory contains:\n");
        for (Item item : inventory){
            response.append("- ").append(item.itemName).append("\n");
        }
        response.append("-----------------------\n");
        response.append("Type 'inspect [itemName]' to inspect an item.");
        return response.toString();
    }
    
}