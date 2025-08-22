package com.textadventure;
import java.util.ArrayList;

public class Item {
    String itemName;
    String itemType; // consumable, distinct
    boolean combinationPossible;

    ArrayList <String> itemDescriptions = new ArrayList<>();

    public Item(String itemName, boolean combinationPossible){
        this.itemName = itemName;
        this.combinationPossible = combinationPossible;
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
    
}