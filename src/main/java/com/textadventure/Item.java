package com.textadventure;
import java.util.ArrayList;

public class Item {
    String itemName;
    String itemType; // consumable, distinct
    Boolean onGround = false;
    Boolean questItem = false;

    ArrayList <String> itemDescriptions = new ArrayList<>();

    public Item(String itemName){
        this.itemName = itemName;
    }

    public void addItemDescription(String itemDescription) {
        itemDescriptions.add(itemDescription);
    }

    public ArrayList <String> getItemDescription(){
        return itemDescriptions;
    }

    // New method to return descriptions as a single formatted String
    public String getItemDescriptions(){
        return String.join("\n", itemDescriptions) + "\n";
    }

    public String getItemByName(String name){
        if (this.itemName.equalsIgnoreCase(name)){
            return this.itemName;
        }
        return null;
    }
    
}