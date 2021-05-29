package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

/**
 *This class holds all relevant infomation about the current game.
 * <p></p>
 * Following information will be tracked:
 * <li> Single/Multiplayer as boolean
 * <li> number of players as int
 * <li> the size of the game map as int
 * <li> maximum of marbles selectable at once as int
 * <li> skins for the marbles
 * <li> player names
 */
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

    /**
     * Singleton Constructor for this GameInfo.
     *
     * @return this GameInfo, new if null
     */
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
