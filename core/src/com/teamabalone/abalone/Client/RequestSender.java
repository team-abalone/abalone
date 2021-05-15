package com.teamabalone.abalone.Client;

import com.google.gson.Gson;
import com.teamabalone.abalone.Client.Requests.BaseRequest;

import org.json.JSONException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

public class RequestSender implements Callable {
    private final BaseRequest request;
    private final PrintWriter writer;

    /**
     * Sends the given request to the api and returns the response as BaseResponse,
     * which can then outside be parsed depending on the commandCode parameter.
     *
     * @param request
     * @throws IOException
     */
    public RequestSender(Socket socket, BaseRequest request) throws IOException {
        this.request = request;
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    @Override
    public Object call() throws Exception {
        try {
            Gson gson = new Gson();

            this.writer.println(gson.toJson(request));
            this.writer.flush();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
