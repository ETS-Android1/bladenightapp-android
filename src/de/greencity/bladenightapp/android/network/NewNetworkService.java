package de.greencity.bladenightapp.android.network;

import de.tavendo.autobahn.Wamp;
import de.tavendo.autobahn.WampConnection;
import de.tavendo.autobahn.WampOptions;
import de.tavendo.autobahn.WebSocketOptions;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class NewNetworkService extends Service {
	private final String TAG = "NewNetworkService";
	private WampConnection wampConnection;

	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		connect();
		registerReceivers();
	}

	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		unregisterReceivers();
	}


	@Override
	public void onRebind(Intent intent) {
		Log.i(TAG, "onRebind");
		super.onRebind(intent);
	}


	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "onCreate");
		super.onStart(intent, startId);
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onCreateCommand");
		return super.onStartCommand(intent, flags, startId);
	}


	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "onUnbind");
		return super.onUnbind(intent);
	}


	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return new Binder();
	}

	private void registerReceivers() {
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(Actions.GET_ALL_EVENTS);
		registerReceiver(getAllEventsReceiver, filter2);
	}

	private void unregisterReceivers() {
		unregisterReceiver(getAllEventsReceiver);
	}

	void connect() {
		final String uri = "ws://192.168.178.30:8081";
		Log.i(TAG, "Connecting to: " + uri);

		Wamp.ConnectionHandler handler  = new Wamp.ConnectionHandler() {
			@Override
			public void onOpen() {
				Log.d(TAG, "Status: Connected to " + uri);
				sendBroadcast(new Intent(Actions.CONNECTED));
			}

			@Override
			public void onClose(int code, String reason) {
				Log.d(TAG, "Connection lost to " + uri);
				Log.d(TAG, "Reason:" + reason);
				sendBroadcast(new Intent(Actions.DISCONNECTED));
			}

		};
		wampConnection = new WampConnection();
		WampOptions wampOptions = new WampOptions();

		// Default options, copied from de/tavendo/autobahn/WampConnection.java
		wampOptions.setReceiveTextMessagesRaw(true);
		wampOptions.setMaxMessagePayloadSize(64*1024);
		wampOptions.setMaxFramePayloadSize(64*1024);
		wampOptions.setTcpNoDelay(true);

		// Our own options:
		wampOptions.setReconnectInterval(5000);

		wampConnection.connect(uri, handler, wampOptions);
	}

	private final BroadcastReceiver getAllEventsReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"getAllEventsReceiver.onReceive");
		}
	};



}
