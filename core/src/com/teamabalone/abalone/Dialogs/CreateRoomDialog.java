package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Client.IResponseHandlerObserver;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.CreateRoomResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CreateRoomDialog extends Dialog implements IResponseHandlerObserver {
    private final Stage stage;
    private ImageButton exitButton;
    private ResponseHandler ResponseHandler;

    public CreateRoomDialog(String title, final Skin skin, Stage stage) {
        super(title, skin);
        this.stage = stage;

        ResponseHandler = com.teamabalone.abalone.Client.ResponseHandler.newInstance();
        ResponseHandler.addObserver(this);

        Table rootTable = getContentTable();
        rootTable.setFillParent(true);

        Label header = new Label(title, skin);
        exitButton = FactoryHelper.CreateImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);
        exitButton.padTop(1000);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            };
        });

        TextButton CreateRoomButton = FactoryHelper.CreateButtonWithText("Create Room", 100, 100);

        CreateRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CreateRoomRequest createRoomRequest = new CreateRoomRequest(UUID.randomUUID(), 2);

                try {
                    RequestSender rs = new RequestSender(createRoomRequest);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future future = executorService.submit(rs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        });

        rootTable.add(header).left();
        rootTable.add(exitButton).right().top().expandX().height(100);

        Label NumberOfPlayersLabel = new Label("Choose the number of players: ", skin);

        final SelectBox<Integer> numberOfPlayersSelect = new SelectBox<Integer>(skin);
        numberOfPlayersSelect.setItems(GameConstants.PlayerNumberSelect);

        numberOfPlayersSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("ClickListener", event.toString() + " was set");
            }
        });

        rootTable.row().padTop(50);
        rootTable.add(NumberOfPlayersLabel);
        rootTable.add(numberOfPlayersSelect);

        getButtonTable().add(CreateRoomButton).width(800);
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
        if(response.getCommandCode() == ResponseCommandCodes.ROOM_CREATED.getValue()) {
            // TODO: Dialog showing black screen -> Don't know why. Works perfectly if called outside of interface method.
            Gdx.app.log(response.getClass().getSimpleName(), response.toString());
            WaitingForPlayersDialog waitingForPlayersDialog = new WaitingForPlayersDialog("Waiting for players...", FactoryHelper.GetDefaultSkin(), ((CreateRoomResponse) response).getRoomKey(), true);
            waitingForPlayersDialog.show(stage);
            waitingForPlayersDialog.debug();
        }
    }
}
