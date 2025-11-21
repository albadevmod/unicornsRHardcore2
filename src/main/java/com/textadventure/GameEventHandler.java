package com.textadventure;
import java.util.ArrayList;

public class GameEventHandler {

    String inputString;
    String lastAction;
    Player player;
    Chapter activeChapter;
    DialogueManager dialogueManager;
    QuestTracker questTracker;
    Arena arena;

    public GameEventHandler(Player player, Chapter activeChapter){
        this.player = player;
        this.activeChapter = activeChapter;
        this.arena = arena;
    }
    
    public void setDialogueManager(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
    }

    public void setQuestTracker(QuestTracker questTracker) {
        this.questTracker = questTracker;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    // Method to process input from the player
    public String processInput (String inputString){
        this.lastAction = inputString;
        StringBuilder response = new StringBuilder();
        inputString = inputString.toLowerCase();

        if (arena != null && arena.combatStarted != null && arena.combatStarted) {
            return arena.processCombatInput(inputString);
        }
        if (inputString.equals("north") || inputString.equals("south") ||
            inputString.equals("west") || inputString.equals("east")) {
            if(activeChapter.nextChapters.get(inputString) == null){
                response.append("The path is blocked.");
            } else {
                response.append(movePlayer(inputString));
            }

        } else if(inputString.equals("exit")) {
            response.append("Exiting game...");

        } else if (inputString.startsWith("inspect ")) {
            String itemName = inputString.substring(8).trim();
            response.append(inspectItem(itemName));

        } else if(inputString.startsWith("give ")){
            String itemName = inputString.substring(5).trim();
            response.append(giveItem(itemName));
        }else if (inputString.startsWith("inspect")) {
            response.append(inspect());

        } else if(inputString.equals("inventory")) {
            response.append(displayInventory());

        } else if (inputString.startsWith("talk ")) {
            String inputStringName = inputString.substring(5).trim();
            NPC currentNPC = null;
            for(NPC npc : activeChapter.chapterNPCList){
                if(inputStringName.equalsIgnoreCase(npc.npcName)){
                    currentNPC = activeChapter.getChapterNPCByName(npc.npcName);
                    System.out.println("Current NPC: " + currentNPC.npcName);
                    break;
                }
            }
            if (currentNPC != null && !currentNPC.npcQuests.isEmpty()){
                questTracker.checkAndTriggerQuestEventsForNPC(currentNPC);
                String questText = questTracker.getQuestEventTextForNPC(currentNPC);
                if (!questText.trim().isEmpty()) {
                    // Quest event was triggered, show quest text
                    response.append(questText);
                    questTracker.completeAndAdvanceShownEventsForNPC(currentNPC);
                    System.out.println("Talk used, Quests updated for NPC: " + currentNPC.npcName);
                } else {
                    // No quest event triggered, use normal dialogue
                    response.append(dialogueManager.talkToNPC(currentNPC));
                    System.out.println("Normal dialogue cued - no active quest events.");
                }
            }
            else if (currentNPC != null){
                response.append(dialogueManager.talkToNPC(currentNPC));
                currentNPC = null;
                System.out.println("Normal dialogue cued.");
            } else {
                response.append("This doesn't seem to be anybody's name here.");
            }
        } else if(inputString.startsWith("attack ")) {
            String inputStringName = inputString.substring(7).trim();
            NPC currentNPC = null;
            for(NPC npc : activeChapter.chapterNPCList){
                if(inputStringName.equalsIgnoreCase(npc.npcName)){
                    currentNPC = activeChapter.getChapterNPCByName(npc.npcName);
                    System.out.println("Current NPC: " + currentNPC.npcName);
                    break;
                }
            }
            if (currentNPC == null) {
                response.append("There's no one here by that name to attack.");
            } else if (player.weapon == null) {
                response.append("You have no weapon. What are you trying to do?");
            } else if (!currentNPC.isHostile) {
                String attackResult = currentNPC.getsAttacked();
                response.append(attackResult);
                // Check if it was a game over attack
                if (attackResult != null && attackResult.contains("GAME OVER")) {
                    // Set player as dead to prevent further processing
                    player.health = 0;
                }
            } else {
                // currentNPC != null && currentNPC.isHostile && player.weapon != null
                arena.enemyNPC = currentNPC; // Set the enemy NPC in arena
                arena.combatStarted = true;
                response.append(arena.initiateCombat(player, currentNPC));
                // DON'T call arena.processCombatInput here!
                response.append("\nA fight ensues!");
                response.append("\n\nEnter your combat action (poke, flail arms, push kick, intimidate)");
                System.out.println("Combat initiated with NPC: " + currentNPC.npcName);
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
            if (questTracker != null) {
                questTracker.checkAndStartQuests();
                questTracker.checkAndTriggerQuestEvents();
                String questText = questTracker.getQuestEventText();
                response.append(questText);
                questTracker.completeAndAdvanceShownEvents();
                System.out.println("Player moved, Quests updated.");
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
            if(item.onGround == true){
                response.append("- ").append(item.itemName).append("\n");
                player.addItemToInventory(item);
            }
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

    // Method to give an item to an NPC
    public String giveItem(String itemName) {
        StringBuilder response = new StringBuilder();
        
        // Find the item in player's inventory
        Item itemToGive = null;
        for (Item item : player.getInventory()) {
            if (item != null && item.itemName.equalsIgnoreCase(itemName)) {
                itemToGive = item;
                break;
            }
        }
        
        if (itemToGive == null) {
            response.append("You don't have a ").append(itemName).append(" to give.");
            return response.toString();
        }
        
        // Only quest items can be given away - regular items should never be transferable
        if (!itemToGive.questItem) {
            response.append("You can't give away the ").append(itemName).append(".");
            return response.toString();
        }
        
        // Check if there's an NPC in the current chapter that can receive this quest item
        NPC targetNPC = null;
        boolean canGiveItem = false;
        
        for (NPC npc : activeChapter.chapterNPCList) {
            // Check if this NPC has this item in their keyItems (items they want/need)
            for (Item keyItem : npc.keyItems) {
                if (keyItem.itemName.equalsIgnoreCase(itemName)) {
                    targetNPC = npc;
                    
                    // For quest items, verify they are part of the current active quest stage
                    if (questTracker != null && !npc.npcQuests.isEmpty()) {
                        canGiveItem = isItemValidForCurrentQuestStage(itemToGive, npc);
                    }
                    break;
                }
            }
            if (targetNPC != null) break;
        }
        
        if (targetNPC == null) {
            response.append("There's nobody here who wants your ").append(itemName).append(".");
            return response.toString();
        }
        
        if (!canGiveItem) {
            response.append("This doesn't seem like the right time to give the ").append(itemName).append(".");
            return response.toString();
        }
        
        // Remove item from player's inventory
        player.giveItem(itemToGive);
        
        response.append("-----------------------\n");
        response.append("You gave the ").append(itemName).append(" to ").append(targetNPC.npcName).append(".\n");
        
        // Check for quest events triggered by giving this item
        if (questTracker != null && !targetNPC.npcQuests.isEmpty()) {
            questTracker.checkAndTriggerQuestEventsForNPC(targetNPC);
            String questText = questTracker.getQuestEventTextForNPC(targetNPC);
            response.append(questText);
            questTracker.completeAndAdvanceShownEventsForNPC(targetNPC);
            System.out.println("Item given, Quests updated for NPC: " + targetNPC.npcName);
        } else {
            // Default response if no quest is triggered
            response.append(targetNPC.npcName).append(" accepts the ").append(itemName).append(".\n");
        }
        
        response.append("-----------------------\n");
        return response.toString();
    }
    
    // Helper method to validate if an item can be given for the current quest stage
    private boolean isItemValidForCurrentQuestStage(Item item, NPC npc) {
        // Check each active quest for this NPC
        for (Quest quest : npc.npcQuests) {
            QuestEvent currentEvent = quest.getCurrentEvent();
            
            // If there's no current event, this quest isn't active
            if (currentEvent == null) {
                continue;
            }
            
            // Check if this quest event expects this specific item to be given
            // This is determined by the quest event's start condition checking for "give [itemName]"
            if (currentEvent.canStart() && isQuestEventExpectingItem(currentEvent, item)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Helper method to check if a quest event is expecting a specific item
    private boolean isQuestEventExpectingItem(QuestEvent questEvent, Item item) {
        // This method checks if the current quest event's start condition is looking for this item
        // We simulate the action and check if the start condition would be satisfied
        String originalLastAction = this.lastAction;
        this.lastAction = "give " + item.itemName.toLowerCase();
        
        boolean expectsItem = questEvent.canStart();
        
        // Restore original last action
        this.lastAction = originalLastAction;
        
        return expectsItem;
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