package com.textadventure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogueManager {
    
    Player player;
    NPC currentNPC;
    QuestTracker questTracker;

    Map<NPC, List<String>> basicDialogues;
    Map<NPC, Map<Item, String>> inventoryDialogues;
    Map<NPC, Integer> currentDialogueIndex;
    // NPC → quest name → stage → dialogue
    Map<NPC, Map<String, Map<Integer, String>>> questDialogues; 
    
    public DialogueManager(Player player) {
        this.player = player;
        this.questDialogues = new HashMap<>();
        this.basicDialogues = new HashMap<>();
        this.inventoryDialogues = new HashMap<>();
        this.currentDialogueIndex = new HashMap<>();
        this.questTracker = questTracker;
    }

    public void addQuestDialogue(NPC npc, String questName, Map<Integer, String> stageDialogues) {
        questDialogues.computeIfAbsent(npc, k -> new HashMap<>()).put(questName, stageDialogues);
    }

    public String checkQuestDialogue(NPC npc) {
        if (!questDialogues.containsKey(npc)) return null;

        for (Map.Entry<String, Map<Integer, String>> questEntry : questDialogues.get(npc).entrySet()) {
            String questName = questEntry.getKey();
            int stage = questTracker.questManager.getQuestStage(questName);
            Map<Integer, String> dialogues = questEntry.getValue();

            if (dialogues.containsKey(stage)) {
                return dialogues.get(stage);
            }
        }
        return null;
    }

    public void addBasicDialogue(NPC npc, List<String> dialogues) {
        basicDialogues.put(npc, dialogues);
        currentDialogueIndex.put(npc, 0);
        System.out.println("Added dialogues for NPC " + npc.npcName);
        System.out.println("Dialogues " + dialogues);
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

    public void addInventoryDialogue(NPC npc, Map<Item, String> dialogues){
        inventoryDialogues.put(npc, dialogues);
    }

    public String checkInventoryDialogue(){
        if (inventoryDialogues.containsKey(currentNPC)) {
            for(Map.Entry<Item, String> entry : inventoryDialogues.get(currentNPC).entrySet()){
                if (player.getInventory().contains(entry.getKey())){
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    public String talkToNPC(NPC npc){
        this.currentNPC = npc;

        String questDialogue = checkQuestDialogue(npc);
        System.out.print(questDialogue);
        if (questDialogue != null){
        // Run hook when quest dialogue matches stage
            for (String questName : questDialogues.getOrDefault(npc, Map.of()).keySet()) {
                int stage = questTracker.questManager.getQuestStage(questName);
                questTracker.runHookIfExists(questName, stage);
                questTracker.questManager.advanceQuestStage(questName);
            }
            return questDialogue;
        }

        String inventoryDialogue = checkInventoryDialogue();
        if (inventoryDialogue != null) {
            return inventoryDialogue;
        }

        return getBasicDialogue();
    }

}