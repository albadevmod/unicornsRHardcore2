package com.textadventure;

import java.util.ArrayList;

public class QuestEvent {

    boolean eventStarted;
    boolean eventCompleted;
    boolean textShown;
    boolean showTextAgain;

    int stageNumber;

    public String questEventName;
    public ArrayList<String> questEventText;
    public Chapter associatedChapter;

    private QuestCondition startCondition;

    // set up starting condition of this event
    public void setStartCondition(QuestCondition startCondition) {
        this.startCondition = startCondition;
    }

    public boolean canStart() {
        return startCondition == null || startCondition.check();
    }

    public boolean isStartConditionMet() {
        return startCondition != null && startCondition.check();
    }

    public Runnable onStart;

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    public void trigger() {
        if (!eventStarted) {
            eventStarted = true;
        }
    }

    public QuestEvent(String questEventName, int stageNumber) {
        this.questEventName = questEventName;
        this.stageNumber = stageNumber;
        this.questEventText = new ArrayList<>();
    }

    public void addEventText(String text) {
        questEventText.add(text);
    }

    
    public String getEventText() {
        StringBuilder eventText = new StringBuilder();
        for (String text : questEventText) {
            eventText.append(text).append("\n");
        }
        return eventText.toString();
    }

    public String getQuestEventName() {
        return questEventName;
    }

}