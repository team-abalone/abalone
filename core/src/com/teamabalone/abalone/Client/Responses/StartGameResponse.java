package com.teamabalone.abalone.Client.Responses;

public class StartGameResponse extends BaseResponse{
    protected int [][] gameField;
    protected String [] players;
    protected String [] userIds;

    public int [][] getGameField(){
        return this.gameField;
    }
    public String [] getPlayers(){
        return this.players;
    }
    public String [] getUserIds(){
        return this.userIds;
    }

}
