package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class CreateRoomRequest extends BaseRequest {
    private final int gameFieldType;
    private int numberOfPlayers;
    private String userName;

    public CreateRoomRequest(UUID userId, int numberOfPlayers, String userName, InititalFieldType ift) {
        this.commandCode = RequestCommandCodes.CREATE_ROOM.getValue();
        this.gameFieldType = ift.getValue();
        this.numberOfPlayers = numberOfPlayers;
        this.userId = userId;
        this.userName = userName;
    }
}
