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
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.GameInfo;
import com.teamabalone.abalone.Gamelogic.GameStartPositions;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Screens.MenuScreen;

public class SelectLocalFieldDialog extends Dialog {
    private ImageButton exitButton;
    private Label headerLabel;
    private final Stage stage;
    private SelectBox<GameStartPositions> initialFieldTypeSelect;
    private GameImpl game;

    Table titleTable = getTitleTable();
    Table rootTable = getContentTable();
    Table buttonTable = getButtonTable();


    public SelectLocalFieldDialog(String title, Skin skin, Stage stage, GameImpl game) {
        super(title, skin);
        this.game = game;
        GameInfo.getInstance().setSingleDeviceMode(true);
        this.stage = stage;
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
                GameInfo.getInstance().setSingleDeviceMode(false);
                GameInfo.getInstance().setStartPosition(null);
                remove();
            }
        });
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameStartPositions ift = initialFieldTypeSelect.getSelected();
                GameInfo.getInstance().setStartPosition(ift);

                game.menuScreen.setField(new Field(5));

                remove();
            }

            ;
        });
        Label initialFieldTypeLabel = new Label("Select the initial field type:", skin);

        initialFieldTypeSelect = new SelectBox<>(skin);
        initialFieldTypeSelect.setItems(GameStartPositions.values());
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
