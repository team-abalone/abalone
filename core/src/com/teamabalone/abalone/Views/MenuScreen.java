package com.teamabalone.abalone.Views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.teamabalone.abalone.Helpers.FactoryHelper;
import com.teamabalone.abalone.Helpers.Helpers;

import java.util.Set;

/**
 * Implements the menu screen of the game.
 */
public class MenuScreen implements Screen {
    private Game game;

    private TextButton CreateRoomButton;
    private TextButton JoinGameButton;
    private ImageButton SettingsButton;

    private Stage stage;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        Table buttonTable = FactoryHelper.CreateTable(
                Gdx.graphics.getWidth() / 4,
                Gdx.graphics.getHeight() / 3,
                Gdx.graphics.getWidth() / 2 - Gdx.graphics.getWidth() / 8,
                Gdx.graphics.getHeight() / 6);

        CreateRoomButton = FactoryHelper.CreateButtonWithText("Create Room");
        JoinGameButton = FactoryHelper.CreateButtonWithText("Join Game");

        SettingsButton = FactoryHelper.CreateImageButton(
                Helpers.TextureToDrawable(new Texture(Gdx.files.internal("buttons/settings_up.png"))),
                Helpers.TextureToDrawable(new Texture(Gdx.files.internal("buttons/settings_down.png"))),
                Helpers.TextureToDrawable(new Texture(Gdx.files.internal("buttons/settings_background.png"))),
                200,
                200,
                Gdx.graphics.getWidth() - 250,
                Gdx.graphics.getHeight() - 250);


        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Adding buttons.
        buttonTable.row().fillX().expandX();
        buttonTable.add(CreateRoomButton);
        buttonTable.row().fillX().expandX().padTop(32);
        buttonTable.add(JoinGameButton);
        stage.addActor(SettingsButton);
        stage.addActor(buttonTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
