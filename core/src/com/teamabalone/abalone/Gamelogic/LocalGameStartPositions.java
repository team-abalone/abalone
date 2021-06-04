package com.teamabalone.abalone.Gamelogic;

public enum LocalGameStartPositions {
    DEFAULT(0),
    GERMAN_DAISY(1);

    private final int value;

    private LocalGameStartPositions(int value){
        this.value=value;
    }
    public int getValue(){
        return this.value;
    }

    public int[][] getPositions(LocalGameStartPositions type){

        if(type.getValue() == 0){
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

        else if(type.getValue() == 1){
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
        }
        return null;
    }
}
