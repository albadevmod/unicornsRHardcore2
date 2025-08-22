package com.textadventure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Book {

    Player player;
    DialogueManager dialogueManager;
    QuestTracker questTracker;

    public Book(Player player, QuestTracker questTracker, DialogueManager dialogueManager){
        this.player = player;
        this.questTracker = questTracker;
        this.dialogueManager = dialogueManager;
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

    Item fish = new Item("fish", true);

    NPC guard = new NPC("guard", 20, 5);
    NPC frog = new NPC("Froggy", 5, 5);
    NPC fox = new NPC("fox", 10, 40);

    public void createBook(){

        cottonCandyLand.addChaptersToList(Arrays.asList(sweetopolisCityGateSouth, cityOutskirtSouth, cityOutskirtSouthEast, cityOutskirtSouthWest, cityOutskirtEast, willowTreeForest));
        questTracker.setFirstLevel(cottonCandyLand);
        cottonCandyLand.levelMap = candyLandMap;
        cityOutskirtSouth.setMapReward(cottonCandyLand);

        cottonCandyLand.initialChapter = cityOutskirtSouth;
        cityOutskirtSouth.addNextChapters(sweetopolisCityGateSouth, cityOutskirtSouthEast, null, cityOutskirtSouthWest);
        sweetopolisCityGateSouth.addNextChapters(null, null, cityOutskirtSouth, null);
        cityOutskirtSouthEast.addNextChapters(cityOutskirtEast, null, null, cityOutskirtSouth);
        cityOutskirtSouthWest.addNextChapters(null, cityOutskirtSouth, null, null);
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
        dialogueManager.addBasicDialogue(guard, new ArrayList<>(Arrays.asList("NO HUMANS ALLOWED!", "get away, pescy human!", "don't bother me.")));
        dialogueManager.inventoryDialogues.put(guard, Map.of(fish, "you stink!"));


        /********************************************
        *           CityOutskirtSouthEast           *
        ********************************************/

        cityOutskirtSouth.addItem(fish); 
        fish.addItemDescription("You slipped it right into your pocket. It stinks.");

        cityOutskirtSouth.addStoryText("You have come into existence without knowing of what was before, what is, or what is going to be.");
        cityOutskirtSouth.addStoryText("You find yourself close to a big city enclosed by walls. It lays north from here.");

        cityOutskirtSouthEast.addStoryText("Here, delicate strands of twisted cotton candy swirl and dance through the air, carried by a soft, whimsical breeze.");
        cityOutskirtSouthEast.addStoryText("It seems the wind is ferrying them in from the southeast.");
        cityOutskirtSouthEast.addStoryText("You can see a fox peeking through the bushes.");
        cityOutskirtSouthEast.addNPCsToChapter(new ArrayList<NPC>(Arrays.asList(fox)));
        dialogueManager.addBasicDialogue(fox, new ArrayList<>(Arrays.asList("It scurries away into the bushes when you try to get closer.", "You see its eyes gleaming within the dark bushes.")));
        dialogueManager.inventoryDialogues.put(fox, Map.of(fish, "Hmm, you reek deliciously."));

        cityOutskirtEast.addStoryText("A flurry of cotton candy strands dance through the air.");
        cityOutskirtEast.addStoryText("It seems the wind carries them from the forest stretching eastward.");

        willowTreeForest.addStoryText("As you progress thick willow trees rise from the ground made of red licorice bark.");
        willowTreeForest.addStoryText("From the licorice branches hang heavy thick strands of pink cotton candy braids.");
        willowTreeForest.addStoryText("The air is thickened with the swirling strands laying over the forest like a fog.");

        cityOutskirtSouthWest.addStoryText("From here you can get a good view of the city walls in the distance.");
        cityOutskirtSouthWest.addStoryText("Flanking the gates, they are made of hardened nougat and reinforced with chocolate bricks.");
        cityOutskirtSouthWest.addStoryText("The textures are rich and irregular, giving them the appearance of an impenetrable fortress, albeit a sweet one.");

        questTracker.setupAllQuests();

        
    }
}