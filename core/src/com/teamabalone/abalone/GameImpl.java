package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.teamabalone.abalone.Client.ICoreLauncher;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.SocketManager;
import com.teamabalone.abalone.Screens.MenuScreen;

import java.io.IOException;
import java.util.UUID;

public class GameImpl extends Game {
    public static Abalone abalone;

    public SpriteBatch batch;
    public MenuScreen menuScreen;

    public Stage menuStage;
    public Stage gameStage;

    private ICoreLauncher launcher;

    public GameImpl(ICoreLauncher launcher) {
        this.launcher = launcher;
    }

    @Override
    public void create() {
        // Ensuring the app has a UserId stored.
        ensureUserIdCreated();


        // Initializing our response handler, which is called from Service.
        ResponseHandler rh = ResponseHandler.newInstance();
        launcher.setICoreResponseMessageHandler(rh);

        try {
            SocketManager sm = SocketManager.newInstance();
            launcher.setSocket(sm.getSocket());

        } catch (IOException e) {
            e.printStackTrace();
        }

        menuStage = new Stage();
        gameStage = new Stage();
        Gdx.input.setInputProcessor(menuStage);
        batch = new SpriteBatch();
        menuScreen = new MenuScreen(this, launcher.getCommitHash());
        this.setScreen(menuScreen);
    }

    @Override
    public void resume() {
        super.resume();
        this.setScreen(abalone);
    }

    @Override
    public void dispose() {
        super.dispose();
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
    private void ensureUserIdCreated() {
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
