package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public class Game implements GameInfos{

    @Override
    public boolean singleDeviceMode() {
        return true;
    }

    @Override
    public int numberPlayers() {
        return 2;
    }

    @Override
    public int mapSize() {
        return 5;
    }

    @Override
    public int maximalSelectableMarbles() {
        return 3;
    }

    @Override
    public ArrayList<String> colors() {
        return null;
    }

    @Override
    public ArrayList<String> names() {
        return null;
    }
}
