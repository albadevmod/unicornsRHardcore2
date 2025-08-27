package com.textadventure;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestTracker {

    // Accesses DialogueManager
    public GameEventHandler gameEventHandler; 
    // Retrieve all chapters belonging to a level starting from here
    private Level currentLevel;
    // If you want to move NPCs, unlock areas, etc.
    private Chapter chapter;
    // Overarching quest object contianing relevant quest information and questEvents
    public Quest quest;
    // Singular "happenings" with progression ID attached to a quest
    public QuestEvent questEvent;

    public ArrayList<Quest> activeQuests = new ArrayList<>();
    public ArrayList<Quest> finishedQuests = new ArrayList<>();

    public QuestTracker(GameEventHandler gameEventHandler) {
        this.gameEventHandler = gameEventHandler;
        this.currentLevel = null;
    }

    public void addActiveQuest(Quest quest){
        activeQuests.add(quest);
    }

    public void addFinishedQuest(Quest quest){
        finishedQuests.add(quest);
    }

    public void setCurrentLevel(Level currentLevel){
        this.currentLevel= currentLevel;
    }

    public void updateQuests(StringBuilder response) {
        for (Quest quest : activeQuests) {
            quest.update(response); // Each quest handles its own progression and event logic
        }
    }

    public void setupAllQuests() {
        setupFoxChase();
        System.out.print("setupAllQuests finished.");
        // Add future quests here
    }

    private void setupFoxChase() {

        Quest foxChase = new Quest("foxChase");
    // response will be passed in from GameEventHandler
        Chapter startChapter = currentLevel.getChapter("City Outskirt South East");
        NPC fox = startChapter.getChapterNPCByName("fox");

        fox.npcQuests.add(foxChase);

        foxChase.setStartCondition(() -> gameEventHandler.activeChapter == startChapter);
        foxChase.setFinishCondition(() -> {
            return foxChase.questIsFinished(foxChase) == true;
        });

        if(foxChase.canStart()){
            foxChase.startQuest();
            activeQuests.add(foxChase);
        }

        QuestEvent introFoxChase = new QuestEvent("introFoxChase", 1);
        introFoxChase.addEventText("As you walk through the outskirts of Sweetopolis, a sudden rustling in the bushes catches your attention.");
        introFoxChase.setStartCondition(() -> gameEventHandler.activeChapter == startChapter);
        introFoxChase.setOnStart((StringBuilder response) -> {
            // Play story text
            for (String text : introFoxChase.questEventTexts) {
                response.append(text).append("\n");
            }
            // my logic here
        });

        QuestEvent foxChaseStart = new QuestEvent("foxChaseStart", 2);
        foxChaseStart.addEventText("The sly fox darts away northward.");
        foxChaseStart.setStartCondition(() -> gameEventHandler.lastAction.equalsIgnoreCase("talk fox"));
        foxChaseStart.setOnStart((StringBuilder response) -> {
            // Play story text
            for (String text : foxChaseStart.questEventTexts) {
                response.append(text).append("\n");
            }
            // my logic here
            currentLevel.moveNPC(fox, startChapter, currentLevel.getChapter("City Outskirt East"));
        });

        QuestEvent foxChaseMid = new QuestEvent("foxChaseMid", 3);
        foxChaseMid.setStartCondition(() -> gameEventHandler.activeChapter == currentLevel.getChapter("City Outskirt East"));
        foxChaseMid.addEventText("Following the fox, you see it is holding something shiny in its mouth. The fox glances back at you, but continues running east.");
        foxChaseMid.setOnStart((StringBuilder response) -> {
            // Play story text
            for (String text : foxChaseMid.questEventTexts) {
                response.append(text).append("\n");
            }
            // my logic here
            currentLevel.moveNPC(fox, currentLevel.getChapter("City Outskirt East"), currentLevel.getChapter("Willow Tree Forest"));
        });

        QuestEvent foxChaseEnd = new QuestEvent("foxChaseEnd", 4);
        foxChaseEnd.setStartCondition(() -> gameEventHandler.lastAction.equalsIgnoreCase("talk fox"));
        foxChaseEnd.addEventText("Scurrying into the bushes, the fox watches you from the safety of the undergrowth. You can only see its eyes gleaming within the shadows.");
        foxChaseEnd.setOnStart((StringBuilder response) -> {
            // Play story text
            for (String text : foxChaseEnd.questEventTexts) {
                response.append(text).append("\n");
            }
            // my logic here
            foxChase.finishQuest();
            finishedQuests.add(foxChase);
        });

        foxChase.addQuestEvents(new ArrayList<QuestEvent>(Arrays.asList(introFoxChase, foxChaseStart , foxChaseMid, foxChaseEnd)));

    }
}