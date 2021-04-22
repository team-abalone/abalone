package com.teamabalone.abalone.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.teamabalone.abalone.Abalone;
import com.teamabalone.abalone.GameImpl;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.Helpers;


/**
 * Implements the menu screen of the game.
 */
public class MenuScreen implements Screen {
    private GameImpl Game;

    private TextButton CreateRoomButton;
    private TextButton JoinGameButton;
    private ImageButton SettingsButton;

    private Stage Stage;

    public MenuScreen(GameImpl game) {
        this.Game = game;
    }

    @Override
    public void show() {
        Table buttonTable = FactoryHelper.CreateTable(
                Gdx.graphics.getWidth() / 4,
                Gdx.graphics.getHeight() / 3,
                Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8,
                Gdx.graphics.getHeight() / 6);

        CreateRoomButton = FactoryHelper.CreateButtonWithText("Create Room");

        CreateRoomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open create room overlay.
                Gdx.app.log("ClickListener", CreateRoomButton.toString() + " clicked");

                // For testing purposes we  for now use this button for starting the game screen.
                Abalone abalone = new Abalone(Game);
                Game.setScreen(abalone);
            }

            ;
        });

        JoinGameButton = FactoryHelper.CreateButtonWithText("Join Game");
        JoinGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open join game overlay.
                Gdx.app.log("ClickListener", JoinGameButton.toString() + " clicked");
            }

            ;
        });

        SettingsButton = FactoryHelper.CreateImageButton(
                Helpers.TextureToDrawable(new Texture(Gdx.files.internal("buttons/settings_up.png"))),
                Helpers.TextureToDrawable(new Texture(Gdx.files.internal("buttons/settings_down.png"))),
                Helpers.TextureToDrawable(new Texture(Gdx.files.internal("buttons/settings_background.png"))),
                200,
                200,
                Gdx.graphics.getWidth() - 250,
                Gdx.graphics.getHeight() - 250);

        SettingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // TODO: Open settings overlay.
                Gdx.app.log("ClickListener", SettingsButton.toString() + " clicked");
            };
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
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
