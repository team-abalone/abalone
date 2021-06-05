package com.teamabalone.abalone.Gamelogic;

public enum LocalGameStartPositions {
    DEFAULT(0),
    GERMAN_DAISY(1);

    private final int value;

    LocalGameStartPositions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static int[][] getPositions(LocalGameStartPositions startPositions) {
        switch (startPositions) {
            case GERMAN_DAISY:
                return new int[][]{
                        {0, 0, 0, 0, 0},
                        {1, 1, 0, 0, 2, 2},
                        {1, 1, 1, 0, 2, 2, 2},
                        {0, 1, 1, 0, 0, 2, 2, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 2, 2, 0, 0, 1, 1, 0},
                        {2, 2, 2, 0, 1, 1, 1},
                        {2, 2, 0, 0, 1, 1},
                        {0, 0, 0, 0, 0}
                };
            default:
                return new int[][]{
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1, 1},
                        {0, 0, 1, 1, 1, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 2, 2, 2, 0, 0},
                        {2, 2, 2, 2, 2, 2},
                        {2, 2, 2, 2, 2}
                };
        }
    }
}
