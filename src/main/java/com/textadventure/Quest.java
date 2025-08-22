package com.textadventure;

import java.util.HashMap;
import java.util.Map;

public class Quest {
    private final String name;
    private int stage = 0;

    private QuestCondition startCondition = () -> true;
    private QuestCondition finishCondition = () -> false;

    private final Map<Integer, Runnable> stageHooks = new HashMap<>();

    public Quest(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public int getStage() { return stage; }

    public void setStage(int stage) { this.stage = stage; }
    public void advanceStage() { stage++; }

    public void setStartCondition(QuestCondition cond) { this.startCondition = cond; }
    public void setFinishCondition(QuestCondition cond) { this.finishCondition = cond; }

    public boolean canStart() { return startCondition.check(); }
    public boolean canFinish() { return finishCondition.check(); }

    public void addStageHook(int stage, Runnable hook) {
        stageHooks.put(stage, hook);
    }

    public void runHookIfExists(int stage) {
        Runnable hook = stageHooks.get(stage);
        if (hook != null) hook.run();
    }
}