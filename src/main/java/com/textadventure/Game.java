package com.textadventure;

public class Game {

    public static Player player;
    public static Book book;
    public static DialogueManager dialogueManager;
    public static GameEventHandler gameEventHandler;
    public static Arena arena;
    public static QuestTracker questTracker;
    public static QuestLog questLog;

    public static String startGame(String playerName) {
        player = new Player(playerName);
        gameEventHandler = new GameEventHandler(player, null);
        questLog = new QuestLog();
        questTracker = new QuestTracker(gameEventHandler);
        dialogueManager = new DialogueManager(gameEventHandler, questTracker);
        gameEventHandler.setDialogueManager(dialogueManager);
        book = new Book(gameEventHandler, questTracker);
        arena = new Arena(player, gameEventHandler);

        // Set activeChapter of gameEventHandler
        gameEventHandler.activeChapter = book.cottonCandyLand.initialChapter;
        gameEventHandler.setQuestTracker(questTracker);
        gameEventHandler.setArena(arena);
        questLog.setQuestTracker(questTracker);
        questTracker.setQuestLog(questLog);

        questTracker.setupAllQuests();

        StringBuilder introText = new StringBuilder();

        introText.append("Good luck, ").append(playerName).append(".\n\n");
        introText.append("-----------------------\n");
        introText.append("Welcome to Cotton Candy Land!\n");
        introText.append("The sky is a delicate arrangement of pink and blue candy flowing in sophisticated swirls.\n");
        introText.append("The sweet scent of adventure lies in the air.\n");
        introText.append("-----------------------\n");
        introText.append(book.cottonCandyLand.startLevel());

        return introText.toString();
    }

    public static String handleInput(String input) {
        try {
            if (gameEventHandler == null) {
                return "Game error: Handler is null. Please reload the page.";
            }
            return gameEventHandler.processInput(input);
        } catch (Exception e) {
            e.printStackTrace();
            return "Game error occurred: " + e.getMessage() + ". Please reload the page.";
        }
    }

}