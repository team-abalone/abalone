package com.teamabalone.abalone.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.teamabalone.abalone.Client.ICoreResponseMessageHandler;
import com.teamabalone.abalone.Client.Responses.BaseResponse;
import com.teamabalone.abalone.Client.Responses.CreateRoomResponse;

import com.teamabalone.abalone.Client.Responses.ExceptionResponse;
import com.teamabalone.abalone.Client.Responses.GameClosedResponse;
import com.teamabalone.abalone.Client.Responses.MadeMoveResponse;

import com.teamabalone.abalone.Client.Responses.GameStartedResponse;

import com.teamabalone.abalone.Client.Responses.ResponseCommandCodes;
import com.teamabalone.abalone.Client.Responses.RoomJoinedResponse;
import com.teamabalone.abalone.Client.Responses.StartGameResponse;
import com.teamabalone.abalone.Client.Responses.SurrenderResponse;
import com.teamabalone.abalone.Helpers.Helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

public class ResponseHandlerService extends Service {
    private ICoreResponseMessageHandler CoreResponseMessageHandler;
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;
    private Socket Socket;

    public void setICoreResponseMessageHandler(ICoreResponseMessageHandler mh) {
        CoreResponseMessageHandler = mh;
    }

    public void setSocket(Socket socket) {
        Socket = socket;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            while (true) {
                if (Socket != null) {

                    try {
                        InputStream is = Socket.getInputStream();

                        BufferedReader in =
                                new BufferedReader(
                                        new InputStreamReader(is));

                        String responseString = in.readLine();

                        // If this fails input isn't valid json.
                        new JsonParser().parse(responseString);

                        Gson gson = Helpers.GetGsonInstance();


                        if (CoreResponseMessageHandler != null) {
                            BaseResponse response = gson.fromJson(responseString, BaseResponse.class);

                            if(response.getCommandCode() == ResponseCommandCodes.ROOM_CREATED.getValue()) {
                                response = gson.fromJson(responseString, CreateRoomResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.ROOM_JOINED.getValue()) {
                                response = gson.fromJson(responseString, RoomJoinedResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.ROOM_JOINED_OTHER.getValue()) {
                                response = gson.fromJson(responseString, RoomJoinedResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.MADE_MOVE.getValue()) {
                                response = gson.fromJson(responseString, MadeMoveResponse.class);
                            }

                            else if(response.getCommandCode() == ResponseCommandCodes.GAME_STARTED.getValue()) {
                                response = gson.fromJson(responseString, GameStartedResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.GAME_CLOSED.getValue()){
                                response = gson.fromJson(responseString, GameClosedResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.SURRENDERED.getValue()){
                                response = gson.fromJson(responseString, SurrenderResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.SERVER_EXCEPTION.getValue()){
                                response = gson.fromJson(responseString, ExceptionResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.ROOM_EXCEPTION.getValue()){
                                response = gson.fromJson(responseString, ExceptionResponse.class);
                            }
                            else if(response.getCommandCode() == ResponseCommandCodes.GAME_EXCEPTION.getValue()){
                                response = gson.fromJson(responseString, ExceptionResponse.class);
                            }

                            if(response.getCommandCode() != 0) {
                                CoreResponseMessageHandler.HandleMessage(response);
                            }
                        }
                    } catch (IOException e) {
                        // Restore interrupt status.
                        Thread.currentThread().interrupt();
                    }
                    catch(JsonSyntaxException e) {
                        // Ignore message if server sent invalid json.
                    }
                }
            }

            //stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {
        // Start up the thread running the service. Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block. We also make it
        // background priority so CPU-intensive work doesn't disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    // Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ResponseHandlerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return ResponseHandlerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}