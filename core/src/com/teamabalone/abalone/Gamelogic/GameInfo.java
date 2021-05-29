package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public class GameInfo implements GameInfos {
    private static GameInfo singletonInstance;
    private boolean singleDeviceMode = false;
    private int numberPlayers;
    private int mapSize = 5;
    private int maximalSelectableMarbles = 3;
    private int playerId;
    private ArrayList<String> names;

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
        return singleDeviceMode;
    }

    @Override
    public int numberPlayers() {
        return numberPlayers;
    }

    @Override
    public int mapSize() {
        return mapSize;
    }

    @Override
    public int maximalSelectableMarbles() {
        return maximalSelectableMarbles;
    }

    @Override
    public int playerId() {
        return playerId;
    }

    @Override
    public ArrayList<String> names() {
        return names;
    }

    public void setSingleDeviceMode(boolean singleDeviceMode) {
        this.singleDeviceMode = singleDeviceMode;
    }

    public void setNumberPlayers(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    public void setMaximalSelectableMarbles(int maximalSelectableMarbles) {
        this.maximalSelectableMarbles = maximalSelectableMarbles;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setNames(ArrayList<String> names) {
        this.names = names;
    }
}
