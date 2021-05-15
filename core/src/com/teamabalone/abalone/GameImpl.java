package com.teamabalone.abalone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Service;
import com.teamabalone.abalone.Client.SocketManager;
import com.teamabalone.abalone.Screens.MenuScreen;

import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GameImpl extends Game {

    public SpriteBatch batch;
    public MenuScreen menuScreen;

    @Override
    public void create() {
        // Ensuring the app has a UserId stored.
        EnsureUserIdCreated();
        InitSocket();

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

    /**
     * Making sure the socket is created.
     * TODO: Move or change to do before sending requests.
     */
    private void InitSocket() {
        try {
            SocketManager sm = SocketManager.newInstance();
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future1 = executorService.submit(sm);
            executorService.shutdown();

            // Test request for now.
            ExecutorService executorService2 = Executors.newSingleThreadExecutor();
            Service ts = new Service(SocketManager.newInstance().getSocket(), new CreateRoomRequest(2));
            Future future2 = executorService2.submit(ts);
            executorService.shutdown();
        }
        catch (Exception ex) {
            Gdx.app.error(ex.getClass().toString(), ex.getMessage(), ex);
        }
    }
}


//https://libgdx.com/dev/simple-game-extended/