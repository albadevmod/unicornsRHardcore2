package com.textadventure;

import java.util.ArrayList;

public class NPC {

    String npcName;
    int health;
    int stamina;

    ArrayList <String> npcDialogue = new ArrayList<>();
    ArrayList<Item> keyItems; 

    public NPC(){};

    public NPC(String npcName, int health, int stamina){
        this.npcName = npcName;
        this.health = health;
        this.stamina = stamina;
        this.keyItems = keyItems;
    }

    public void addNPCDialogue(String npcDialogueString){
        npcDialogue.add(npcDialogueString);
    }

    public ArrayList <String> getNPCDialogue(){
        return npcDialogue;
    }

}