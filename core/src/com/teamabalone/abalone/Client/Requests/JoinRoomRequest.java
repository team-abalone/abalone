package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

/**
 * Can either be used for joining, closing room or starting game.
 */
public class JoinRoomRequest extends BaseRequest {
    private String roomKey;

    public JoinRoomRequest(UUID userId, String roomKey) {
        this.userId = userId;
        this.commandCode = RequestCommandCodes.JOIN_ROOM.getValue();
        this.roomKey = roomKey;
    }
}
