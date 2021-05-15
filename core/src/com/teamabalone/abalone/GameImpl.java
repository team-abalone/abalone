package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamabalone.abalone.Client.ICoreLauncher;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.SocketManager;
import com.teamabalone.abalone.Screens.MenuScreen;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameImpl extends Game {


    public static Abalone abalone;


    private ICoreLauncher Launcher;
    public SpriteBatch batch;
    public MenuScreen menuScreen;

    public GameImpl(ICoreLauncher launcher) {
        Launcher = launcher;
    }

    @Override
    public void create() {
        // Ensuring the app has a UserId stored.
        EnsureUserIdCreated();


        // Initializing our response handler, which is called from Service.
        ResponseHandler rh = new ResponseHandler();
        Launcher.setICoreResponseMessageHandler(rh);

        try {
            SocketManager sm = SocketManager.newInstance();
            Launcher.setSocket(sm.getSocket());

        } catch (IOException e) {
            e.printStackTrace();
        }

        TestMessage();

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

    /**
     * Making sure the socket is created.
     * TODO: Move or change to do before sending requests.
     */
    private void TestMessage() {
        try {
            // Test request for now.
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            RequestSender ts = new RequestSender(SocketManager.newInstance().getSocket(), new CreateRoomRequest(UUID.randomUUID(), 2));
            Future future = executorService.submit(ts);
            executorService.shutdown();
        }
        catch (Exception ex) {
            Gdx.app.error(ex.getClass().toString(), ex.getMessage(), ex);
        }
    }
}


//https://libgdx.com/dev/simple-game-extended/
