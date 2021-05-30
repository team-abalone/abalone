package com.teamabalone.abalone.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Dialogs.CreateRoomDialog;
import com.teamabalone.abalone.Dialogs.JoinGameDialog;
import com.teamabalone.abalone.Dialogs.SettingsDialog;
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Gamelogic.Field;
import com.teamabalone.abalone.Gamelogic.GameInfo;
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

    private TextureAtlas.AtlasRegion logo = FactoryHelper.getAtlas().findRegion("logo");

    public MenuScreen(GameImpl game, String commitHash) {
        this.game = game;
        this.commitHash = commitHash;
        Preferences preferences = Gdx.app.getPreferences("UserPreferences");
        userId = UUID.fromString(preferences.getString("UserId"));
    }

    @Override
    public void show() {
        Table buttonTable = FactoryHelper.createTable(
                Gdx.graphics.getWidth() / 3.5f,
                Gdx.graphics.getHeight() / 3,
                Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8,
                Gdx.graphics.getHeight() / 6);

        // Creating and adding buttons.
        createRoomButton = FactoryHelper.createButtonWithText("Create Room");

        createRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open create room overlay.
                Gdx.app.log("ClickListener", createRoomButton.toString() + " clicked");

                //Game.setScreen(new Abalone(Game));
                CreateRoomDialog createRoomDialog = new CreateRoomDialog(userId, "Create Room", defaultSkin, stage, game);
                createRoomDialog.show(stage);
            }

            ;
        });

        GameInfo.getInstance().setSingleDeviceMode(true);
        createLocalGameButton = FactoryHelper.createButtonWithText("Local Game");

        createLocalGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Field field = new Field(GameInfo.getInstance().getMapSize());
                game.setScreen(new Abalone(game, field));
            };
        });

        joinGameButton = FactoryHelper.createButtonWithText("Join Game");

        joinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                JoinGameDialog createRoomDialog = new JoinGameDialog(userId, "Join Room", defaultSkin, stage, game);
                createRoomDialog.show(stage);
            };
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
                SettingsDialog settingsDialog = new SettingsDialog("", uiSkin, null);
                settingsDialog.show(stage);
            }

            ;
        });

        Label versionLabel = new Label(commitHash, defaultSkin);
        versionLabel.setFontScale(0.7f);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

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

        // Drawing logo.
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        batch.draw(logo, Gdx.graphics.getWidth() / 2 - logo.getRegionWidth() / 2, Gdx.graphics.getHeight() / 1.8f); //550 is X and 380 is Y position.
        batch.end();
        stage.act();
        stage.draw();
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
}
