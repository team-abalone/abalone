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
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.Requests.InititalFieldType;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.GameStartedResponse;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Screens.MenuScreen;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SelectLocalFieldDialog extends Dialog {
    private ImageButton exitButton;
    private Label headerLabel;
    private final Stage stage;
    private SelectBox<InititalFieldType> initialFieldTypeSelect;

    Table titleTable = getTitleTable();
    Table rootTable = getContentTable();
    Table buttonTable = getButtonTable();



    public SelectLocalFieldDialog(String title, Skin skin, Stage stage) {
        super(title, skin);
        this.stage= stage;
        headerLabel = new Label("", skin);

        exitButton = FactoryHelper.createImageButton(skin.get("exit-btn", ImageButton.ImageButtonStyle.class));
        TextButton playButton = FactoryHelper.createButtonWithText("Play", 100, 100);

        exitButton.setHeight(100);
        exitButton.setWidth(100);

        buttonTable.add(playButton).width(800);
        buttonTable.setWidth(getWidth());


        titleTable.add(exitButton).right().top().width(100).height(100);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Preferences settings = Gdx.app.getPreferences("UserSettings");
                InititalFieldType ift = initialFieldTypeSelect.getSelected();

                Field localField = new Field(6,true);
                //Maybe, to stay uniform with our parameters, we could build a pseudo-response here
                //MenuScreen.field =localField;
                remove();
            };
        });
        Label initialFieldTypeLabel = new Label("Select the initial field type:", skin);

        initialFieldTypeSelect = new SelectBox<>(skin);
        initialFieldTypeSelect.setItems(InititalFieldType.values());
        rootTable.row().padTop(250);

        rootTable.add(initialFieldTypeLabel).left();
        rootTable.add(initialFieldTypeSelect).left().padLeft(20);

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
}
