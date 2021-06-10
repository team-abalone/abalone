package com.teamabalone.abalone.Client.Requests;

public enum InititalFieldType {
    DEFAULT(20),
    GERMAN_DAISY(10),
    THE_WALL(30),
    SNAKE(40)
    ;

    private final int value;

    private InititalFieldType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
