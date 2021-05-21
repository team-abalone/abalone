package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class CreateRoomRequest extends BaseRequest {
    private int numberOfPlayers;

    public CreateRoomRequest(UUID userId, int numberOfPlayers) {
        this.commandCode = RequestCommandCodes.CREATE_ROOM.getValue();
        this.numberOfPlayers = numberOfPlayers;
        this.userId = userId;
    }
}
