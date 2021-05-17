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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.Dialogs.SettingsDialog;
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.GameConstants;
import com.teamabalone.abalone.View.GameSet;


/**
 * Implements the menu screen of the game.
 */
public class MenuScreen implements Screen {
    private GameImpl Game;
    Skin Skin = FactoryHelper.GetDefaultSkin();

    private TextButton CreateRoomButton;
    private TextButton JoinGameButton;
    private ImageButton SettingsButton;
    TextureAtlas.AtlasRegion logo = FactoryHelper.GetAtlas().findRegion("logo");

    private Stage Stage;

    public MenuScreen(GameImpl game) {
        this.Game = game;
    }

    @Override
    public void show() {
        Table buttonTable = FactoryHelper.CreateTable(
                Gdx.graphics.getWidth() / 3.5f,
                Gdx.graphics.getHeight() / 3,
                Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8,
                Gdx.graphics.getHeight() / 6);
        // Creating and adding buttons.
        CreateRoomButton = FactoryHelper.CreateButtonWithText("Create Room");
        CreateRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open create room overlay.
                Gdx.app.log("ClickListener", CreateRoomButton.toString() + " clicked");
                GameImpl.abalone = new Abalone(Game);
                Game.setScreen(GameImpl.abalone);
            }

            ;
        });

        JoinGameButton = FactoryHelper.CreateButtonWithText("Join Game");
        JoinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open join game overlay.
                Gdx.app.log("ClickListener", JoinGameButton.toString() + " clicked");
            };
        });


        SettingsButton = FactoryHelper.CreateImageButton(
                Skin.get("settings-btn", ImageButton.ImageButtonStyle.class),
                150,
                150,
                Gdx.graphics.getWidth() - 250,
                Gdx.graphics.getHeight() - 250);

        SettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", SettingsButton.toString() + " clicked");
                Skin uiSkin = new Skin(Gdx.files.internal(GameConstants.CUSTOM_UI_JSON));
                SettingsDialog settingsDialog = new SettingsDialog("", uiSkin);
                settingsDialog.show(Stage);
            }

            ;
        });

        Stage = new Stage();
        Gdx.input.setInputProcessor(Stage);

        // Adding buttons.
        buttonTable.row().fillX().expandX();
        buttonTable.add(CreateRoomButton);
        buttonTable.row().fillX().expandX().padTop(32);
        buttonTable.add(JoinGameButton);
        Stage.addActor(SettingsButton);
        Stage.addActor(buttonTable);
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
        Stage.act();
        Stage.draw();
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
