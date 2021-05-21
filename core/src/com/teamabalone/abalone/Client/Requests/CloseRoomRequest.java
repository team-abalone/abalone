package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class CloseRoomRequest extends BaseRequest {
    private String roomKey;

    public CloseRoomRequest(UUID userId, String roomKey) {
        this.commandCode = RequestCommandCodes.CLOSE_ROOM.getValue();
        this.roomKey = roomKey;
        this.userId = userId;
    }
}
