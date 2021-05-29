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
    public static GameInfo singletonInstance;

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
