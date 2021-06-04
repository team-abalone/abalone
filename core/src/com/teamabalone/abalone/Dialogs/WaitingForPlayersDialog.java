package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Client.IResponseHandlerObserver;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.CloseRoomRequest;
import com.teamabalone.abalone.Client.Requests.StartGameRequest;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.GameStartedResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Client.Responses.RoomJoinedResponse;
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Screens.MenuScreen;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class WaitingForPlayersDialog extends Dialog implements IResponseHandlerObserver {
    private com.badlogic.gdx.scenes.scene2d.ui.List<UUID> currentPlayersList;
    private ImageButton exitButton;
    private Label headerLabel;
    private String roomKey;
    private UUID[] playerList;

    private GameImpl game;

    public WaitingForPlayersDialog(UUID userId, String title, Skin skin, boolean isRoomCreator, GameImpl game) {
        super(title, skin);
        this.game = game;

        Table rootTable = getContentTable();
        Table buttonTable = getButtonTable();
        Table titleTable = getTitleTable();
        rootTable.setFillParent(true);


        ResponseHandler responseHandler = ResponseHandler.newInstance();
        responseHandler.addObserver(this);

        headerLabel = new Label("", skin);
        exitButton = FactoryHelper.createImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            };
        });

        currentPlayersList = new com.badlogic.gdx.scenes.scene2d.ui.List<UUID>(skin);

        // Only the creator of the room can start it.
        if (isRoomCreator) {
            TextButton startGameButton = FactoryHelper.createButtonWithText("Start Game", 100, 100);
            buttonTable.add(startGameButton).width(800);
            buttonTable.setWidth(getWidth());

            // Add self to current player list if room creator.
            playerList = new UUID[] { userId };
            currentPlayersList.setItems(playerList);

            startGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    StartGameRequest startGameRequest = new StartGameRequest(userId, roomKey);

                    try {
                        RequestSender rs = new RequestSender(startGameRequest);
                        ExecutorService executorService = Executors.newSingleThreadExecutor();
                        Future future = executorService.submit(rs);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };
            });
        }

        // Button for closing room.
        TextButton closeRoomButton = FactoryHelper.createButtonWithText("Close Game", 100, 100);
        closeRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CloseRoomRequest closeRoomRequest = new CloseRoomRequest(userId, roomKey);

                try {
                    RequestSender rs = new RequestSender(closeRoomRequest);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future future = executorService.submit(rs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            ;
        });

        titleTable.add(exitButton).right().top().width(100).height(100);
        rootTable.row().padTop(250).height(150);

        rootTable.add(currentPlayersList).fillX().center();

        if(isRoomCreator) {
            buttonTable.add(closeRoomButton).width(800);
        }

        buttonTable.setWidth(getWidth());
    }

    public void setTitle(String title) {
        super.getTitleLabel().setText(title);
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public void setPlayers(UUID[] players) {
        this.playerList = players;
        currentPlayersList.setItems(players);
    }

    @Override
    public void HandleResponse(BaseResponse response) {
        // Room closal done server side.
        if (response.getCommandCode() == ResponseCommandCodes.ROOM_CLOSED.getValue()) {
            remove();
        }
        // Other players has joined room.
        else if (response.getCommandCode() == ResponseCommandCodes.ROOM_JOINED_OTHER.getValue()) {
            // Updating player list.
            List<UUID> playerListTemp = ((RoomJoinedResponse) response).getPlayers();
            playerList = playerListTemp.toArray(new UUID[playerListTemp.size()]);
            currentPlayersList.setItems(playerList);
        }
        // Game has started.
        // TODO: Init client side field with data from server.
        else if (response.getCommandCode() == ResponseCommandCodes.GAME_STARTED.getValue()) {
            // TODO: Handle start game -> response can be parsed to GameStartedResponse
            MenuScreen.field = new Field(5, (GameStartedResponse) response);
        }
        else if(response.getCommandCode() == ResponseCommandCodes.ROOM_EXCEPTION.getValue()){
            //Exception handling goes here : Maybe a small notification to be shown
        }
        else if(response.getCommandCode() == ResponseCommandCodes.SERVER_EXCEPTION.getValue()){
            //Exception handling goes here : Maybe a small notification to be shown
        }
        else if(response.getCommandCode() == ResponseCommandCodes.GAME_EXCEPTION.getValue()){
            //Exception handling goes here : Maybe a small notification to be shown
        }
    }
}
