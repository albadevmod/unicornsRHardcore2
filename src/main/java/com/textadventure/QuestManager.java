package com.textadventure;

import java.util.HashMap;
import java.util.Map;

public class QuestManager {
    private final Map<String, Quest> allQuests = new HashMap<>();
    private final Map<String, Quest> activeQuests = new HashMap<>();

    public Quest createQuest(String name) {
        Quest q = new Quest(name);
        allQuests.put(name, q);
        return q;
    }

    public boolean isQuestStarted(String name) {
        return activeQuests.containsKey(name);
    }

    public Quest getQuest(String name) {
        return allQuests.get(name);
    }

    public void startQuest(String name) {
        Quest q = allQuests.get(name);
        if (q != null && q.canStart() && !isQuestStarted(name)) {
            activeQuests.put(name, q);
            System.out.println("[Quest Started] " + name);
        }
    }

    public void tryFinishQuest(String name) {
        Quest q = activeQuests.get(name);
        if (q != null && q.canFinish()) {
            activeQuests.remove(name);
            System.out.println("[Quest Finished] " + name);
        }
    }

    public int getQuestStage(String name) {
        Quest q = activeQuests.get(name);
        return q != null ? q.getStage() : -1;
    }

    public void advanceQuestStage(String name) {
        Quest q = activeQuests.get(name);
        if (q != null) q.advanceStage();
    }

    public void runHookIfExists(String name) {
        Quest q = activeQuests.get(name);
        if (q != null) q.runHookIfExists(q.getStage());
    }
}
