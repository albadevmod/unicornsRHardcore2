package com.textadventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Book {

    Player player;
    GameEventHandler gameEventHandler;
    QuestTracker questTracker;

    public Book(GameEventHandler gameEventHandler, QuestTracker questTracker){
        this.gameEventHandler = gameEventHandler;
        this.player = gameEventHandler.player;
        this.questTracker = questTracker;
        createBook();
    }

    // initiate Level Cotton Candy Land
    public Level cottonCandyLand = 
    new Level("Cotton Candy Land", player);

    LevelMap candyLandMap = new LevelMap("candyLandMap", "images/candyLandMap.png");

    Chapter sweetopolisCityGateSouth = new Chapter(player, "Sweetopolis Citygate South", cottonCandyLand);
    Chapter cityOutskirtSouth = new Chapter(player,"City Outskirt South", cottonCandyLand);
    Chapter cityOutskirtSouthEast = new Chapter(player, "City Outskirt South East", cottonCandyLand);
    Chapter cityOutskirtSouthWest = new Chapter(player,"City Outskirt South West", cottonCandyLand);
    Chapter cityOutskirtEast = new Chapter(player,"City Outskirt East", cottonCandyLand);
    Chapter willowTreeForest = new Chapter(player,"Willow Tree Forest", cottonCandyLand);
    Chapter cityOutskirtWest = new Chapter(player,"City Outskirt West", cottonCandyLand);
    Chapter sweetopolisEntrySouth = new Chapter(player,"Sweetopolis Entry South", cottonCandyLand);

    Item fish = new Item("fish");
    // sword is now created dynamically in QuestLog.java during quest completion
    Item horsemask = new Item("horsemask");

    NPC guard = new NPC("guard", 20, 5, 8);
    NPC manWithHorseMask = new NPC("man", 10, 0, 4);
    NPC frog = new NPC("froggy", 5, 0, 0);
    NPC fox = new NPC("fox", 10, 0, 0);

    public void createBook(){

        cottonCandyLand.addChaptersToList(Arrays.asList(sweetopolisCityGateSouth, cityOutskirtSouth, cityOutskirtSouthEast, cityOutskirtSouthWest, cityOutskirtEast, willowTreeForest));
        questTracker.setCurrentLevel(cottonCandyLand);
        cottonCandyLand.levelMap = candyLandMap;
        cityOutskirtSouth.setMapReward(cottonCandyLand);

        cottonCandyLand.initialChapter = cityOutskirtSouth;
        cityOutskirtSouth.addNextChapters(sweetopolisCityGateSouth, cityOutskirtSouthEast, null, cityOutskirtSouthWest);
        sweetopolisCityGateSouth.addNextChapters(null, null, cityOutskirtSouth, null);
        cityOutskirtSouthEast.addNextChapters(cityOutskirtEast, null, null, cityOutskirtSouth);
        cityOutskirtSouthWest.addNextChapters(cityOutskirtWest, cityOutskirtSouth, null, null);
        cityOutskirtWest.addNextChapters(null, null, cityOutskirtSouthWest, null);
        cityOutskirtEast.addNextChapters(null, willowTreeForest, cityOutskirtSouthEast, null);
        willowTreeForest.addNextChapters(null, null, null, cityOutskirtEast);

        /********************************************
        *             CityGateSouth                 *
        ********************************************/

        sweetopolisCityGateSouth.addStoryText("The gates stand toweringly high, nearly 30 feet of polished candy glass shimmering with intricate candy craftsmanship.");
        sweetopolisCityGateSouth.addStoryText("The translucent material is arched and elegantly ornate with golden caramel veins, resembling the gateway to a palace built for confectionery royalty.");
        sweetopolisCityGateSouth.addStoryText("The design seamlessly balances beauty and an eerie, almost unnatural perfection...");
        sweetopolisCityGateSouth.addStoryText("A guard is stationed at the gates. He is standing upright, walking on two hooves and has the head of a horse.");

        sweetopolisCityGateSouth.addNPCsToChapter(new ArrayList<NPC>(Arrays.asList(guard)));
        gameEventHandler.dialogueManager.addBasicDialogue(guard, new ArrayList<>(Arrays.asList("NO HUMANS ALLOWED!", "get away, pesky human!", "don't bother me.")));
        gameEventHandler.dialogueManager.addInventoryDialogue(guard, Map.of(horsemask, "Hello fellow horseman! You may pass."));
        gameEventHandler.dialogueManager.addInventoryDialogue(guard, Map.of(fish, "you stink!"));


        /********************************************
        *           CityOutskirtSouthEast           *
        ********************************************/

        cityOutskirtSouth.addItem(fish);
        fish.addItemDescription("You slipped it right into your pocket. It stinks.");
        fish.onGround = true;

        fox.setKeyItems(new ArrayList<>(Arrays.asList(fish)));
        fish.questItem = true;

        guard.setKeyItems(new ArrayList<>(Arrays.asList(horsemask)));
        horsemask.addItemDescription("You couldn't wait and put it on immediately. It smells like bad breath in here ...");
        
        // Note: sword is created during quest completion, not placed in world
        // willowTreeForest.addItem(sword); // REMOVED - sword only appears through quest
        // sword.addItemDescription moved to QuestLog.java where sword is created

        cityOutskirtSouth.addStoryText("You have come into existence without knowing of what was before, what is, or what is going to be.");
        cityOutskirtSouth.addStoryText("You find yourself close to a big city enclosed by walls. It lays north from here.");

        cityOutskirtSouthEast.addStoryText("Here, delicate strands of twisted cotton candy swirl and dance through the air, carried by a soft, whimsical breeze.");
        cityOutskirtSouthEast.addStoryText("It seems the wind is ferrying them in from the southeast.");
        cityOutskirtSouthEast.addNPCsToChapter(new ArrayList<NPC>(Arrays.asList(fox)));

        gameEventHandler.dialogueManager.addBasicDialogue(fox, new ArrayList<>(Arrays.asList("It scurries away into the bushes when you try to get closer.", "You see its eyes gleaming within the dark bushes.")));
        gameEventHandler.dialogueManager.addInventoryDialogue(fox, Map.of(fish, "Hmm, you reek deliciously."));

        cityOutskirtEast.addStoryText("A flurry of cotton candy strands dance through the air.");
        cityOutskirtEast.addStoryText("It seems the wind carries them from the forest stretching eastward.");

        willowTreeForest.addStoryText("As you progress thick willow trees rise from the ground made of red licorice bark.");
        willowTreeForest.addStoryText("From the licorice branches hang heavy thick strands of pink cotton candy braids.");
        willowTreeForest.addStoryText("The air is thickened with the swirling strands laying over the forest like a fog.");

        cityOutskirtSouthWest.addStoryText("From here you can get a good view of the city walls in the distance.");
        cityOutskirtSouthWest.addStoryText("Flanking the gates, they are made of hardened nougat and reinforced with chocolate bricks.");
        cityOutskirtSouthWest.addStoryText("The textures are rich and irregular, giving them the appearance of an impenetrable fortress, albeit a sweet one.");

        cityOutskirtWest.addNPCsToChapter(new ArrayList<NPC>(Arrays.asList(manWithHorseMask)));
        manWithHorseMask.isHostile = true;
        manWithHorseMask.addItemDrop(horsemask);
        gameEventHandler.dialogueManager.addBasicDialogue(manWithHorseMask, new ArrayList<>(Arrays.asList("He glares at you through the eye holes of his horse mask.", "He doesn't seem friendly ...")));
        cityOutskirtWest.addStoryText("------------------------------------------");
        cityOutskirtWest.addStoryText("The cotton candy strands in the air seem less dense here. The blue sky is clearly visible above you.");
        cityOutskirtWest.addStoryText("Swaying in the breeze, candy flowers made of sugar glass shimmer in the sunlight, climpering softly when touching each other.");
        cityOutskirtWest.addStoryText("Within the bright specs of red, yellow and violet, stands a ... naked man with a horse mask. Menacing.");
        cityOutskirtWest.addStoryText("------------------------------------------");
        
    }
}