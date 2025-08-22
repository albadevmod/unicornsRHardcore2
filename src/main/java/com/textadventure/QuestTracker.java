package com.textadventure;

import java.util.HashMap;
import java.util.Map;

public class QuestTracker {

    // Holds all functions to set up quests and queststructure
    public QuestManager questManager;
    // Accesses DialogueManager
    private GameEventHandler gameEventHandler; 
    // Retrieve all chapters belonging to a level starting from here
    private Level firstLevel;
    // If you want to move NPCs, unlock areas, etc.
    private Chapter chapter;


    public QuestTracker(GameEventHandler gameEventHandler) {
        this.questManager = new QuestManager();
        this.gameEventHandler = gameEventHandler;
        this.firstLevel = firstLevel;
    }

    public void setFirstLevel(Level firstLevel){
        this.firstLevel= firstLevel;
    }

    private Map<String, Map<Integer, Runnable>> questStageHooks = new HashMap<>();

    // Hooks
    public void registerQuestStageHook(String questName, int stage, Runnable action) {
        questStageHooks
            .computeIfAbsent(questName, k -> new HashMap<>())
            .put(stage, action);
    }

    public void runHookIfExists(String questName, int stage) {
        if (questStageHooks.containsKey(questName)) {
            Runnable hook = questStageHooks.get(questName).get(stage);
            if (hook != null) hook.run();
        }
    }

    public void setupAllQuests() {
        setupFoxChase();
        // Add future quests here
    }

    private void setupFoxChase() {
        
        Chapter startChapter = firstLevel.getChapter("City Outskirt South East");
        NPC fox = startChapter.getChapterNPCByName("fox");

        // Start the quest initially (or when first triggered)
        if (!questManager.isQuestStarted("foxChase")) {
            questManager.startQuest("foxChase");
        }

        Map<Integer, String> foxChaseDialogues = new HashMap<>();
        foxChaseDialogues.put(0, "The fox scurries away to the north!");
        foxChaseDialogues.put(1, "The fox eyes you and bolts east.");
        foxChaseDialogues.put(2, "Something something.");

        gameEventHandler.dialogueManager.addQuestDialogue(fox, "foxChase", foxChaseDialogues);

        registerQuestStageHook("foxChase", 0, () -> {
            Chapter currentChapter = startChapter;
            Chapter targetChapter = firstLevel.getChapter("City Outskirt East");
            firstLevel.moveNPC(fox, currentChapter, targetChapter);
        });

        registerQuestStageHook("foxChase", 1, () -> {
            Chapter currentChapter = firstLevel.getChapter("City Outskirt East");
            Chapter targetChapter = firstLevel.getChapter("Willow Tree Forest");
            firstLevel.moveNPC(fox, currentChapter, targetChapter);
        });

        registerQuestStageHook("foxChase", 2, () -> {
            gameEventHandler.dialogueManager.checkInventoryDialogue();
        });

    }

}
