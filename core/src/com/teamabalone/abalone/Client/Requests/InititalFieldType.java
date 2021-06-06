package com.teamabalone.abalone.Client.Requests;

public enum InititalFieldType {
    DEFAULT(20),
    GERMANY_DAISY(10);

    private final int value;

    private InititalFieldType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
