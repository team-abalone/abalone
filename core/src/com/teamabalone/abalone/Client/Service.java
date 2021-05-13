package com.teamabalone.abalone.Client;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

public class Service implements Callable<JSONObject> {
    //Needed to send request
    private int commandCode;
    private String userId;
    private JSONObject props;
    private PrintWriter writer;
    private BufferedReader br;

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
     *
     */
    @Override
    public JSONObject call() throws Exception{
        JSONObject req = new JSONObject();
        try {
            //This is just to ensure that our String will be build correctly
            req.put("userId", this.userId);
            req.put("commandCode", this.commandCode);
            req.put("props", this.props);

            this.writer.println(req.toString());
            this.writer.flush();

            return new JSONObject(this.br.readLine());

        }
        catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
