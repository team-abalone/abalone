package com.teamabalone.abalone.Client;

import com.teamabalone.abalone.Client.Responses.BaseResponse;

public interface ICoreResponseMessageHandler {
    void HandleMessage(BaseResponse response);
}
