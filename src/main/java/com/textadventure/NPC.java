package com.textadventure;

import java.util.ArrayList;

public class NPC {

    String npcName;
    int health;
    int armor;
    int attackDamage;
    Boolean isHostile = false;
    Item itemDrop;

    ArrayList <String> npcDialogue = new ArrayList<>();
    ArrayList <Quest> npcQuests = new ArrayList<>();
    ArrayList<Item> keyItems = new ArrayList<>();

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

    // if the enemy is hostlie but cannot be attacked (like a guard) and a special dialogue is needed for this case,
    // this is the method to call
    public String getsAttacked(String string){
        return string;
    }

}