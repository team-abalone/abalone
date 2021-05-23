package com.teamabalone.abalone.Client.Responses;

import com.teamabalone.abalone.Gamelogic.Directions;
/*
MadeMoveResponse occurs, when a player's move is broadcast to other players.
marbles contains the moving marbles' ids.
direction contains the Directions(enum) where the marbles will move
 */
public class MadeMoveResponse extends BaseResponse{
    protected int [] marbles;
    protected Directions direction;
    //TODO: Remove or adapt - unknown if this will be of need
    @Override
    public String toString() {
        return "MadeMoveResponse{" +
                "Direction='" + direction + '\'' +
                '}';
    }
    public int [] getMarbles(){
        return this.marbles;
    }
    public Directions getDirection(){
        return this.direction;
    }
}
