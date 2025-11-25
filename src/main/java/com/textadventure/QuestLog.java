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
        introFoxChase.addEventText("It's a fox! It doesn't seem to be paying attention to you as it's digging up something in the ground ...");
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
                foxChase.advanceEvent();
            }
        });

        QuestEvent foxSword = new QuestEvent("foxSword", 5);
        foxSword.associatedChapter = questTracker.currentLevel.getChapter("Willow Tree Forest");
        foxSword.setStartCondition(() -> questTracker.gameEventHandler.lastAction.equalsIgnoreCase("give fish") && questTracker.gameEventHandler.activeChapter == questTracker.currentLevel.getChapter("Willow Tree Forest") && foxChase.previousQuestEventCompleted(foxSword.stageNumber));
        foxSword.addEventText("-----------------------");
        foxSword.addEventText("You fling the fish toward the fox and it snaps it out of the air. After some rustling, a sword comes flying out of the bushes, landing right in front of your feet.");
        foxSword.addEventText("You take the sword and equip it as your weapon.");
        foxSword.addEventText("-----------------------");
        foxSword.setOnStart(() -> {
            foxSword.eventStarted = true;
            // event logic here
            if (foxSword.textShown == true){
                foxSword.eventCompleted = true;
                // Create and add sword to player inventory
                Weapon sword = new Weapon("sword", 3, 5);
                sword.addItemDescription("A heavily used sword with a candy cane striped hilt and a blade made of crystallized sugar. It looks like somebody couldn't resist eating it ... it's got some bite marks.");
                questTracker.gameEventHandler.player.addItemToInventory(sword);
                questTracker.gameEventHandler.player.weapon = sword;
                foxChase.finishQuest();
                questTracker.finishedQuests.add(foxChase);
                fox.npcQuests.remove(foxChase);
                System.out.println("Quest finished: " + foxChase.questName);
            }
        });

        foxChase.addQuestEvents(new ArrayList<QuestEvent>(Arrays.asList(introFoxChase, foxChaseStart , foxChaseMid, foxChaseEnd, foxSword)));

        //////////////////////////////////////////////////////////////
        /// 
        Quest toadQuest = new Quest("toadQuest");
        // response will be passed in from GameEventHandler
        Chapter startChapterToadQuest= questTracker.currentLevel.getChapter("City Outskirt North East");
        NPC toad = startChapterToadQuest.getChapterNPCByName("toad");

        toad.npcQuests.add(toadQuest);
        questTracker.potentialQuests.add(toadQuest);

        toadQuest.setStartCondition(() -> questTracker.gameEventHandler.activeChapter == startChapterToadQuest 
            && questTracker.gameEventHandler.player.getInventory().stream().anyMatch(item -> item.itemName.equalsIgnoreCase("candyflowers")));
        toadQuest.setFinishCondition(() -> {
            // Check if the last event in the quest is completed
            QuestEvent lastEvent = toadQuest.getCurrentEvent();
            return lastEvent != null && lastEvent.eventCompleted;
        });

        QuestEvent getArmor = new QuestEvent("getArmor", 1);
        getArmor.associatedChapter = startChapterToadQuest;
        getArmor.addEventText("-----------------------");
        getArmor.addEventText("Hmmm ... are those candyflowers for me? You throw the candyflowers into the giant toad's mouth.");
        getArmor.addEventText("A nice sunny spot and candyflowers - have this piece of armor as a token of my gratitude!");
        getArmor.addEventText("You equip the armor.");
        getArmor.addEventText("-----------------------");
        getArmor.showTextAgain = true;
        getArmor.setStartCondition(() -> 
            questTracker.gameEventHandler.lastAction != null &&
            questTracker.gameEventHandler.lastAction.toLowerCase().contains("give candyflowers") && 
            questTracker.gameEventHandler.activeChapter == startChapterToadQuest
        );
            getArmor.setOnStart(() -> {
                getArmor.eventStarted = true;
                // event logic here
                if (getArmor.textShown == true){
                    getArmor.eventCompleted = true;
                    // Get armor 
                    Armor candyarmor = new Armor("candyarmor", 5);
                    candyarmor.addItemDescription("Three pieces of thick sugar glass armor of multiple layered caramel plates that protect your shoulders and chest. Stylish and edible!");
                    questTracker.gameEventHandler.player.addItemToInventory(candyarmor);
                    questTracker.gameEventHandler.player.armor = candyarmor;
                    // complete quest
                    toadQuest.finishQuest();
                    questTracker.finishedQuests.add(toadQuest);
                    toad.npcQuests.remove(toadQuest);
                    System.out.println("Quest finished: " + toadQuest.questName);
                }
                if (getArmor.textShown && getArmor.showTextAgain) {
                    getArmor.textShown = false;
                }
            });

        toadQuest.addQuestEvents(new ArrayList<>(Arrays.asList(getArmor)));
        
        //////////////////////////////////////////////////////////////

        Quest enterCity = new Quest("enterCity");
        // response will be passed in from GameEventHandler
        Chapter startChapterEnterCity = questTracker.currentLevel.getChapter("Sweetopolis Citygate South");
        NPC guard = startChapterEnterCity.getChapterNPCByName("guard");

        guard.npcQuests.add(enterCity);
        questTracker.potentialQuests.add(enterCity);

        enterCity.setStartCondition(() -> questTracker.gameEventHandler.activeChapter == startChapterEnterCity 
            && questTracker.gameEventHandler.player.getInventory().stream().anyMatch(item -> item.itemName.equalsIgnoreCase("horsemask")));
        enterCity.setFinishCondition(() -> {
            // Check if the last event in the quest is completed
            QuestEvent lastEvent = enterCity.getCurrentEvent();
            return lastEvent != null && lastEvent.eventCompleted;
        });

        QuestEvent enterCityWithHorsemask = new QuestEvent("enterCityWithHorsemask ", 1);
        enterCityWithHorsemask.associatedChapter = startChapterEnterCity;
        enterCityWithHorsemask.addEventText("-----------------------");
        enterCityWithHorsemask.addEventText("Your new drip has the guards looking much more friendly now.");
        enterCityWithHorsemask.addEventText("The guard nods approvingly and steps aside.");
        enterCityWithHorsemask.addEventText("'Welcome to Sweetopolis, fellow horseman! The gates are open to you.'");
        enterCityWithHorsemask.addEventText("");
        enterCityWithHorsemask.addEventText("*** QUEST COMPLETED: City Entry ***");
        enterCityWithHorsemask.addEventText("WIN_MESSAGE: Your new drip has the guards looking much more friendly now.\nThe guard nods approvingly and steps aside.\n'Welcome to Sweetopolis, fellow horseman! The gates are open to you.'\n\n*** QUEST COMPLETED: City Entry ***");
        enterCityWithHorsemask.addEventText("YOU WIN!");
        enterCityWithHorsemask.addEventText("-----------------------");
        enterCityWithHorsemask.showTextAgain = true;
        enterCityWithHorsemask.setStartCondition(() -> 
            questTracker.gameEventHandler.lastAction.equalsIgnoreCase("talk guard") && 
            questTracker.gameEventHandler.activeChapter == startChapterEnterCity &&
            questTracker.gameEventHandler.player.getInventory().stream().anyMatch(item -> item.itemName.equalsIgnoreCase("horsemask"))
        );
        enterCityWithHorsemask.setOnStart(() -> {
            enterCityWithHorsemask.eventStarted = true;
            // event logic here
            if (enterCityWithHorsemask.textShown == true){
                enterCityWithHorsemask.eventCompleted = true;
                // Quest completion logic  
                enterCity.finishQuest();
                questTracker.finishedQuests.add(enterCity);
                guard.npcQuests.remove(enterCity);
                System.out.println("Quest finished: " + enterCity.questName);
            }
            if (enterCityWithHorsemask.textShown && enterCityWithHorsemask.showTextAgain) {
                enterCityWithHorsemask.textShown = false;
            }
        });

        enterCity.addQuestEvents(new ArrayList<>(Arrays.asList(enterCityWithHorsemask)));

    }
}
