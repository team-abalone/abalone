package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public interface GameInfos {

    boolean singleDeviceMode();
    int numberPlayers();
    int mapSize();
    int maximalSelectableMarbles();
    ArrayList<String> colors(); //skin or color; probably parsing necessary
    ArrayList<String> names();
}
