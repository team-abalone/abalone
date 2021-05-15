package com.teamabalone.abalone.Client;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Helpers.GameConstants;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class SocketManager implements Callable {
    private Socket Socket;
    private InputStream InputStream;
    private OutputStream OutputStream;
    private static SocketManager SocketManager;

    private SocketManager() throws UnknownHostException, IOException {
        Socket = new Socket(GameConstants.SERVER_URL, GameConstants.SERVER_PORT);
        InputStream = Socket.getInputStream();
        OutputStream = Socket.getOutputStream();
    }

    public static SocketManager newInstance() throws UnknownHostException, IOException {
        if(SocketManager == null) {
            SocketManager = new SocketManager();
        }
        return SocketManager;
    }

    // TODO: Test, error handling, restart after message to handle next message.
    private void InitMessageHandler() throws IOException {

        String responseString = IOUtils.toString(InputStream, "UTF-8");

        while(responseString.equals("")) {
            Gson gson = new Gson();
            BaseResponse response = gson.fromJson(responseString, BaseResponse.class);

            //TODO: Handle responses here.
            Gdx.app.log("message received: ", "message received: " + response.toString());
        }
    }

    private void HandleMessage(BaseResponse response) {

    }

    public java.net.Socket getSocket() {
        return Socket;
    }

    @Override
    public Object call() throws Exception {
        InitMessageHandler();

        return null;
    }
}
