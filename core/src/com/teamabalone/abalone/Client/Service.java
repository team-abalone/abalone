package com.teamabalone.abalone.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Service extends Thread{
    //Needed to send request
    int commandCode;
    String userId;
    JSONObject props;
    PrintWriter writer;
    BufferedReader br;

    /**
     * Prepares everything needed for our requests
     * @param commandCode - will provide the corresponding server-action
     * @param userId - still unsure, but let's keep it in for now
     * @param props - further information regarding the request
     * @throws IOException - can occur due to InputStream and OutputStream
     */
    public Service(int commandCode, String userId, JSONObject props) throws IOException {
        this.commandCode = commandCode;
        this.userId = userId;
        this.props = props;

        this.writer = new PrintWriter(Client.socket.getOutputStream());
        this.br = new BufferedReader(new InputStreamReader(Client.socket.getInputStream(), "UTF-8"));
    }

    /**
     * Builds JSON to send to api (as string, in the end)
     * Receives String from api. We will convert that to a JSONObject by simply using new JSONObject(string response)
     * Will later return that JSONObject - this structure is provided to build standardized requests and responses
     */
    @Override
    public void run() {
        JSONObject req = new JSONObject();
        try {
            req.put("userId", this.userId);
            req.put("commandCode", this.commandCode);
            req.put("props", this.props.toString());

            this.writer.println(req.toString());
            this.writer.flush();

            System.out.println(this.br.readLine());

        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
