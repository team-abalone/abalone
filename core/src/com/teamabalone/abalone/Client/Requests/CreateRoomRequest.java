package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class CreateRoomRequest extends BaseRequest {
    private int NumberOfPlayers;

    public CreateRoomRequest(UUID userId, int numberOfPlayers) {
        CommandCode = RequestCommandCodes.CREATE_ROOM.getValue();
        NumberOfPlayers = numberOfPlayers;
        UserId = userId;
    }
}
