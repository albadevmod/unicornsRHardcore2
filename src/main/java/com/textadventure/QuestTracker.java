package com.textadventure;

import java.util.ArrayList;

public class QuestTracker {

    // Accesses DialogueManager
    public GameEventHandler gameEventHandler; 
    // Retrieve all chapters belonging to a level starting from here
    public Level currentLevel;
    // If you want to move NPCs, unlock areas, etc.
    public Chapter chapter;
    // Overarching quest object contianing relevant quest information and questEvents
    public Quest quest;
    // Singular "happenings" with progression ID attached to a quest
    public QuestEvent questEvent;
    // all quests are stored here
    public QuestLog questLog;

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

    public void setQuestLog(QuestLog questLog) {
        this.questLog = questLog;
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
        questLog.setupFoxChase();
        System.out.println("Set up all quests ...");
        // Add future quests here
    }

}