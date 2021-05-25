package com.teamabalone.abalone.Client;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.google.gson.Gson;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.BaseRequest;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.Requests.RequestCommandCodes;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.CreateRoomResponse;
import com.teamabalone.abalone.Client.SocketManager;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.Buffer;
import java.util.UUID;

public class ClientTest {
    @Test
    public void socketManagerTest() throws IOException {

        SocketManager sm = SocketManager.newInstance();
        Assert.assertEquals(true, sm.getSocket() instanceof Socket);

    }
    @Test
    public void singletonTest() throws IOException {
        SocketManager sm1 = SocketManager.newInstance();
        SocketManager sm2 = SocketManager.newInstance();
        Assert.assertEquals(true, sm1.equals(sm2));
    }
    @Test
    public void responseHandlerTest() {
        ResponseHandler rh =ResponseHandler.newInstance();
        Gson gson = new Gson();
        String responseString = "{CommandCode: 40}";
        gson.toJson(responseString);
        BaseResponse response = gson.fromJson(responseString, BaseResponse.class);

        rh.HandleMessage(response);
        //TODO: Check for return value in responseHandler

    }
    @Test
    public void requestSenderTest() throws Exception {
        UUID userId = new UUID(1,32);
        BaseRequest br = new CreateRoomRequest(userId,3);
        Gson gson = new Gson();
        System.out.println(gson.toJson(br));
        SocketManager sm = SocketManager.newInstance();

        RequestSender rs = new RequestSender(br);
        rs.call();

        InputStream is = sm.getSocket().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String response = reader.readLine();
        String resString = response.toString();
        int outCommand = Integer.parseInt(resString.split("")[15]+""+resString.split("")[16]);
        Assert.assertEquals(40,outCommand);
    }
    @Test
    public void requestCommandCodesTestCreate() {
        RequestCommandCodes cc = RequestCommandCodes.CREATE_ROOM;
        int a = 20;
        Assert.assertEquals(a, cc.getValue());
    }


}
