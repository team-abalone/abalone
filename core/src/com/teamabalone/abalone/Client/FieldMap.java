package com.teamabalone.abalone.Client;

import java.util.InvalidPropertiesFormatException;

public class FieldMap {
    private FieldMapEntry marbles [];
    private static FieldMap FieldMap;
    private FieldMap(int numberOfMarbles){
        this.marbles = new FieldMapEntry[numberOfMarbles];
    }

     public static FieldMap newInstance(int numberOfPlayers) {
        int numberOfMarbles = numberOfPlayers * 14;
        FieldMap= new FieldMap(numberOfMarbles);
        return FieldMap;
    }
    public void setMarbles(FieldMapEntry [] marbles) /*throws Exception*/{
        if(marbles.length != this.marbles.length){
            //TODO:Throw exception
        }
        for (int i = 0; i < marbles.length; i++) {
            this.marbles[i] = marbles[i];
        }
    }
    public FieldMapEntry[] getMarbles(){
        //marbles array will be part of our Request - this will contain all marbles, their players, position and id
        return this.marbles;
    }
    public void moveMarble(FieldMapEntry [] marble){
        for (int i = 0; i < marbles.length; i++) {
            for (int j = 0; j <this.marbles.length ; j++) {
                if(this.marbles[j].id == marble[i].id){
                    this.marbles[j] = marble[i];
                }
            }
        }
    }
    public void removeMarble(FieldMapEntry marble){

    }

}
