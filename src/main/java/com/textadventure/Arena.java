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

    public String checkPlayerHealth(Player player){
        if (player.health <= 0){
            combatStarted = false;
            return player.getPlayerName() + " has been defeated!";
        } else {
            return player.getPlayerName() + " is still standing!";
        }
    }

    public String receiveItemDropUponVictory(NPC enemyNPC){
        if (enemyNPC.itemDrop != null){
            player.addItemToInventory(enemyNPC.itemDrop);
            return enemyNPC.npcName + " dropped " + enemyNPC.itemDrop.itemName + ". It has been added to your inventory.";
        }
        return enemyNPC.npcName + " left nothing behind.";
    }

    private String executeCombatAction(String action, StringBuilder response) {
        switch (action) {
            case "poke":
                response.append(hitEnemy(player, enemyNPC)).append("\n");
                if (enemyNPC.health <= 0) {
                    return handleVictory("You poke the enemies eye out and claim victory!");
                }
                response.append(takeDamage(player, enemyNPC)).append("\n");
                break;
            case "flail arms":
                response.append(flailArms(enemyNPC)).append("\n");
                if (enemyNPC.health <= 0) {
                    return handleVictory("You defeat your enemy by a thousand bleeding cuts.");
                }
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
        
        // Check if player died after taking damage
        if (player.health <= 0) {
            combatStarted = false;
            // Extract the last damage message
            String[] lines = response.toString().split("\n");
            String deathCause = lines.length > 0 ? lines[lines.length - 1] : "You have been defeated!";
            
            // Build combat status for game over screen
            String combatStatus = "--- Final Combat Status ---\n" +
                                player.getPlayerName() + " HP: " + player.health + "\n" +
                                enemyNPC.npcName + " HP: " + enemyNPC.health;
            
            return response.append("\n\nYou have fallen in combat!\n")
                          .append("DEATH_CAUSE: ").append(deathCause).append("\n")
                          .append("COMBAT_STATUS: ").append(combatStatus).append("\n")
                          .append("GAME OVER.").toString();
        }
        
        return null; // Combat continues
    }
    
    private String handleVictory(String victoryMessage) {
        StringBuilder response = new StringBuilder();
        combatStarted = false;
        enemyNPC.isHostile = false;
        response.append(victoryMessage).append("\n");
        response.append(receiveItemDropUponVictory(enemyNPC));
        return response.toString();
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