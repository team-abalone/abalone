package com.teamabalone.abalone.Client;

import java.net.Socket;

public class SocketManager {
    private Socket Socket;
    private static SocketManager SocketManager;

    private SocketManager() {
        Socket = new Socket();
    }

    public SocketManager newInstance() {
        if(SocketManager == null) {
            SocketManager = new SocketManager();
        }
        return SocketManager;
    }

    public java.net.Socket getSocket() {
        return Socket;
    }

    public void setSocket(java.net.Socket socket) {
        Socket = socket;
    }
}
