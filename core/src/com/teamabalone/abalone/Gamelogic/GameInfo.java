package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public class GameInfo implements GameInfos {
    public static GameInfo singletonInstance;

    private GameInfo() {
        //private constructor
    }

    public static GameInfo getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new GameInfo();
        }
        return singletonInstance;
    }

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
