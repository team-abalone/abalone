package com.teamabalone.abalone.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Dialogs.CreateRoomDialog;
import com.teamabalone.abalone.Dialogs.JoinGameDialog;
import com.teamabalone.abalone.Dialogs.SelectLocalFieldDialog;
import com.teamabalone.abalone.Dialogs.SettingsDialog;
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;

import java.util.UUID;

/**
 * Implements the menu screen of the game.
 */
public class MenuScreen implements Screen {
    private Skin defaultSkin = FactoryHelper.getDefaultSkin();
    private GameImpl game;
    private Stage stage;
    private UUID userId;

    private final String commitHash;

    private TextButton createLocalGameButton;
    private TextButton createRoomButton;
    private TextButton joinGameButton;
    private ImageButton settingsButton;
    public Field field;

    private TextureAtlas.AtlasRegion logo = FactoryHelper.getAtlas().findRegion("logo");

    public MenuScreen(GameImpl game, String commitHash) {
        this.game = game;
        stage = game.menuStage;
        this.commitHash = commitHash;
        Preferences preferences = Gdx.app.getPreferences("UserPreferences");
        userId = UUID.fromString(preferences.getString("UserId"));
    }

    @Override
    public void show() {
        Image logoHeading = new Image(logo);
        logoHeading.setPosition((Gdx.graphics.getWidth() - logo.getRegionWidth()) / 2f, Gdx.graphics.getHeight() / 1.8f); //550 is X and 380 is Y position.
        stage.addActor(logoHeading);

        Table buttonTable = FactoryHelper.createTable(
                Gdx.graphics.getWidth() / 3.5f,
                Gdx.graphics.getHeight() / 3f,
                Gdx.graphics.getWidth() / 2f - Gdx.graphics.getWidth() / 8f,
                Gdx.graphics.getHeight() / 6f);

        // Creating and adding buttons.
        createRoomButton = FactoryHelper.createButtonWithText("Create Room");

        createRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open create room overlay.
                Gdx.app.log("ClickListener", createRoomButton.toString() + " clicked");

                CreateRoomDialog createRoomDialog = new CreateRoomDialog(userId, "Create Room", defaultSkin, stage, game);
                createRoomDialog.show(stage);
            }

            ;
        });

        createLocalGameButton = FactoryHelper.createButtonWithText("Local Game");

        createLocalGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SelectLocalFieldDialog selectLocalFieldDialog = new SelectLocalFieldDialog("Local Game", defaultSkin, stage, game);
                selectLocalFieldDialog.show(stage);
            }

            ;
        });

        joinGameButton = FactoryHelper.createButtonWithText("Join Game");

        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JoinGameDialog createRoomDialog = new JoinGameDialog(userId, "Join Room", defaultSkin, stage, game);
                createRoomDialog.show(stage);
            }

            ;
        });


        settingsButton = FactoryHelper.createImageButton(
                defaultSkin.get("settings-btn", ImageButton.ImageButtonStyle.class),
                150,
                150,
                Gdx.graphics.getWidth() - 250,
                Gdx.graphics.getHeight() - 250);

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", settingsButton.toString() + " clicked");
                Skin uiSkin = new Skin(Gdx.files.internal(GameConstants.CUSTOM_UI_JSON));
                SettingsDialog settingsDialog = new SettingsDialog("Settings", uiSkin, null);
                settingsDialog.show(stage);
            }

            ;
        });

        Label versionLabel = new Label(commitHash, defaultSkin);
        versionLabel.setFontScale(0.7f);

        // Adding buttons.
        buttonTable.row().fillX().expandX().padTop(120);
        buttonTable.add(createLocalGameButton);
        buttonTable.row().fillX().expandX().padTop(32);
        buttonTable.add(createRoomButton);
        buttonTable.row().fillX().expandX().padTop(32);
        buttonTable.add(joinGameButton);

        buttonTable.row().padTop(15);

        // Adding version label.
        buttonTable.add(versionLabel).center().bottom();

        stage.addActor(settingsButton);
        stage.addActor(buttonTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        if (field != null) {
            Abalone abalone = new Abalone(game, field);
            game.setScreen(abalone);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
