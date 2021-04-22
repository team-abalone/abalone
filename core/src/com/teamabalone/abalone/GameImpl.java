package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameImpl extends Game {

    public SpriteBatch batch;
    public Abalone abalone;

    @Override
    public void create() {
        batch = new SpriteBatch();
        abalone = new Abalone(this);
        this.setScreen(abalone);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        abalone.dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}


//https://libgdx.com/dev/simple-game-extended/