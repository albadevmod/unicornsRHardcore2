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
        String returnString = ("You go on all fours, shaking uncontrollably and making screeching noises.\n " + enemyNPC.npcName + " attack damage lowered by 1 due to intimidation!");
        enemyNPC.attackDamage -= 1;
        return returnString;
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

    private String executeCombatAction(String action, StringBuilder response) {
        switch (action) {
            case "poke":
                response.append(hitEnemy(player, enemyNPC)).append("\n");
                if (isCombatOver()) return response.append("You poke the enemies eye out and claim victory!").toString();
                response.append(takeDamage(player, enemyNPC)).append("\n");
                break;
            case "flail arms":
                response.append(flailArms(enemyNPC)).append("\n");
                if (isCombatOver()) return response.append("You defeat your enemy by a thousand bleeding cuts.").toString();
                response.append(takeDamage(player, enemyNPC)).append("\n");
                break;
            case "push kick":
                response.append(pushKick(enemyNPC)).append("\n");
                response.append(takeDamage(player, enemyNPC)).append("\n");
                break;
            case "intimidate":
                response.append(intimidate(enemyNPC)).append("\n");
                response.append(takeDamage(player, enemyNPC)).append("\n");
                break;
            default:
                response.append("Invalid combat action. Get back into the fight!");
                return response.toString();
        }
        
        // Check if combat ended after taking damage
        if (isCombatOver()) return response.append("\n\nGAME OVER.").toString();
        
        return null; // Combat continues
    }
    
    private boolean isCombatOver() {
        if (enemyNPC.health <= 0) {
            combatStarted = false;
            return true;
        }
        if (player.health <= 0) {
            combatStarted = false;
            return true;
        }
        return false;
    }
    
    private void appendCombatStatus(StringBuilder response) {
        response.append("\n--- Combat Status ---\n");
        response.append(player.getPlayerName()).append(" HP: ").append(player.health).append("\n");
        response.append(enemyNPC.npcName).append(" HP: ").append(enemyNPC.health).append("\n");
        response.append("Enter your next action:\n");
    }

    public String processCombatInput (String inputString){
        gameEventHandler.lastAction = inputString;
        StringBuilder response = new StringBuilder();
        String action = inputString.toLowerCase();
        
        String result = executeCombatAction(action, response);
        if (result != null) {
            return result; // Combat ended
        }
        
        appendCombatStatus(response);
        return response.toString();
    }

}