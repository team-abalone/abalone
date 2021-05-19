package com.teamabalone.abalone.Client;

import com.teamabalone.abalone.Client.Responses.BaseResponse;

public interface IResponseHandlerObserver {
    void HandleResponse(BaseResponse response);
}
