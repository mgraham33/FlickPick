package com.flickpick.volleyexperiment;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Controller extends Application {
	private RequestQueue requestQueue;
	private static Controller instance;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	public static synchronized Controller getInstance() {
		return instance;
	}
	
	public RequestQueue getRequestQueue() {
		if (requestQueue == null) requestQueue = Volley.newRequestQueue(getApplicationContext());
		return requestQueue;
	}
	
	public void cancelPendingRequests(Object tag) {
		if (requestQueue != null) {
			requestQueue.cancelAll(tag);
		}
	}
	
}
