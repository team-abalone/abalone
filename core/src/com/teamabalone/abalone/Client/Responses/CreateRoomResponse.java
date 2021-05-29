package com.teamabalone.abalone.Client.Responses;

public class CreateRoomResponse extends BaseResponse {
    private String RoomKey;

    @Override
    public String toString() {
        return "CreateRoomResponse{" +
                "RoomKey='" + RoomKey + '\'' +
                '}';
    }

    public String getRoomKey() {
        return RoomKey;
    }

    public void setRoomKey(String roomKey) {
        RoomKey = roomKey;
    }
}
