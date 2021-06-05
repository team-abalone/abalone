package com.teamabalone.abalone.Gamelogic;

public enum LocalGameStartPositions {
    DEFAULT,
    GERMAN_DAISY,
    SNAKES,
    THE_WALL;

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
            case SNAKES:
                return new int[][]{
                        {1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 0, 0},
                        {1, 0, 0, 0, 0, 0, 0},
                        {1, 0, 0, 1, 1, 2, 2, 0},
                        {0, 1, 0, 1, 0, 2, 0, 2, 0},
                        {0, 1, 1, 2, 2, 0, 0, 2},
                        {0, 0, 0, 0, 0, 0, 2},
                        {0, 0, 0, 0, 0, 2},
                        {2, 2, 2, 2, 2}
                };
            case THE_WALL:
                return new int[][]{
                        {0, 0, 2, 0, 0},
                        {0, 0, 0, 0, 0, 0},
                        {0, 2, 2, 2, 2, 2, 0},
                        {2, 2, 2, 2, 2, 2, 2, 2},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {1, 1, 1, 1, 1, 1, 1, 1},
                        {0, 1, 1, 1, 1, 1, 0},
                        {0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0}
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
