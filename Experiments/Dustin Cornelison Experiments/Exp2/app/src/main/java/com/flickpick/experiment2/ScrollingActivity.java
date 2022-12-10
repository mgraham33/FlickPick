package com.flickpick.experiment2;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.flickpick.experiment2.databinding.ActivityScrollingBinding;

public class ScrollingActivity extends AppCompatActivity {
	private boolean favorited = false;
	private ActivityScrollingBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityScrollingBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Toolbar toolbar = binding.toolbar;
		setSupportActionBar(toolbar);
		CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
		toolBarLayout.setTitle(getTitle());
		
		FloatingActionButton fab = binding.fab;
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (!favorited) {
					Snackbar.make(view, "Added to favorites", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
					fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_24));
				} else {
					Snackbar.make(view, "Removed from favorites", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
					fab.setImageDrawable(getDrawable(R.drawable.ic_baseline_star_border_24));
				}
				favorited = !favorited;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_scrolling, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent i = new Intent(ScrollingActivity.this, SettingsActivity.class);
			startActivity(i);
			return true;
		}
		if (id == R.id.action_exit) {
			new ExitDialogFragment().show(getSupportFragmentManager(), "exit");
		}
		return super.onOptionsItemSelected(item);
	}
}