package com.teamabalone.abalone.Gamelogic;

import java.util.ArrayList;

public interface AbaloneQueries {

    boolean isInLine(int[] ids);

    boolean isPushedOutOfBound();

    int[] checkMove(int[] ids, Directions direction);

    int[] getWholeField();
}
