package com.flickpick.volleyexperiment.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flickpick.volleyexperiment.Controller;
import com.flickpick.volleyexperiment.databinding.FragmentHomeBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
	
	private FragmentHomeBinding binding;
	
	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {
		HomeViewModel homeViewModel =
			new ViewModelProvider(this).get(HomeViewModel.class);
		
		binding = FragmentHomeBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		
		final TextView textView = binding.textHome;
		
		JsonObjectRequest jOR = new JsonObjectRequest(Request.Method.GET, "http://ip.jsontest.com/", null, (JSONObject response) -> {
			String t;
			try {
				t = "Raw JSON:\n" + response + "\nParsed:\nYour IP address is " + response.getString("ip");
			} catch (JSONException e) {
				t = "Raw JSON:\n" + response + "\nParsed:\nYour IP address is " + e;
			}
			textView.setText(t);
		}, null);
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