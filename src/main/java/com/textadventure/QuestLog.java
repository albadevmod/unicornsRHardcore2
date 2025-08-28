package com.textadventure;

import java.util.ArrayList;
import java.util.Arrays;

public class QuestLog {

    public QuestTracker questTracker;

    public void setQuestTracker(QuestTracker questTracker) {
        this.questTracker = questTracker;
    }

    public void setupFoxChase() {

        Quest foxChase = new Quest("foxChase");
        // response will be passed in from GameEventHandler
        Chapter startChapter = questTracker.currentLevel.getChapter("City Outskirt South East");
        NPC fox = startChapter.getChapterNPCByName("fox");

        fox.npcQuests.add(foxChase);
        questTracker.potentialQuests.add(foxChase);

        foxChase.setStartCondition(() -> questTracker.gameEventHandler.activeChapter == startChapter);
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
        introFoxChase.setStartCondition(() -> questTracker.gameEventHandler.activeChapter == startChapter);
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
        foxChaseStart.setStartCondition(() -> questTracker.gameEventHandler.lastAction.equalsIgnoreCase("talk fox") && foxChase.previousQuestEventCompleted(foxChaseStart.stageNumber));
        foxChaseStart.setOnStart(() -> {
            foxChaseStart.eventStarted = true;
            // event logic here
            if (foxChaseStart.textShown == true){
                questTracker.currentLevel.moveNPC(fox, startChapter, questTracker.currentLevel.getChapter("City Outskirt East"));
                foxChaseStart.eventCompleted = true;
                foxChase.advanceEvent();
            }
        });

        QuestEvent foxChaseMid = new QuestEvent("foxChaseMid", 3);
        foxChaseMid.associatedChapter = questTracker.currentLevel.getChapter("City Outskirt East");
        foxChaseMid.setStartCondition(() -> questTracker.gameEventHandler.activeChapter == questTracker.currentLevel.getChapter("City Outskirt East") && foxChase.previousQuestEventCompleted(foxChaseMid.stageNumber));
        foxChaseMid.addEventText("-----------------------");
        foxChaseMid.addEventText("Following the fox, you see it is holding something shiny in its mouth. The fox spots you and continues running east.");
        foxChaseMid.addEventText("-----------------------");
        foxChaseMid.setOnStart(() -> {
            foxChaseMid.eventStarted = true;
            // event logic here
            if (foxChaseMid.textShown == true){
                questTracker.currentLevel.moveNPC(fox, questTracker.currentLevel.getChapter("City Outskirt East"), questTracker.currentLevel.getChapter("Willow Tree Forest"));
                foxChaseMid.eventCompleted = true;
                foxChase.advanceEvent();
            }
        });

        QuestEvent foxChaseEnd = new QuestEvent("foxChaseEnd", 4);
        foxChaseEnd.associatedChapter = questTracker.currentLevel.getChapter("Willow Tree Forest");
        foxChaseEnd.setStartCondition(() -> questTracker.gameEventHandler.activeChapter == questTracker.currentLevel.getChapter("Willow Tree Forest") && foxChase.previousQuestEventCompleted(foxChaseEnd.stageNumber));
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
                questTracker.finishedQuests.add(foxChase);
                fox.npcQuests.remove(foxChase);
                System.out.println("Quest finished: " + foxChase.questName);
            }
        });

        foxChase.addQuestEvents(new ArrayList<QuestEvent>(Arrays.asList(introFoxChase, foxChaseStart , foxChaseMid, foxChaseEnd)));

    }
}
