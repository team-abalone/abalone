package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Client.IResponseHandlerObserver;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.Requests.JoinRoomRequest;
import com.teamabalone.abalone.Client.Requests.LeaveRoomRequest;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Client.Responses.RoomJoinedResponse;
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Helpers.FactoryHelper;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JoinGameDialog extends Dialog implements IResponseHandlerObserver {
    private final Stage stage;
    private final UUID userId;
    private final TextField tfRoomKey;
    private WaitingForPlayersDialog waitingForPlayersDialog;

    private com.teamabalone.abalone.Client.ResponseHandler ResponseHandler;

    public JoinGameDialog(UUID userId, String title, final Skin skin, Stage stage, GameImpl game) {
        super(title, skin);
        this.stage = stage;
        this.userId = userId;

        try{
            RequestSender rs = new RequestSender(new LeaveRoomRequest());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(rs);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        // Creating dialogs.
        waitingForPlayersDialog = new WaitingForPlayersDialog(userId,"Waiting for players...", FactoryHelper.getDefaultSkin(), false, game);

        ResponseHandler = com.teamabalone.abalone.Client.ResponseHandler.newInstance();
        ResponseHandler.addObserver(this);

        Table rootTable = getContentTable();
        Table buttonTable = getButtonTable();
        Table titleTable = getTitleTable();

        rootTable.setFillParent(true);

        // Exit button.
        ImageButton exitButton = FactoryHelper.createExitButton();

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            };
        });

        tfRoomKey = new TextField("", FactoryHelper.getDefaultSkin());
        TextField.TextFieldStyle style = tfRoomKey.getStyle();
        style.background.setLeftWidth(60);
        tfRoomKey.setStyle(style);

        Label roomKeyLabel = new Label("Enter the RoomKey: ", skin);

        TextButton joinRoomButton = FactoryHelper.createButtonWithText("Join Room", 100, 100);

        joinRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferences settings = Gdx.app.getPreferences("UserSettings");
                String userName = settings.getString("UserName");

                JoinRoomRequest joinRoomRequest = new JoinRoomRequest(userId, tfRoomKey.getText(), userName != null ? userName : "default");

                try {
                    RequestSender rs = new RequestSender(joinRoomRequest);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.submit(rs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        });

        titleTable.add(exitButton).right().top().width(100).height(100);

        rootTable.row().padTop(250).height(150);

        rootTable.add(roomKeyLabel);
        rootTable.add(tfRoomKey).fillX().width(Gdx.graphics.getWidth() / 5);

        buttonTable.add(joinRoomButton).width(800);
        buttonTable.setWidth(getWidth());
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void HandleResponse(BaseResponse response) {
        // User joined successfully.
        if (response.getCommandCode() == ResponseCommandCodes.ROOM_JOINED.getValue()) {
            waitingForPlayersDialog.setTitle(String.format("Waiting for players (%s) ... ", ((RoomJoinedResponse) response).getRoomKey()));
            Collection<String> playerListTemp = ((RoomJoinedResponse) response).getPlayerMap().values();
            String[] playerList = playerListTemp.toArray(new String[playerListTemp.size()]);
            waitingForPlayersDialog.setPlayers(playerList);
            waitingForPlayersDialog.show(stage);
            this.hide();
        }
        else if(response.getCommandCode() == ResponseCommandCodes.ROOM_EXCEPTION.getValue()){
            //Exception handling goes here : Maybe a small notification to be shown
        }
        else if(response.getCommandCode() == ResponseCommandCodes.SERVER_EXCEPTION.getValue()){
            //Exception handling goes here : Maybe a small notification to be shown
        }
        else if(response.getCommandCode() == ResponseCommandCodes.LEFT_ROOM.getValue()){

        }
        else if(response.getCommandCode() == ResponseCommandCodes.NO_ROOM_TO_LEAVE.getValue()){

        }
    }
}