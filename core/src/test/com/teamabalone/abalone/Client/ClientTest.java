package com.teamabalone.abalone.Client;

import com.badlogic.gdx.utils.compression.lzma.Base;
import com.google.gson.Gson;
import com.teamabalone.abalone.Client.RequestSender;
import com.teamabalone.abalone.Client.Requests.BaseRequest;
import com.teamabalone.abalone.Client.Requests.CreateRoomRequest;
import com.teamabalone.abalone.Client.Requests.InititalFieldType;
import com.teamabalone.abalone.Client.Requests.RequestCommandCodes;
import com.teamabalone.abalone.Client.ResponseHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.CreateRoomResponse;
import com.teamabalone.abalone.Client.SocketManager;
import com.teamabalone.abalone.Helpers.Helpers;

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
        Assert.assertTrue(sm.getSocket() instanceof Socket);
    }

    @Test
    public void singletonTest() throws IOException {
        SocketManager sm1 = SocketManager.newInstance();
        SocketManager sm2 = SocketManager.newInstance();
        Assert.assertTrue(sm1.equals(sm2));
    }

    @Test
    public void responseHandlerTest() {
        ResponseHandler rh = ResponseHandler.newInstance();
        Gson gson = Helpers.GetGsonInstance();
        String responseString = "{CommandCode: 40}";
        gson.toJson(responseString);
        BaseResponse response = gson.fromJson(responseString, BaseResponse.class);

        rh.HandleMessage(response);
        //TODO: Check for return value in responseHandler
    }

    @Test
    public void requestSenderTest() throws Exception {
        UUID userId = new UUID(1, 32);
        BaseRequest br = new CreateRoomRequest(userId, 3, "bspid", InititalFieldType.DEFAULT);
        SocketManager sm = SocketManager.newInstance();

        RequestSender rs = new RequestSender(br);
        rs.call();

        InputStream is = sm.getSocket().getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String response = reader.readLine();
        int outCommand = Integer.parseInt(response.split("")[15] + "" + response.split("")[16]);
        Assert.assertEquals(40, outCommand);
    }

    @Test
    public void requestCommandCodesTestCreate() {
        RequestCommandCodes cc = RequestCommandCodes.CREATE_ROOM;
        Assert.assertEquals(20, cc.getValue());
    }

}
