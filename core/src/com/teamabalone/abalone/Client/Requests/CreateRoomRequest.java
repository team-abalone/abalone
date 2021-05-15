package com.teamabalone.abalone.Client.Requests;

import java.util.UUID;

public class CreateRoomRequest extends BaseRequest {
    private int NumberOfPlayers;

    public CreateRoomRequest(UUID userId, int numberOfPlayers) {
        NumberOfPlayers = numberOfPlayers;
        UserId = userId;
    }
}
