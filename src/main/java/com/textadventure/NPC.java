package com.textadventure;

import java.util.ArrayList;

public class NPC {

    String npcName;
    int health;
    int armor;
    int attackDamage;
    Boolean isHostile = false;
    Boolean gameOverUponAttacking = false;
    Item itemDrop;

    ArrayList <String> npcDialogue = new ArrayList<>();
    ArrayList <Quest> npcQuests = new ArrayList<>();
    ArrayList<Item> keyItems = new ArrayList<>();

    String getsAttackedDialogue;

    public NPC(){};

    public NPC(String npcName, int health, int armor, int attackDamage){
        this.npcName = npcName;
        this.health = health;
        this.armor = armor;
        this.attackDamage = attackDamage;
        this.keyItems = keyItems;
    }

    public void addItemDrop(Item item){
        this.itemDrop = item;
    }

    public void addNPCDialogue(String npcDialogueString){
        npcDialogue.add(npcDialogueString);
    }

    public void addNPCQuest(Quest quest){
        npcQuests.add(quest);
    }

    public ArrayList <String> getNPCDialogue(){
        return npcDialogue;
    }

    public void setKeyItems(ArrayList<Item> keyItems){
        this.keyItems = keyItems;
    }

    // if the enemy is not hostile queue this dialogue
    public String getsAttacked(){
        if (this.gameOverUponAttacking == true){
            return "As you attack " + npcName + ", they step backwards quickly and sound the alarm!\n\n" +
                   "A hot stream of chocolate ganache is spilled from atop the tower, cooking you alive as it pours over you!\n\n" +
                   "You have been defeated by the authorities!\n\n" +
                   "Your chocolate charred flesh will not be remembered.\n" +
                   "GAME OVER";
        } else {
            return this.getAttackedDialogue();
        }
    }

    public void setGetsAttackedDialogue(String dialogue){
        this.getsAttackedDialogue = dialogue;
    }

    public String getAttackedDialogue(){
        if (getsAttackedDialogue == null) {
            return "The " + npcName + " seems unimpressed by your attack.";
        }
        return getsAttackedDialogue;
    }

    public void setGameOverUponAttacking(Boolean gameOverUponAttacking){
        this.gameOverUponAttacking = gameOverUponAttacking;
    }

}