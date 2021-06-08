package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class CloseGameRequest extends BaseRequest{
    public CloseGameRequest(UUID userId){
        this.commandCode = RequestCommandCodes.CLOSE_GAME.getValue();
        this.userId = userId;
    }
}
