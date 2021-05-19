package com.teamabalone.abalone.Client;

import com.google.gson.Gson;
import com.teamabalone.abalone.Client.Requests.BaseRequest;
import com.teamabalone.abalone.Helpers.Helpers;

import org.json.JSONException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Callable;

public class RequestSender implements Callable {
    private final BaseRequest request;
    private final PrintWriter writer;
    private SocketManager socketManager;

    /**
     * Sends the given request to the api and returns the response as BaseResponse,
     * which can then outside be parsed depending on the commandCode parameter.
     *
     * @param request
     * @throws IOException
     */
    public RequestSender(BaseRequest request) throws IOException {
        socketManager = SocketManager.newInstance();
        this.request = request;
        this.writer = new PrintWriter(socketManager.getSocket().getOutputStream());
    }

    @Override
    public Object call() throws Exception {
        try {
            Gson gson = Helpers.GetGsonInstance();


            this.writer.println(gson.toJson(request));
            this.writer.flush();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
