package com.teamabalone.abalone.Client;

import com.teamabalone.abalone.Helpers.GameConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

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
