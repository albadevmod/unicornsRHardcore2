package com.textadventure;

import java.util.ArrayList;

public class QuestEvent {

    boolean eventStarted;
    boolean eventCompleted;
    

    int stageNumber;

    public String questEventName;
    public ArrayList<String> questEventTexts;

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

    private Runnable onStart;

    public void setOnStart(Runnable onStart) {
        this.onStart = onStart;
    }

    public void trigger(StringBuilder response) {
        if (!eventStarted) {
            eventStarted = true;
        }
    }

    public QuestEvent(String questEventName, int stageNumber) {
        this.questEventName = questEventName;
        this.stageNumber = stageNumber;
        this.questEventTexts = new ArrayList<>();
    }

    public void addEventText(String text) {
        questEventTexts.add(text);
    }

    public String getQuestEventName() {
        return questEventName;
    }

}