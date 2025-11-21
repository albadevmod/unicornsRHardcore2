package com.textadventure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogueManager {
    
    private final Player player;
    private NPC currentNPC;
    public QuestTracker questTracker;
    public GameEventHandler gameEventHandler;

    private final Map<NPC, List<String>> basicDialogues;
    private final Map<NPC, Map<Item, String>> inventoryDialogues;
    private final Map<NPC, Integer> currentDialogueIndex;
    // NPC → quest name → stage → dialogue
    private final Map<NPC, Map<String, Map<Integer, String>>> questDialogues; 
    
    public DialogueManager(GameEventHandler gameEventHandler, QuestTracker questTracker) {
        this.gameEventHandler = gameEventHandler;
        this.player = gameEventHandler.player;
        this.questTracker = questTracker;
        this.questDialogues = new HashMap<>();
        this.basicDialogues = new HashMap<>();
        this.inventoryDialogues = new HashMap<>();
        this.currentDialogueIndex = new HashMap<>();
    }

    // ----------------------
    // Quest dialogues
    // ----------------------
    public void addQuestDialogue(NPC npc, String questName, Map<Integer, String> stageDialogues) {
        questDialogues.computeIfAbsent(npc, k -> new HashMap<>()).put(questName, stageDialogues);
    }

    // ----------------------
    // Basic dialogues (rotating)
    // ----------------------
    public void addBasicDialogue(NPC npc, List<String> dialogues) {
        basicDialogues.put(npc, dialogues);
        currentDialogueIndex.put(npc, 0);
    }

    private String getBasicDialogue() {
        List<String> dialogues = basicDialogues.get(currentNPC);
        if (dialogues != null && !dialogues.isEmpty()) {
            int index = currentDialogueIndex.get(currentNPC);
            String dialogue = dialogues.get(index);
            currentDialogueIndex.put(currentNPC, (index + 1) % dialogues.size());
            return dialogue;
        } else {
            return "The NPC has nothing to say.";
        }
    }

    // ----------------------
    // Inventory dialogues
    // ----------------------
    public void addInventoryDialogue(NPC npc, Map<Item, String> dialogues){
        inventoryDialogues.put(npc, dialogues);
    }

    public String checkInventoryDialogue(){
        if (inventoryDialogues.containsKey(currentNPC)) {
            for(Map.Entry<Item, String> entry : inventoryDialogues.get(currentNPC).entrySet()){
                // Check if player has an item with the same name as the key item
                for (Item playerItem : player.getInventory()) {
                    if (playerItem.itemName.equalsIgnoreCase(entry.getKey().itemName)) {
                        return entry.getValue();
                    }
                }
            }
        }
        return null;
    }

    // ----------------------
    // Public re-usable accessors for quests
    // ----------------------
    public String getInventoryDialogueFor(NPC npc) {
        this.currentNPC = npc;
        return checkInventoryDialogue();
    }

    public String getBasicDialogueFor(NPC npc) {
        this.currentNPC = npc;
        return getBasicDialogue();
    }

    // ----------------------
    // Main entry point when player talks
    // ----------------------
    public String talkToNPC(NPC npc){
        this.currentNPC = npc;

        // Then inventory-based dialogues
        String inventoryDialogue = checkInventoryDialogue();
        if (inventoryDialogue != null) {
            return inventoryDialogue;
        }

        // Fallback rotating basic dialogue
        return getBasicDialogue();
    }
}
