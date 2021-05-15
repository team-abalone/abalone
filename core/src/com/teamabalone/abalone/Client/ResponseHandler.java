package com.teamabalone.abalone.Client;

import com.badlogic.gdx.Gdx;
import com.teamabalone.abalone.Client.Responses.BaseResponse;


public class ResponseHandler implements ICoreResponseMessageHandler {

    @Override
    public void HandleMessage(BaseResponse response) {
        Gdx.app.log("response received", response.toString());
    }
}
