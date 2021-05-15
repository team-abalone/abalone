package com.teamabalone.abalone.Client;

import com.teamabalone.abalone.Client.Requests.BaseRequest;

//TODO: Make Client Singleton Class (after testing), make Client be global
public class Client {
    public static SocketManager SocketManager;

    /**
     * Constructor, Builds socket and instanciates neccasary classes for further actions
     * @param userId - May be of use, if we do not decide to automatically generate the user's id
     */
    public Client() {
        try {
            SocketManager = SocketManager.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Here the Client calls our service, which then proceeds by sending the request
    //TODO: Maybe deal with possible timeouts etc. later on
    public Object sendRequest(BaseRequest request) throws Exception {
        Service service = new Service(SocketManager.getSocket(), request);
        return service.call();
    }
}