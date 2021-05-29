package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public interface GameInfos {

    boolean singleDeviceMode();
    int numberPlayers();
    int mapSize();
    int maximalSelectableMarbles();
    int playerId();
    ArrayList<String> names();
}
