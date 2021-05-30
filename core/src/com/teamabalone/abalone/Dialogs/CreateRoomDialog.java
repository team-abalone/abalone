package com.teamabalone.abalone.Dialogs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Client.IResponseHandlerObserver;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.Requests.InititalFieldType;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.CreateRoomResponse;
import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.GameImpl;
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
    private ResponseHandler responseHandler;
    final SelectBox<InititalFieldType> initialFieldTypeSelect;
    final SelectBox<Integer> numberOfPlayersSelect;

    private WaitingForPlayersDialog waitingForPlayersDialog;

    public CreateRoomDialog(UUID userId, String title, final Skin skin, Stage stage, GameImpl game) {
        super(title, skin);
        this.stage = stage;

        // Dialog declarations
        waitingForPlayersDialog = new WaitingForPlayersDialog(userId,"Waiting for players...", FactoryHelper.getDefaultSkin(), true, game);

        responseHandler = com.teamabalone.abalone.Client.ResponseHandler.newInstance();
        responseHandler.addObserver(this);

        Table rootTable = getContentTable();
        Table buttonTable = getButtonTable();
        Table titleTable = getTitleTable();

        rootTable.setFillParent(true);

        Label header = new Label(title, skin);
        exitButton = FactoryHelper.createImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        exitButton.setHeight(100);
        exitButton.setWidth(100);

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            };
        });

        TextButton createRoomButton = FactoryHelper.createButtonWithText("Create Room", 100, 100);

        createRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferences settings = Gdx.app.getPreferences("UserSettings");
                String userName = settings.getString("UserName");

                InititalFieldType ift = initialFieldTypeSelect.getSelected();
                CreateRoomRequest createRoomRequest = new CreateRoomRequest(userId, 2, userName != null ? userName : "default", ift);

                try {
                    RequestSender rs = new RequestSender(createRoomRequest);
                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    Future future = executorService.submit(rs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            };
        });

        titleTable.add(exitButton).right().top().width(100).height(100);

        rootTable.row().padTop(250);

        Label numberOfPlayersLabel = new Label("Choose the number of players:", skin);

        numberOfPlayersSelect = new SelectBox<Integer>(skin);
        numberOfPlayersSelect.setItems(GameConstants.playerNumberSelect);

        Label initialFieldTypeLabel = new Label("Select the initial field type:", skin);

        initialFieldTypeSelect = new SelectBox<>(skin);
        initialFieldTypeSelect.setItems(InititalFieldType.values());

        rootTable.add(numberOfPlayersLabel).left();
        rootTable.add(numberOfPlayersSelect).left().padLeft(20);

        rootTable.row().padTop(40);

        rootTable.add(initialFieldTypeLabel).left();
        rootTable.add(initialFieldTypeSelect).left().padLeft(20);

        buttonTable.add(createRoomButton).width(800);
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
        // Room creation completed -> open waiting for players dialog, hide current dialog.
        if(response.getCommandCode() == ResponseCommandCodes.ROOM_CREATED.getValue()) {

            waitingForPlayersDialog.setTitle(String.format("Waiting for players (%s) ... ", ((CreateRoomResponse) response).getRoomKey()));
            waitingForPlayersDialog.setRoomKey(((CreateRoomResponse) response).getRoomKey());
            waitingForPlayersDialog.show(stage);
            this.hide();
        }
    }
}
