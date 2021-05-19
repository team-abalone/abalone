package com.teamabalone.abalone.Gamelogic;

public enum Directions {
    RIGHT, RIGHTUP, LEFTUP, LEFT, LEFTDOWN, RIGHTDOWN, NOTSET;

    public static Directions calculateDirection(int sensitivity, float startX, float startY, float endX, float endY) {
        float adjacentLeg = endX - startX;
        float oppositeLeg = startY - endY; //screen coordinates: (0,0) left UPPER corner

        if (Math.abs(adjacentLeg) < sensitivity && Math.abs(oppositeLeg) < sensitivity) {
            return NOTSET;
        }

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
        Directions direction;

        switch (index) {
            case 0:
            case 11:
                direction = RIGHT;
                break;
            case 1:
            case 2:
                direction = RIGHTUP;
                break;
            case 3:
            case 4:
                direction = LEFTUP;
                break;
            case 5:
            case 6:
                direction = LEFT;
                break;
            case 7:
            case 8:
                direction = LEFTDOWN;
                break;
            case 9:
            case 10:
                direction = RIGHTDOWN;
                break;
            default:
                direction = NOTSET;
        }

        return direction;
    }
}
