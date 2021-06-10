package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class SurrenderRequest extends BaseRequest {
    public SurrenderRequest(UUID userId){
        this.commandCode = RequestCommandCodes.SURRENDER.getValue();
        this.userId = userId;
    }
}
