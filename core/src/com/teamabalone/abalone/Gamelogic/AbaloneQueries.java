package com.teamabalone.abalone.Gamelogic;

public interface AbaloneQueries {

    boolean isInLine(int[] ids);

    boolean isPushedOutOfBound();

    int[] checkMove(int[] ids, Directions direction);

    int[] getWholeField();

    void changeTo(int tileId, int playerId);

    int idOfCurrentRenegade();
}
