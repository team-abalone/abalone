package com.teamabalone.abalone.Client;

import com.google.gson.Gson;
import com.teamabalone.abalone.Client.Requests.BaseRequest;
import com.teamabalone.abalone.Client.Responses.BaseResponse;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;

public class Service implements Callable<BaseResponse> {
    private final BaseRequest request;
    private PrintWriter writer;
    private BufferedReader br;


    /**
     * Sends the given request to the api and returns the response as BaseResponse,
     * which can then outside be parsed depending on the commandCode parameter.
     *
     * @param request
     * @throws IOException
     */
    public Service(Socket socket, BaseRequest request) throws IOException {
        this.request = request;

        this.writer = new PrintWriter(socket.getOutputStream());
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    }

    /**
     * Builds JSON to send to api (as string, in the end)
     * Receives String from api. We will convert that to a JSONObject by simply using new JSONObject(string response)
     */
    @Override
    public BaseResponse call() throws Exception {

        try {
            Gson gson = new Gson();

            this.writer.println(gson.toJson(request));
            this.writer.flush();

            return gson.fromJson(this.br.readLine(), BaseResponse.class);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
