package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public interface GameInfos {

    boolean getSingleDeviceMode();
    int getNumberPlayers();
    int getMapSize();
    int getMaximalSelectableMarbles();
    int getPlayerId();
    ArrayList<String> names();
}
