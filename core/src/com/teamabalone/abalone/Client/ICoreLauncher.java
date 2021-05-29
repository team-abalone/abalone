package com.teamabalone.abalone.Client;


import java.net.Socket;

public interface ICoreLauncher {
    void setICoreResponseMessageHandler(com.teamabalone.abalone.Client.ICoreResponseMessageHandler ICoreResponseMessageHandler);
    void setSocket(Socket socket);
    String getCommitHash();
}
