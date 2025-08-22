package com.textadventure;
import java.util.ArrayList;

public class Weapon {
    public String weaponName;
    public int baseDamage;

    ArrayList <String> weaponDescription = new ArrayList<>();

    public Weapon(String weaponName, int baseDamage){
        this.weaponName = weaponName;
        this.baseDamage = baseDamage;
    }

    public void addItemDescription(String itemDescription) {
        weaponDescription.add(itemDescription);
    }

    public ArrayList <String> getItemDescription(){
        return weaponDescription;
    }
}