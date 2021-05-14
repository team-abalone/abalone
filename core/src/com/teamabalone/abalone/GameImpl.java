package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamabalone.abalone.Screens.MenuScreen;

import java.util.UUID;

public class GameImpl extends Game {

    public SpriteBatch batch;
    public MenuScreen menuScreen;

    @Override
    public void create() {
        // Ensuring the app has a UserId stored.
        EnsureUserIdCreated();

        batch = new SpriteBatch();
        menuScreen = new MenuScreen(this);
        this.setScreen(menuScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        menuScreen.dispose();
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Ensures, the app has a generated UserId, which is later used for api communication.
     * Upon first start a new id is generated and stored using Preferences.
     */
    private void EnsureUserIdCreated() {
        Preferences preferences = Gdx.app.getPreferences("UserPreferences");
        String userId = preferences.getString("UserId");

        if(userId == null || userId.isEmpty()) {
            userId = UUID.randomUUID().toString();
            preferences.putString("UserId", userId);
            preferences.flush();
        }
    }
}


//https://libgdx.com/dev/simple-game-extended/