package com.teamabalone.abalone.Gamelogic;

/**
 * The enums for Directions.
 * <p></p>
 * This class holds all directions available for the user.
 * It can be:
 * <li>RIGHT
 * <li>RIGHTUP
 * <li>RIGHTDOWN
 * <li>LEFT
 * <li>LEFTUP
 * <li>LEFTDOWN
 * <li>NOTSET
 */
public enum Directions {
    RIGHT, RIGHTUP, LEFTUP, LEFT, LEFTDOWN, RIGHTDOWN, NOTSET;

    /**
     * Takes the swipe input and returns a direction.
     *
     * @param sensitivity the swipe sensivity
     * @param startX      the x-coordinate at pointer down
     * @param startY      the y-coordinate at pointer down
     * @param endX        the x-coordinate at pointer up
     * @param endY        the y-coordinate at pointer down
     * @return the most fitting direction
     */
    public static Directions calculateDirection(int sensitivity, float startX, float startY, float endX, float endY) {
        float adjacentLeg = endX - startX;
        float oppositeLeg = startY - endY; //screen coordinates: (0,0) left UPPER corner

        if (Math.abs(adjacentLeg) < sensitivity && Math.abs(oppositeLeg) < sensitivity) {
            return NOTSET;
        }
        return getDirection(adjacentLeg, oppositeLeg);
    }

   /* public static Directions calculateDirectionFromAcceleration(float adjacentLeg, float oppositeLeg) {
        return getDirection(adjacentLeg, oppositeLeg);
    }
*/
    /**
     * Calculates the direction according to the tilt sensor
     *
     * @param adjacentLeg   the x-coordinate of the tilt location
     * @param oppositeLeg  the y-coordinate of the tilt location
     * @return  the most fitting direction
     */
    public static Directions getDirection(float adjacentLeg, float oppositeLeg) {

        //to get 360°
        boolean olneg = oppositeLeg < 0;
        boolean alneg = adjacentLeg < 0;
        double offset = 0;
        if (alneg) {
            offset = 180;
        }
        if (!alneg && olneg) {
            offset = 360;
        }

        double degrees = Math.toDegrees(Math.atan(oppositeLeg / adjacentLeg)) + offset; //atan() returns only -pi/2 - pi/2 (circle half)
        int index = ((int) ((degrees) / 30)); //get sector

        switch (index) {
            case 0:
            case 11:
                return RIGHT;
            case 1:
            case 2:
                return RIGHTUP;
            case 3:
            case 4:
                return LEFTUP;
            case 5:
            case 6:
                return LEFT;
            case 7:
            case 8:
                return LEFTDOWN;
            case 9:
            case 10:
                return RIGHTDOWN;
            default:
                return NOTSET;
        }
    }

    /**
     * Turns the Direction 180° around.
     *
     * @param direction the direction
     * @return the turned direction
     */
    public static Directions mirrorDirection(Directions direction) {
        switch (direction) {
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            case LEFTUP:
                return RIGHTDOWN;
            case RIGHTUP:
                return LEFTDOWN;
            case LEFTDOWN:
                return RIGHTUP;
            case RIGHTDOWN:
                return LEFTUP;
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }
}
