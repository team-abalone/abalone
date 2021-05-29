package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class StartGameRequest extends BaseRequest {
    private String roomKey;

    public StartGameRequest(UUID userId, String roomKey) {
        this.userId = userId;
        this.commandCode = RequestCommandCodes.START_GAME.getValue();
        this.roomKey = roomKey;
    }
}
