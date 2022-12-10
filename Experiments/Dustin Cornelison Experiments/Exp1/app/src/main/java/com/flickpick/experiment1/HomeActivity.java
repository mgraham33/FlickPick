package com.flickpick.experiment1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
	Button b1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		b1 = findViewById(R.id.buttonBack);
		
		b1.setOnClickListener((View view) -> {
			Intent i = new Intent(HomeActivity.this, MainActivity.class);
			startActivity(i);
		} );
	}
}