package com.teamabalone.abalone.Client.Responses;

public class RoomJoinedResponse extends BaseResponse {
    private String RoomKey;

    public String getRoomKey() {
        return RoomKey;
    }

    public void setRoomKey(String roomKey) {
        RoomKey = roomKey;
    }
}
