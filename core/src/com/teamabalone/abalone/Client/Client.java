package com.teamabalone.abalone.Client;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
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
    public Client(String userId) throws JSONException{
        try{
            //Socket is built here
            Client.socket = new Socket("abaloneapi.germanywestcentral.cloudapp.azure.com", 5001);
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
    //Here the Client calls our service, which then proceeds by sending the request
    //TODO: Maybe deal with possible timeouts etc. later on
    public JSONObject sendRequest(int commandCode, JSONObject props) throws Exception {
        Service service = new Service(commandCode,this.userId,props);
        return service.call();
    }

    //Depending on how client/socket will be global, this may be redundant
    //TODO: remove in case of redundancy
    public void closeSocket() throws IOException {
        Client.socket.close();
    }

}