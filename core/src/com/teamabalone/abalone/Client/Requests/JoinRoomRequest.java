package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

/**
 * Can either be used for joining, closing room or starting game.
 */
public class JoinRoomRequest extends BaseRequest {
    private String RoomKey;

    public JoinRoomRequest(UUID userId, String roomKey) {
        CommandCode = RequestCommandCodes.JOIN_ROOM.getValue();
        RoomKey = roomKey;
    }
}
