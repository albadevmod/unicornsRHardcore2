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
    public ArrayList<Quest> potentialQuests = new ArrayList<>();

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

    public void checkAndStartQuests() {
        ArrayList<Quest> toStart = new ArrayList<>();
        for (Quest quest : potentialQuests) {
            if (quest.canStart()) {
                quest.startQuest();
                activeQuests.add(quest);
                toStart.add(quest);
                System.out.println("Quest started: " + quest.getCurrentEvent().questEventName);
                checkAndTriggerQuestEvents();
            }
        }
        potentialQuests.removeAll(toStart);
    }

    public void checkAndTriggerQuestEvents() {
        for (Quest quest : activeQuests) {
            QuestEvent event = quest.getCurrentEvent();
            if (event != null && event.canStart()) {
                if (event.onStart != null) {
                    event.onStart.run();
                }
                event.trigger();
            }
            System.out.println("Current active quests: " + activeQuests.size());
            System.out.println("Current quest: " + quest.getCurrentEvent().questEventName + " Stage: " + quest.getCurrentEvent().stageNumber);
        }
    }

    public void checkAndTriggerQuestEventsForNPC(NPC npc) {
        for (Quest quest : npc.npcQuests) {
            QuestEvent event = quest.getCurrentEvent();
            if (event != null && event.canStart()) {
                if (event.onStart != null) {
                    event.onStart.run();
                }
                event.trigger();
            }
            System.out.println("Updating quest for NPC: " + npc.npcName);
            System.out.println("Current active quests: " + activeQuests.size());
            System.out.println("Current quest: " + quest.getCurrentEvent().questEventName + " Stage: " + quest.getCurrentEvent().stageNumber);
        }
    }

    public void setupAllQuests() {
        setupFoxChase();
        System.out.print("Set up all quests ...");
        // Add future quests here
    }

    public String getQuestText(ArrayList<String> questTextList) {
        StringBuilder questText = new StringBuilder();
        for (String string : questTextList) {
            questText.append(string).append("\n");
        }
        return questText.toString();
    }

    private void setupFoxChase() {

        Quest foxChase = new Quest("foxChase");
    // response will be passed in from GameEventHandler
        Chapter startChapter = currentLevel.getChapter("City Outskirt South East");
        NPC fox = startChapter.getChapterNPCByName("fox");

        fox.npcQuests.add(foxChase);
        potentialQuests.add(foxChase);

        foxChase.setStartCondition(() -> gameEventHandler.activeChapter == startChapter);
        foxChase.setFinishCondition(() -> {
            return foxChase.questIsFinished(foxChase) == true;
        });

        QuestEvent introFoxChase = new QuestEvent("introFoxChase", 1);
        introFoxChase.addEventText("As you walk through the outskirts of Sweetopolis, a sudden rustling in the bushes catches your attention.");
        introFoxChase.setStartCondition(() -> gameEventHandler.activeChapter == startChapter);
        introFoxChase.setOnStart(() -> {
            introFoxChase.eventStarted = true;
            // Play story text
            for (String text : introFoxChase.questEventTexts) {
                System.out.println(text);
            }
            introFoxChase.eventCompleted = true;
            foxChase.advanceEvent();
        });

        QuestEvent foxChaseStart = new QuestEvent("foxChaseStart", 2);
        foxChaseStart.addEventText("The sly fox darts away northward.");
        foxChaseStart.setStartCondition(() -> gameEventHandler.lastAction.equalsIgnoreCase("talk fox") && foxChase.previousQuestEventCompleted(foxChaseStart.stageNumber));
        foxChaseStart.setOnStart(() -> {
            foxChaseStart.eventStarted = true;
            // Play story text
            for (String text : foxChaseStart.questEventTexts) {
                System.out.println(text);
            }
            // my logic here
            currentLevel.moveNPC(fox, startChapter, currentLevel.getChapter("City Outskirt East"));
            foxChaseStart.eventCompleted = true;
            foxChase.advanceEvent();
        });

        QuestEvent foxChaseMid = new QuestEvent("foxChaseMid", 3);
        foxChaseMid.setStartCondition(() -> gameEventHandler.activeChapter == currentLevel.getChapter("City Outskirt East") && foxChase.previousQuestEventCompleted(foxChaseMid.stageNumber));
        foxChaseMid.addEventText("Following the fox, you see it is holding something shiny in its mouth. The fox glances back at you, but continues running east.");
        foxChaseMid.setOnStart(() -> {
            foxChaseMid.eventStarted = true;
            // Play story text
            for (String text : foxChaseMid.questEventTexts) {
                System.out.println(text);
            }
            // my logic here
            currentLevel.moveNPC(fox, currentLevel.getChapter("City Outskirt East"), currentLevel.getChapter("Willow Tree Forest"));
            foxChaseMid.eventCompleted = true;
            foxChase.advanceEvent();
        });

        QuestEvent foxChaseEnd = new QuestEvent("foxChaseEnd", 4);
        foxChaseEnd.setStartCondition(() -> gameEventHandler.lastAction.equalsIgnoreCase("talk fox") && foxChase.previousQuestEventCompleted(foxChaseEnd.stageNumber));
        foxChaseEnd.addEventText("Scurrying into the bushes, the fox watches you from the safety of the undergrowth. You can only see its eyes gleaming within the shadows.");
        foxChaseEnd.setOnStart(() -> {
            foxChaseEnd.eventStarted = true;
            // Play story text
            for (String text : foxChaseEnd.questEventTexts) {
                System.out.println(text);
            }
            // my logic here
            foxChaseEnd.eventCompleted = true;
            foxChase.finishQuest();
            finishedQuests.add(foxChase);
            System.out.println("Quest finished: " + foxChase.questName);
        });

        foxChase.addQuestEvents(new ArrayList<QuestEvent>(Arrays.asList(introFoxChase, foxChaseStart , foxChaseMid, foxChaseEnd)));

    }
}