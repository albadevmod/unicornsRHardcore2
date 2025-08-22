package com.textadventure;
import java.util.ArrayList;

public class Arena{

    // activeChapter --> NPCList --> chooseNPC ---> activeNPC

    Player player;
    ArrayList <NPC> chapterNPCList;
    Chapter activeChapter;

    public Arena (Player player){
        this.player = player;
    }

}