package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
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
import com.teamabalone.abalone.Client.Requests.JoinRoomRequest;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.CreateRoomResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Client.Responses.RoomJoinedResponse;
import com.teamabalone.abalone.Helpers.FactoryHelper;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JoinGameDialog extends Dialog implements IResponseHandlerObserver {
    private final Stage stage;
    private final UUID userId;
    //private final TextField tfRoomKey;

    private com.teamabalone.abalone.Client.ResponseHandler ResponseHandler;

    public JoinGameDialog(UUID userId, String title, final Skin skin, Stage stage) {
        super(title, skin);
        this.stage = stage;
        this.userId = userId;

        ResponseHandler = com.teamabalone.abalone.Client.ResponseHandler.newInstance();
        ResponseHandler.addObserver(this);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);

        Label header = new Label(title, skin);

        ImageButton exitButton = FactoryHelper.CreateImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);
        exitButton.padTop(1000);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            };
        });

//        tfRoomKey = new TextField("RoomKey", FactoryHelper.GetDefaultSkin());

        TextButton joinRoomButton = FactoryHelper.CreateButtonWithText("Join Room", 100, 100);

        joinRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JoinRoomRequest joinRoomRequest = new JoinRoomRequest(UUID.randomUUID(), "oyoKS");

                try {
                    RequestSender rs = new RequestSender(joinRoomRequest);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future future = executorService.submit(rs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        });

        rootTable.add(header).left();

        //rootTable.add(tfRoomKey);

        rootTable.add(exitButton).right().top().expandX().height(100);

        rootTable.row().padTop(50);

        getButtonTable().add(joinRoomButton).width(800);
        getButtonTable().setWidth(getWidth());
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
        if (response.getCommandCode() == ResponseCommandCodes.ROOM_JOINED.getValue()) {
            WaitingForPlayersDialog waitingForPlayersDialog = new WaitingForPlayersDialog(userId,"Waiting for players...", FactoryHelper.GetDefaultSkin(), false);
            waitingForPlayersDialog.show(stage);

            this.hide();
        }
    }
}