package com.textadventure;
import java.util.ArrayList;
import java.util.Random;

public class Arena{

    // activeChapter --> NPCList --> chooseNPC ---> activeNPC

    Player player;
    GameEventHandler gameEventHandler;
    ArrayList <NPC> chapterNPCList;
    NPC enemyNPC;
    String lastAction;
    //ArrayList <NPC> enemyNPCs;
    //ArrayList <NPC> friendlyNPCs;
    Chapter activeChapter;

    Boolean combatStarted = false;

    public Arena (Player player, GameEventHandler gameEventHandler){
        this.player = player;
        this.gameEventHandler = gameEventHandler;
    }

    public String initiateCombat(Player player, NPC enemyNPC){
        return "Combat started between " + player.getPlayerName() + " and " + enemyNPC.npcName;
        // now we need to make processInputCombat handle the input during combat
    }

    public int calcDamage(Weapon weapon, NPC enemyNPC){
        Random random = new Random();
        int randomValue =  random.nextInt(weapon.maxDamage - weapon.minDamage + 1) + weapon.minDamage;
        int damage = randomValue - enemyNPC.armor;
        return damage;
    }

    public int calcEnemyDamage(NPC enemyNPC){
        Random random = new Random();
        int damage = random.nextInt(enemyNPC.attackDamage) + 1;
        return damage;
    }

    public String takeDamage(Player player, NPC enemyNPC){
        int damage = calcEnemyDamage(enemyNPC);
        player.health -= damage;
        if (damage <= 6) {
            return enemyNPC.npcName + " dealt " + damage + " damage to " + player.getPlayerName() + "!";
        } else {
            return enemyNPC.npcName + " landed a critical hit! " + damage + " damage dealt to " + player.getPlayerName() + "!";
        }
    }

    public String hitEnemy(Player player, NPC enemyNPC){
        Weapon weapon = (Weapon) player.weapon;
        int damage = calcDamage(weapon, enemyNPC);
        enemyNPC.health -= damage;
        return player.getPlayerName() + " hit " + enemyNPC.npcName + " for " + damage + " damage!";
    }

    public String flailArms(NPC enemyNPC){
        Random random = new Random();
        int damage = random.nextInt(2) + 1 - enemyNPC.armor; // damage between 1 and 2
        enemyNPC.health -= damage;
        return "You flail your arms wildly at " + enemyNPC.npcName + ", dealing " + damage + " damage!";
    }

    public String pushKick(NPC enemyNPC){
        enemyNPC.armor -= 1;
        return "A piece of armor flies off, armor lowered by 1!";
    }

    public String intimidate(NPC enemyNPC){
        enemyNPC.attackDamage -= 1;
        return enemyNPC.npcName + " attack damage lowered by 1 due to intimidation!";
    }

    public String checkEnemyHealth(NPC enemyNPC){
        if (enemyNPC.health <= 0){
            combatStarted = false;
            return enemyNPC.npcName + " has been defeated!";
        } else {
            return "The struggle continues...";
        }
    }

    public String checkPlayerHealth(Player player){
        if (player.health <= 0){
            combatStarted = false;
            return player.getPlayerName() + " has been defeated!";
        } else {
            return player.getPlayerName() + " is still standing!";
        }
    }

    public String processCombatInput (String inputString){
        gameEventHandler.lastAction = inputString;
        StringBuilder response = new StringBuilder();
        inputString = inputString.toLowerCase();
            //attack
        if (inputString.equals("poke")){
            response.append(hitEnemy(player, enemyNPC) + "\n");
            response.append(takeDamage(player, enemyNPC) + "\n");
            response.append(checkEnemyHealth(enemyNPC)).append("\n");
            response.append(checkPlayerHealth(player)).append("\n");
            // attack
        } else if (inputString.equals("flail arms")){
            response.append("You go on all fours, shaking uncontrollably and making screeching noises.\n");
            response.append(flailArms(enemyNPC) + "\n");
            response.append(takeDamage(player, enemyNPC) + "\n");
            // status effect
        } else if (inputString.equals("push kick")){
            response.append(pushKick(enemyNPC)).append("\n");
            response.append(takeDamage(player, enemyNPC)).append("\n");
            // status effect
        } else if (inputString.equals("intimidate")){
            response.append(intimidate(enemyNPC)).append("\n");
            response.append(takeDamage(player, enemyNPC)).append("\n");
        } else {
            response.append("Invalid combat action. Get back into the fight!");
        } if (combatStarted) {
            response.append("\n--- Combat Status ---\n");
            response.append(player.getPlayerName() + " HP: " + player.health + "\n");
            response.append(enemyNPC.npcName + " HP: " + enemyNPC.health + "\n");
            response.append("Enter your next action:\n");
        }
        return response.toString();
    }

}