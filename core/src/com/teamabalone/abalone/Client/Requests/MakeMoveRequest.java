package com.teamabalone.abalone.Client.Requests;

import com.teamabalone.abalone.Gamelogic.Directions;

import java.util.UUID;

public class MakeMoveRequest extends BaseRequest{
    protected int [] marbles;
    protected Directions direction;
    public MakeMoveRequest(UUID userId, int [] marbles, Directions direction){
        this.userId = userId;
        this.commandCode = 10;
        this.marbles = marbles;
        this.direction = direction;
    }

}
