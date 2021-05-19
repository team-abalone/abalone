package com.teamabalone.abalone.Dialogs;

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
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Helpers.FactoryHelper;

import java.util.UUID;

public class WaitingForPlayersDialog extends Dialog {

    private ImageButton exitButton;


    public WaitingForPlayersDialog(String title, Skin skin, boolean isRoomCreator) {
        super(title, skin);

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

        /**
         * Only the creator of the room can start the game.
         */
        if(isRoomCreator) {
            TextButton StartGameButton = FactoryHelper.CreateButtonWithText("StartGame", 100, 100);
            getButtonTable().add(StartGameButton).width(800);
            getButtonTable().setWidth(getWidth());
        }

        rootTable.row();
    }

    @Override
    public Dialog show(Stage stage) {
        return super.show(stage);
    }

    public void SendCreateRoomRequest() {
        CreateRoomRequest createRoomRequest = new CreateRoomRequest(UUID.randomUUID(), 2);
    }
}
