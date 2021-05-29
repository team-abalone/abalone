package com.teamabalone.abalone;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.teamabalone.abalone.Client.ICoreLauncher;
import com.teamabalone.abalone.Client.ICoreResponseMessageHandler;
import com.teamabalone.abalone.Services.ResponseHandlerService;

import java.net.Socket;

public class AndroidLauncher extends AndroidApplication implements ICoreLauncher {
	private ResponseHandlerService mService;
	private Socket Socket;
	private ICoreResponseMessageHandler CoreResponseMessageHandler;
	private boolean mBound = false;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new GameImpl(this), config);

		// Starting tcp listener service.
		// Warning: I am using some black magic here to make this work.
		Intent intent = new Intent(this, ResponseHandlerService.class);
		startService(intent);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public void setICoreResponseMessageHandler(ICoreResponseMessageHandler mh) {
		if(mService != null) {
			mService.setICoreResponseMessageHandler(mh);
		}
		else {
			CoreResponseMessageHandler = mh;
		}
	}

	@Override
	public void setSocket(Socket socket) {
		if(mService != null) {
			mService.setSocket(socket);
		}
		else {
			Socket = socket;
		}
	}

	/**
	 * Can be used by the core module to retrieve the currently
	 * last commit hash.
	 * @return The currently last commit hash.
	 */
	@Override
	public String getCommitHash() {
		return BuildConfig.GitHash;
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
									   IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance
			ResponseHandlerService.LocalBinder binder = (ResponseHandlerService.LocalBinder) service;
			mService = binder.getService();
			mBound = true;

			// In case we already got the socket and ResponseHandler before, we pass it on to the service.
			if(Socket != null) {
				mService.setSocket(Socket);
			}

			if(CoreResponseMessageHandler != null) {
				mService.setICoreResponseMessageHandler(CoreResponseMessageHandler);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};
}
