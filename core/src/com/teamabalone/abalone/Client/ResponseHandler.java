package com.teamabalone.abalone.Client;

import com.teamabalone.abalone.Client.Responses.BaseResponse;

import java.util.ArrayList;
import java.util.List;


public class ResponseHandler implements ICoreResponseMessageHandler {
    private List<IResponseHandlerObserver> observers;
    private static ResponseHandler instance;

    private ResponseHandler() {
        observers = new ArrayList<>();
    }

    public static ResponseHandler newInstance() {
        if(instance == null) {
            instance = new ResponseHandler();
        }
        return instance;
    }

    @Override
    public void HandleMessage(BaseResponse response) {
       for(IResponseHandlerObserver observer : observers) {
           observer.HandleResponse(response);
       }
    }

    public void addObserver(IResponseHandlerObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IResponseHandlerObserver observer) {
        observers.remove(observer);
    }
}
