package com.teamabalone.abalone.Client.Requests;

public enum InititalFieldType {
    GERMANY_DAISY(10),
    DEFAULT(20);

    private final int value;

    private InititalFieldType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
