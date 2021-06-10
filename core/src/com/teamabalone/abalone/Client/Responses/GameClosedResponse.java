package com.teamabalone.abalone.Client.Responses;

import java.util.UUID;

public class GameClosedResponse extends BaseResponse{
    protected String message;
    protected UUID closedBy;

    public String getMessage(){
        return this.message;
    }
    public UUID getClosedBy(){
        return this.closedBy;
    }
}
