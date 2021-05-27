package com.teamabalone.abalone.Client;

/**
 * This will build a FieldMap which we compare before every move we broadcast via the api
 * The host will build the FieldMap - the other players receive it via api at the beginning of the game
 * The reason for this is to make sure, our board will always be the same throughout all devices
 */
public class FieldMapEntry {

    public int player;
    public int id;
    public int xCoordinate;
    public int yCoordinate;
    public int zCoordinate;

    public FieldMapEntry(int player, int id, int xCoordinate, int yCoordinate, int zCoordinate){
        this.player = player;
        this.id = id;
        this.xCoordinate=xCoordinate;
        this.yCoordinate=yCoordinate;
        this.zCoordinate=zCoordinate;
    }

}
