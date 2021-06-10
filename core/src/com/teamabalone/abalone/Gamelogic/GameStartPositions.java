package com.teamabalone.abalone.Gamelogic;

/**
 * Enum that Describes the starting position a game can have.
 *
 * This is according to the official game rules.
 * https://abaloneonline.wordpress.com/tag/starting-positions/
 */
public enum GameStartPositions {
    DEFAULT,
    GERMAN_DAISY,
    SNAKES,
    THE_WALL;

    /**
     * Returns an 2D array of the lineup of every marble in game, according to the server
     *
     * @return  2D array of the lineup
     */
    public static int[][] getStartPosition() {
        return getPositionArray(GameInfo.getInstance().getStartPosition());
    }

    /**
     * Returns an 2D array of the lineup of every marble in game
     *
     * @param startPositions  a starting position according to this enum
     * @return  2D array of the lineup
     */
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
