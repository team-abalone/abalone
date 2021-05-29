package com.teamabalone.abalone.Client.Responses;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameStartedResponse extends BaseResponse {
    private String roomKey;
    private List<UUID> players;
    private HashMap<UUID, String> playerMap;
    private UUID createdBy;
    private int numberOfPlayers;
    protected int[][] gameField;

    public int[][] getGameField() {
        return gameField;
    }

    public void setGameField(int[][] gameField) {
        this.gameField = gameField;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public void setPlayers(List<UUID> players) {
        this.players = players;
    }

    public HashMap<UUID, String> getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(HashMap<UUID, String> playerMap) {
        this.playerMap = playerMap;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }
}
