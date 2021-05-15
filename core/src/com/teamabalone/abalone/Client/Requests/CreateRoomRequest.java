package com.teamabalone.abalone.Client.Requests;

public class CreateRoomRequest extends BaseRequest {
    private int NumberOfPlayers;

    public CreateRoomRequest(int numberOfPlayers) {
        NumberOfPlayers = numberOfPlayers;
    }
}
