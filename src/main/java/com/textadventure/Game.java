package com.textadventure;

public class Game {

    public static Player player;
    public static Book book;
    public static DialogueManager dialogueManager;
    public static GameEventHandler gameEventHandler;
    public static Arena arena;
    public static QuestTracker questTracker;

    public static String startGame(String playerName) {
        player = new Player(playerName);
        dialogueManager = new DialogueManager(player);
        gameEventHandler = new GameEventHandler(player, null, dialogueManager, arena);
        questTracker = new QuestTracker(gameEventHandler);
        book = new Book(player, questTracker, dialogueManager);

        // Set activeChapter of gameEventHandler
        gameEventHandler.activeChapter = book.cottonCandyLand.initialChapter;
        dialogueManager.questTracker = questTracker;

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
        return gameEventHandler.processInput(input);
    }

}