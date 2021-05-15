package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamabalone.abalone.Screens.MenuScreen;

public class GameImpl extends Game {

    public static Abalone abalone;

    public SpriteBatch batch;
    public MenuScreen menuScreen;

    @Override
    public void create() {
        batch = new SpriteBatch();
        menuScreen = new MenuScreen(this);
        this.setScreen(menuScreen);
    }

    @Override
    public void resume() {
        super.resume();
        this.setScreen(abalone);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {/*
        super.dispose();
        menuScreen.dispose();
        batch.dispose();*/
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}


//https://libgdx.com/dev/simple-game-extended/