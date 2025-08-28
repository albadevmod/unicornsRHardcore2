package com.textadventure;

import java.util.ArrayList;

public class Quest {
    public String questName;
    private QuestEvent questEvent;
    private boolean isActive;

    private QuestCondition startCondition = () -> true;
    private QuestCondition finishCondition = () -> false;
    
    private ArrayList<QuestEvent> questEventList = new ArrayList<>();

    public void addQuestEvent(QuestEvent questEvent){
        questEventList.add(questEvent);
    }

    public void addQuestEvents(ArrayList<QuestEvent> multipleQuestEvents){
        questEventList.addAll(multipleQuestEvents);
    }

    private int currentEventIndex = 0;

    public QuestEvent getCurrentEvent() {
        if (currentEventIndex < questEventList.size()) {
            return questEventList.get(currentEventIndex);
        }
        return null;
    }

    public void advanceEvent() {
        currentEventIndex++;
    }

    public void updateEvent() {
        QuestEvent event = getCurrentEvent();
        if (event != null && !event.eventCompleted && event.canStart()) {
            event.trigger();
            event.eventCompleted = true;
            advanceEvent();
        }
    }

    public Quest(String questName) {
        this.questName = questName;
    }

    public void setStartCondition(QuestCondition cond) { this.startCondition = cond; }
    public void setFinishCondition(QuestCondition cond) { this.finishCondition = cond; }

    public boolean canStart() { return startCondition.check(); }
    public boolean canFinish() { return finishCondition.check(); }
    
    public boolean previousQuestEventCompleted(int stageNumber) {
        if (stageNumber <= 1) return true; // First event, nothing to check
        QuestEvent prev = null;
        for (QuestEvent event : questEventList) {
            if (event.stageNumber == stageNumber - 1) {
                prev = event;
                break;
            }
        }
        return prev != null && prev.eventStarted && prev.eventCompleted;
    }

    // Called when the quest officially starts
    public void startQuest() {
        if (!isActive && canStart()) {
            isActive = true;
        }
    }

    // Called when the quest officially finishes
    public void finishQuest() {
        if (isActive && canFinish()) {
            isActive = false;
            System.out.print("Quest finished.");
        }
    }

    public boolean questIsFinished(Quest quest){
        for(QuestEvent questEvent : quest.questEventList) {
                if (questEvent.questEventName.equals("foxChaseEnd") && questEvent.eventCompleted) {
                    return true;
                }
            }
        return false;
    }

    public QuestEvent getPreviousEvent() {
        int index = currentEventIndex - 1;
        if (index >= 0 && index < questEventList.size()) {
            return questEventList.get(index);
        }
        return null;
    }
    
}