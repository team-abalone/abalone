package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

/**
 * This class holds all relevant infomation about the current game.
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
    private int numberPlayers = 2;
    private int mapSize = 5;
    private int maximalSelectableMarbles = 3;
    private int playerId = 0;
    private ArrayList<String> names;
    private GameStartPositions startPosition = GameStartPositions.DEFAULT;

    private GameInfo() {
        //private constructor
        names = new ArrayList<>();
        names.add("White");
        names.add("Black");
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
    public boolean getSingleDeviceMode() {
        return singleDeviceMode;
    }

    @Override
    public int getNumberPlayers() {
        return numberPlayers;
    }

    @Override
    public int getMapSize() {
        return mapSize;
    }

    @Override
    public int getMaximalSelectableMarbles() {
        return maximalSelectableMarbles;
    }

    @Override
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

    /**
     * Sets if the current game mode is single or multiplayer
     *
     * @param singleDeviceMode  the boolean for the mode
     */
    public void setSingleDeviceMode(boolean singleDeviceMode) {
        this.singleDeviceMode = singleDeviceMode;
    }

    /**
     * sets the number of players
     *
     * @param numberPlayers  the number of players
     */
    public void setNumberPlayers(int numberPlayers) {
        this.numberPlayers = numberPlayers;
    }

    /**
     * Sets the current size for the Field.
     *
     * @param mapSize  the side length of the pentagon
     */
    public void setMapSize(int mapSize) {
        this.mapSize = mapSize;
    }

    /**
     * Sets the max amount a player can select in one turn
     *
     * @param maximalSelectableMarbles  the amount of selectable marbles
     */
    public void setMaximalSelectableMarbles(int maximalSelectableMarbles) {
        this.maximalSelectableMarbles = maximalSelectableMarbles;
    }

    /**
     * Sets the players ID
     *
     * @param playerId  the id of the current player
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Sets the name of all players
     *
     * @param names  an array of all players
     */
    public void setNames(ArrayList<String> names) {
        this.names = names;
    }

    /**
     * Sets the starting position for the marbles
     *
     * @param startPosition  the position to start with
     */
    public void setStartPosition(GameStartPositions startPosition) {
        this.startPosition = startPosition;
    }

    /**
     * Gets the position to start with
     *
     * @return  the position to start with
     */
    public GameStartPositions getStartPosition() {
        return startPosition;
    }

}
