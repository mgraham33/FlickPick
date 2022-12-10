package com.flickpick.flickpick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.flickpick.flickpick.databinding.ActivityDebugBinding;
import com.flickpick.flickpick.databinding.ActivityLoginBinding;
import com.flickpick.flickpick.ui.movie.HomeScreenActivity;
import com.flickpick.flickpick.ui.profile.UserProfileActivity;

public class Debug extends AppCompatActivity {
	private ActivityDebugBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityDebugBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Button accBtn = binding.debugBtnAcc;
		Button homeBtn = binding.debugBtnHome;
		
		accBtn.setOnClickListener(view -> {
			Intent i = new Intent(Debug.this, UserProfileActivity.class);
			startActivity(i);
		});

		homeBtn.setOnClickListener(view -> {
			Intent i = new Intent(Debug.this, HomeScreenActivity.class);
			startActivity(i);
		});

	}
}