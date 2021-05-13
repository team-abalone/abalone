package com.teamabalone.abalone.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


//TODO: Make Client Singleton Class (after testing), make Client be global
public class Client {
    public static Socket socket;
    private String userId;

    /**
     * Constructor, Builds socket and instanciates neccasary classes for further actions
     * @param userId - May be of use, if we do not decide to automatically generate the user's id
     */
    public Client(String userId) /*throws JSONException*/{
        this.userId = userId;
        try{
            //Socket is built here
            this.socket = new Socket("abaloneapi.germanywestcentral.cloudapp.azure.com", 5001);
            this.userId=userId;
        }
        //TODO: Deal with different Exceptions accordingly
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendRequest(int commandCode, JSONObject props) throws JSONException, IOException {
        Service service = new Service(commandCode,this.userId,props);
        service.start();
    }

    public void closeSocket() throws IOException {
        this.socket.close();
    }

}