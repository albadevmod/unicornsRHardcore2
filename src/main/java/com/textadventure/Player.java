package com.textadventure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Player {
    //Scanner playerNameInput = new Scanner(System.in);

    String playerName;
    int health;
    int stamina;
    Item item;

    ArrayList<String> playerDialogue = new ArrayList<>();
    ArrayList<Item> inventory = new ArrayList<>();
    ArrayList<Level> discoveredMaps; // Keeps track of maps the player has found
    
    public Player(String name){
        this.playerName = name;
        this.discoveredMaps = new ArrayList<>();
        this.health = 15;
        this.stamina = 5;
    }

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName (){
        return playerName;
    }

    // items & inventory
    public void addItemToInventory(Item item){
        inventory.add(item);
    }

    public void addMultipleItemsToInventory(ArrayList<Item> items){
        inventory.addAll(items);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public boolean hasItem(String itemName){
        return inventory.contains(item);
    }

    // Handle Map Logic
     public boolean hasMapForLevel(Level level) {
        return discoveredMaps.contains(level);
    }

    public void discoverMap(Level level) {
        discoveredMaps.add(level);
    }

    // quest memory
    private Set<String> seenEvents = new HashSet<>();

    // Event memory
    public void markSeen(String event) {
        seenEvents.add(event);
    }

    public boolean hasSeen(String event) {
        return seenEvents.contains(event);
    }


}