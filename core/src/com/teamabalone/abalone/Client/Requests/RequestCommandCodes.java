package com.teamabalone.abalone.Client.Requests;

/**
 * Enum representing the Commands, that can be sent to the server.
 */
public enum RequestCommandCodes {
    //MAKE_MOVE(10),
    CREATE_ROOM(20),
    JOIN_ROOM(30),
    CLOSE_ROOM(40),
    SEND_CHAT_MESSAGE(50),
    GET_USER_ID(60),
    START_GAME(70),
    MAKE_MOVE(80);

    private final int value;

    private RequestCommandCodes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
