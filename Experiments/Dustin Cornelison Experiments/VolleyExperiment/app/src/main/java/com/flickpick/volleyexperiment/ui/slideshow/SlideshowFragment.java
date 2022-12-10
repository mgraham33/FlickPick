package com.flickpick.volleyexperiment.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flickpick.volleyexperiment.Controller;
import com.flickpick.volleyexperiment.databinding.FragmentSlideshowBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {
	
	private FragmentSlideshowBinding binding;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		SlideshowViewModel slideshowViewModel =
			new ViewModelProvider(this).get(SlideshowViewModel.class);
		
		binding = FragmentSlideshowBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		
		final TextView textView = binding.textSlideshow;
		
		JsonObjectRequest jOR = new JsonObjectRequest(Request.Method.GET, "http://ip.jsontest.com/", null, (JSONObject response) -> {
			String cT;
			String aL;
			String h;
			String t;
			try {
				cT = response.getString("Content-Type");
			} catch (JSONException e) {
				cT = "Error: Content-Type not fetched!";
			}
			try {
				aL = response.getString("Accept-Language");
			} catch (JSONException e) {
				aL = "Error: Accept-Language not fetched!";
			}
			try {
				h = response.getString("Host");
			} catch (JSONException e) {
				h = "Error: Host not fetched!";
			}
			try {
				t = response.getString("Time");
			} catch (JSONException e) {
				t = "Error: Time not fetched!";
			}
			textView.setText(String.format("Raw JSON:\n%s\nParsed:\nContent-Type: %s\nAccept-Language: %s\nHost: %s\nTime: %s", response, cT, aL, h, t));
		}, (VolleyError error) -> {
			VolleyLog.d(SlideshowFragment.class.getSimpleName(), "Error: " + error.getMessage());
			textView.setText("There was a problem!");
		}) {
			/**
			 * Passing some request headers
			 * */
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Content-Type", "application/json");
				//Taken from jsontest.com, but modified slightly
				headers.put("Accept-Language", "en-US");
				headers.put("Host", "headers.jsontest.com");
				
				headers.put("Time", String.valueOf(System.currentTimeMillis()));
				
				return headers;
			}
			
		};
		//jOR.setTag("")
		RequestQueue queue = Controller.getInstance().getRequestQueue();
		queue.add(jOR);
		
		return root;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
}