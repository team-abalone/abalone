package com.teamabalone.abalone.Client.Requests;

import com.teamabalone.abalone.Gamelogic.Directions;

import java.util.UUID;

public class MakeMoveRequest extends BaseRequest{
    protected int [] marbles;
    protected Directions direction;
    protected int renegadeId;
    protected boolean secondTurn;

    public MakeMoveRequest(UUID userId, int [] marbles, Directions direction, int renegadeId, boolean secondTurn){
        this.userId = userId;
        this.commandCode = 10;
        this.marbles = marbles;
        this.direction = direction;
        this.renegadeId=renegadeId;
        this.secondTurn=secondTurn;
    }
}
