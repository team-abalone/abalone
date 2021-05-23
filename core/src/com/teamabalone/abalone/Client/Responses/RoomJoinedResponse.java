package com.teamabalone.abalone.Client.Responses;

import java.util.List;
import java.util.UUID;

public class RoomJoinedResponse extends BaseResponse {
    private String RoomKey;

    private List<UUID> Players;

    private UUID CreatedBy;

    private int NumberOfPlayers;

    public String getRoomKey() {
        return RoomKey;
    }

    public void setRoomKey(String roomKey) {
        RoomKey = roomKey;
    }

    public List<UUID> getPlayers() {
        return Players;
    }

    public void setPlayers(List<UUID> players) {
        Players = players;
    }

    public UUID getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(UUID createdBy) {
        CreatedBy = createdBy;
    }

    public int getNumberOfPlayers() {
        return NumberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        NumberOfPlayers = numberOfPlayers;
    }
}
