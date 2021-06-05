package com.teamabalone.abalone.Gamelogic;

public enum GameStartPositions {
    DEFAULT,
    GERMAN_DAISY,
    SNAKES,
    THE_WALL;

    public static int[][] getStartPosition() {
        return getPositionArray(GameInfo.getInstance().getStartPosition());
    }

    private static int[][] getPositionArray(GameStartPositions startPositions) {
        switch (startPositions) {
            case GERMAN_DAISY:
                return new int[][]{
                        {0, 0, 0, 0, 0},
                        {2, 2, 0, 0, 1, 1},
                        {2, 2, 2, 0, 1, 1, 1},
                        {0, 2, 2, 0, 0, 1, 1, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 0, 0, 2, 2, 0},
                        {1, 1, 1, 0, 2, 2, 2},
                        {1, 1, 0, 0, 2, 2},
                        {0, 0, 0, 0, 0}
                };
            case SNAKES:
                return new int[][]{
                        {2, 2, 2, 2, 2},
                        {2, 0, 0, 0, 0, 0},
                        {2, 0, 0, 0, 0, 0, 0},
                        {2, 0, 0, 2, 2, 1, 1, 0},
                        {0, 2, 0, 2, 0, 1, 0, 1, 0},
                        {0, 2, 2, 1, 1, 0, 0, 1},
                        {0, 0, 0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 0, 1},
                        {1, 1, 1, 1, 1}
                };
            case THE_WALL:
                return new int[][]{
                        {0, 0, 1, 0, 0},
                        {0, 0, 0, 0, 0, 0},
                        {0, 1, 1, 1, 1, 1, 0},
                        {1, 1, 1, 1, 1, 1, 1, 1},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {2, 2, 2, 2, 2, 2, 2, 2},
                        {0, 2, 2, 2, 2, 2, 0},
                        {0, 0, 0, 0, 0, 0},
                        {0, 0, 2, 0, 0}
                };
            case DEFAULT:
            default:
                return new int[][]{
                        {2, 2, 2, 2, 2},
                        {2, 2, 2, 2, 2, 2},
                        {0, 0, 2, 2, 2, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 1, 1, 1, 0, 0},
                        {1, 1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1}
                };
        }
    }
}
