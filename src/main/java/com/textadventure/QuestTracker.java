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
            System.out.println("Current quest: " + quest.getCurrentEvent().questEventName + " Stage: " + quest.getCurrentEvent().stageNumber);
        }
    }

    public String getQuestEventText() {
        StringBuilder questText = new StringBuilder();
        for (Quest quest : activeQuests) {
            QuestEvent event = quest.getCurrentEvent();
            // Only show text if event has started, it hasn't been shown yet and event is not completed
            if (event != null && event.eventStarted && !event.eventCompleted && !event.textShown) {
                questText.append(event.getEventText());
                event.textShown = true;
            } else if (event != null && !event.eventStarted) {
                // Show previous event's text as a hint if allowed
                QuestEvent prev = quest.getPreviousEvent();
                    if (prev != null && prev.showTextAgain && gameEventHandler.activeChapter == prev.associatedChapter) {
                        questText.append(prev.getEventText());
                    }
            }
        }
        return questText.toString();
    }

    public String getQuestEventTextForNPC(NPC npc) {
        StringBuilder questText = new StringBuilder();
        for (Quest quest : npc.npcQuests) {
            QuestEvent event = quest.getCurrentEvent();
            // Only show text if event has started, it hasn't been shown yet and event is not completed
            if (event != null && event.eventStarted && !event.eventCompleted && !event.textShown) {
                questText.append(event.getEventText());
                event.textShown = true;
            } else if (event != null && !event.eventStarted) {
                // Show previous event's text as a hint if allowed
                QuestEvent prev = quest.getPreviousEvent();
                if (prev != null && prev.showTextAgain && gameEventHandler.activeChapter == prev.associatedChapter) {
                    questText.append(prev.getEventText());

                }
            }
        }
        return questText.toString();
    }

    public void completeAndAdvanceShownEvents() {
        for (Quest quest : activeQuests) {
            QuestEvent event = quest.getCurrentEvent();
            if (event != null && event.eventStarted && event.textShown && !event.eventCompleted) {
                if (event.onStart != null) {
                    event.onStart.run();
                }
            }
        }
    }

    public void completeAndAdvanceShownEventsForNPC(NPC npc) {
        for (Quest quest : npc.npcQuests) {
            QuestEvent event = quest.getCurrentEvent();
            if (event != null && event.eventStarted && event.textShown && !event.eventCompleted) {
                if (event.onStart != null) {
                    event.onStart.run();
                }
            }
        }
    }

    public void setupAllQuests() {
        setupFoxChase();
        System.out.println("Set up all quests ...");
        // Add future quests here
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
        introFoxChase.associatedChapter = startChapter;
        introFoxChase.addEventText("-----------------------");
        introFoxChase.addEventText("As you walk through the outskirts of Sweetopolis, a sudden rustling in the bushes catches your attention.");
        introFoxChase.addEventText("It's a fox!");
        introFoxChase.addEventText("-----------------------");
        introFoxChase.showTextAgain = true;
        introFoxChase.setStartCondition(() -> gameEventHandler.activeChapter == startChapter);
        introFoxChase.setOnStart(() -> {
            introFoxChase.eventStarted = true;
            // event logic here
            if (introFoxChase.textShown == true){
                introFoxChase.eventCompleted = true;
                foxChase.advanceEvent();
            }
            if (introFoxChase.textShown && introFoxChase.showTextAgain) {
                introFoxChase.textShown = false;
            }
        });

        QuestEvent foxChaseStart = new QuestEvent("foxChaseStart", 2);
        foxChaseStart.associatedChapter = startChapter;
        foxChaseStart.addEventText("-----------------------");
        foxChaseStart.addEventText("The sly fox darts away northward.");
        foxChaseStart.addEventText("-----------------------");
        foxChaseStart.setStartCondition(() -> gameEventHandler.lastAction.equalsIgnoreCase("talk fox") && foxChase.previousQuestEventCompleted(foxChaseStart.stageNumber));
        foxChaseStart.setOnStart(() -> {
            foxChaseStart.eventStarted = true;
            // event logic here
            if (foxChaseStart.textShown == true){
                currentLevel.moveNPC(fox, startChapter, currentLevel.getChapter("City Outskirt East"));
                foxChaseStart.eventCompleted = true;
                foxChase.advanceEvent();
            }
        });

        QuestEvent foxChaseMid = new QuestEvent("foxChaseMid", 3);
        foxChaseMid.associatedChapter = currentLevel.getChapter("City Outskirt East");
        foxChaseMid.setStartCondition(() -> gameEventHandler.activeChapter == currentLevel.getChapter("City Outskirt East") && foxChase.previousQuestEventCompleted(foxChaseMid.stageNumber));
        foxChaseMid.addEventText("-----------------------");
        foxChaseMid.addEventText("Following the fox, you see it is holding something shiny in its mouth. The fox spots you and continues running east.");
        foxChaseMid.addEventText("-----------------------");
        foxChaseMid.setOnStart(() -> {
            foxChaseMid.eventStarted = true;
            // event logic here
            if (foxChaseMid.textShown == true){
                currentLevel.moveNPC(fox, currentLevel.getChapter("City Outskirt East"), currentLevel.getChapter("Willow Tree Forest"));
                foxChaseMid.eventCompleted = true;
                foxChase.advanceEvent();
            }
        });

        QuestEvent foxChaseEnd = new QuestEvent("foxChaseEnd", 4);
        foxChaseEnd.associatedChapter = currentLevel.getChapter("Willow Tree Forest");
        foxChaseEnd.setStartCondition(() -> gameEventHandler.activeChapter == currentLevel.getChapter("Willow Tree Forest") && foxChase.previousQuestEventCompleted(foxChaseEnd.stageNumber));
        foxChaseEnd.addEventText("-----------------------");
        foxChaseEnd.addEventText("Scurrying into the bushes, the fox watches you from the safety of the undergrowth. You can only see its eyes gleaming within the shadows.");
        foxChaseEnd.addEventText("-----------------------");
        foxChaseEnd.showTextAgain = true;
        foxChaseEnd.setOnStart(() -> {
            foxChaseEnd.eventStarted = true;
            // event logic here
            if (foxChaseEnd.textShown == true){
                foxChaseEnd.eventCompleted = true;
                foxChase.finishQuest();
                finishedQuests.add(foxChase);
                fox.npcQuests.remove(foxChase);
                System.out.println("Quest finished: " + foxChase.questName);
            }
        });

        foxChase.addQuestEvents(new ArrayList<QuestEvent>(Arrays.asList(introFoxChase, foxChaseStart , foxChaseMid, foxChaseEnd)));

    }
}