package com.flickpick.volleyexperiment.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.volleyexperiment.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class PageViewModel extends ViewModel {
	private final String mockURL = "https://54576f5e-f0d9-4208-a744-f5898018d362.mock.pstmn.io/%s";
	private  String toApply = "Loading...";
	
	//TIP of the day: do not create variables for JsonObjectRequests. If you plan on reusing
	//(requesting again), it's more reliable to just call 'new' every time.
	
	private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
	private LiveData<String> mText = Transformations.map(mIndex, input -> {
		if (input == 1) {
			System.out.println("Input 1");
		}
		if (input == 2) {
			System.out.println("Input 2");
		}
		return toApply;
	});
	
	public void setIndex(int index) {
		setIndex(index, true);
	}
	public void setIndex(int index, boolean cancel) {
		System.out.println("setIndex: " + index);
		if (cancel) Controller.getInstance().cancelPendingRequests("tab");
		RequestQueue queue = Controller.getInstance().getRequestQueue();
		System.out.println("q: " + queue.getCache());
		
		
		if (index == 1) {
			JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET, String.format(mockURL, "user/123"), null, (JSONObject response) -> {
				System.out.println("Response");
				
				String id;
				String username;
				String email;
				try {
					id = response.getString("id");
				} catch (JSONException e) {
					id = "Error: id not fetched!";
				}
				try {
					username = response.getString("username");
				} catch (JSONException e) {
					username = "Error: username not fetched!";
				}
				try {
					email = response.getString("email");
				} catch (JSONException e) {
					email = "Error: email not fetched!";
				}
				finishRequest(String.format("Raw JSON:\n%s\nParsed:\nUser ID: %s\nUsername: %s\nEmail: %s", response, id, username, email));
			}, (VolleyError error) -> {
				VolleyLog.d(PageViewModel.class.getSimpleName(), "Error: " + error.getMessage());
				finishRequest("There was a problem!");
			});
			getUser.setTag("tab");
			queue.add(getUser);
		} else if (index == 2) {
			JsonObjectRequest postUser = new JsonObjectRequest(Request.Method.POST, String.format(mockURL, "user/456"), null, (JSONObject response) -> {
				System.out.println("Post");
				
				String id;
				String username;
				String email;
				try {
					id = response.getString("id");
				} catch (JSONException e) {
					id = "Error: id not fetched!";
				}
				try {
					username = response.getString("username");
				} catch (JSONException e) {
					username = "Error: username not fetched!";
				}
				try {
					email = response.getString("email");
				} catch (JSONException e) {
					email = "Error: email not fetched!";
				}
				finishRequest(String.format("Raw JSON:\n%s\nParsed:\nUser ID: %s\nUsername: %s\nEmail: %s", response, id, username, email));
			}, (VolleyError error) -> {
				VolleyLog.d(PageViewModel.class.getSimpleName(), "Error: " + error.getMessage());
				finishRequest("There was a problem!");
			}) {
				@Override
				public byte[] getBody() {
					return "{\"username\":\"John\", \"email\":\"johnsmith@example.com\"}".getBytes(StandardCharsets.UTF_8);
				}
			};
			postUser.setTag("tab");
			queue.add(postUser);
		}
		toApply = "Loading...";
		mIndex.setValue(index);
	}
	
	private void finishRequest(String result) {
		toApply = result;
		mIndex.setValue(0);
		System.out.println("Finish: " + result);
	}
	
	public LiveData<String> getText() {
		return mText;
	}
}