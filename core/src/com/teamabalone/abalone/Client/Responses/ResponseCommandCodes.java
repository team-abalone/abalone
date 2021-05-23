package com.teamabalone.abalone.Client.Responses;

public enum ResponseCommandCodes {
    ID_INITIALIZED(10),
    GAME_STARTED(20),
    ROOM_JOINED(30),
    ROOM_CREATED(40),
    ROOM_CLOSED(50),
    ROOM_JOINED_OTHER(60),
    MADE_MOVE(70);

    private final int value;

    private ResponseCommandCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
