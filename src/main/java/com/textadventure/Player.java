package com.textadventure;

import java.util.ArrayList;

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

    /*public void askForPlayerName(){
        System.out.println("This is your first step into a strange adventure.");
        System.out.println("What is your name?");

        //playerName = playerNameInput.nextLine();
        System.out.println("Good luck, " + playerName + ".");
    }*/

    public void setPlayerName(String name) {
        this.playerName = name;
    }

    public String getPlayerName (){
        return playerName;
    }

    public void addItemToInventory(Item item){
        inventory.add(item);
    }

    public void addMultipleItemsToInventory(ArrayList<Item> items){
        inventory.addAll(items);
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    // Handle Map Logic
    
     public boolean hasMapForLevel(Level level) {
        return discoveredMaps.contains(level);
    }

    public void discoverMap(Level level) {
        discoveredMaps.add(level);
    }


}