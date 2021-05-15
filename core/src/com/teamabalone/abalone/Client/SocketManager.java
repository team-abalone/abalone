package com.teamabalone.abalone.Client;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Helpers.GameConstants;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

public class SocketManager {
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

    public java.net.Socket getSocket() {
        return Socket;
    }
}
