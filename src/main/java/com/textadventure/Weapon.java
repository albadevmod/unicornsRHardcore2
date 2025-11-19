package com.textadventure;

public class Weapon extends Item {
    public int minDamage;
    public int maxDamage;

    public Weapon(String itemName, int minDamage, int maxDamage){
        super(itemName); // Call parent constructor
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
    }

}