package com.teamabalone.abalone.Client.Responses;

public enum ResponseCommandCodes {
    ID_INITIALIZED(10),
    GAME_STARTED(20),
    ROOM_JOINED(30),
    ROOM_CREATED(40),
    ROOM_CLOSED(50),
    ROOM_JOINED_OTHER(60),
    MADE_MOVE(70),
    SERVER_EXCEPTION(100),
    GAME_EXCEPTION(200),
    CHAT_EXCEPTION(300),
    ROOM_EXCEPTION(400);


    private final int value;

    private ResponseCommandCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
