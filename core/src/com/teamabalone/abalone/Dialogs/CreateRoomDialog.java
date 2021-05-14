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
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;

public class CreateRoomDialog extends Dialog {
    private ImageButton exitButton;


    public CreateRoomDialog(String title, Skin skin) {
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

        TextButton CreateRoomButton = FactoryHelper.CreateButtonWithText("Create Room", 100, 100);

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
}
