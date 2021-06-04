package com.teamabalone.abalone.Client;

import com.teamabalone.abalone.Client.Responses.BaseResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;


public class ResponseHandler implements ICoreResponseMessageHandler {
    private CopyOnWriteArrayList<IResponseHandlerObserver> observers;
    private static ResponseHandler instance;

    private ResponseHandler() {
        observers = new CopyOnWriteArrayList<>();
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
