package com.teamabalone.abalone.Client.Responses;

import com.teamabalone.abalone.Gamelogic.Directions;
/*
MadeMoveResponse occurs, when a player's move is broadcast to other players.
marbles contains the moving marbles' ids.
direction contains the Directions(enum) where the marbles will move
 */
public class MadeMoveResponse extends BaseResponse{
    protected int [] ids;
    protected Directions direction;
    protected int renegadeId = 99;
    protected boolean secondTurn;

    public int [] getMarbles(){
        return this.ids;
    }
    public Directions getDirection(){
        return this.direction;
    }
    public int getRenegadeId(){ return this.renegadeId;}
    public boolean getSecondTurn(){ return this.secondTurn;}
}
