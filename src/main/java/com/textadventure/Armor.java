package com.textadventure;

public class Armor extends Item {
    public int armorValue;

    public Armor(String itemName, int armorValue){
        super(itemName); // Call parent constructor
        this.armorValue = armorValue;
    }

}