package com.teamabalone.abalone.Client.Responses;

public enum ResponseCommandCodes {
    ID_INITIALIZED(10),
    GAME_STARTED(20),
    ROOM_JOINED(30),
    ROOM_CREATED(40),
    ROOM_CLOSED(50),
    ROOM_JOINED_OTHER(60),
    MADE_MOVE(70),
    GAME_CLOSED(80),
    SURRENDERED(90),
    SERVER_EXCEPTION(100),
    GAME_EXCEPTION(200),
    CHAT_EXCEPTION(300),
    ROOM_EXCEPTION(400),
    LEFT_ROOM(100),
    OTHER_PLAYER_LEFT(101),
    NO_ROOM_TO_LEAVE(102)
    ;
    private final int value;

    private ResponseCommandCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
