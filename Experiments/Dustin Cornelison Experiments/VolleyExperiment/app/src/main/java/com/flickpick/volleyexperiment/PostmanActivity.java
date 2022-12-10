package com.flickpick.volleyexperiment;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flickpick.volleyexperiment.ui.slideshow.SlideshowFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.flickpick.volleyexperiment.ui.main.SectionsPagerAdapter;
import com.flickpick.volleyexperiment.databinding.ActivityPostmanBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PostmanActivity extends AppCompatActivity {
	private ActivityPostmanBinding binding;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityPostmanBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
		ViewPager viewPager = binding.viewPager;
		viewPager.setAdapter(sectionsPagerAdapter);
		TabLayout tabs = binding.tabs;
		tabs.setupWithViewPager(viewPager);
		FloatingActionButton fab = binding.fab;
		TextView label = binding.title;
		
		tabs.getTabAt(0).setId(0); //GET tab
		tabs.getTabAt(1).setId(1); //POST tab
		
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
					.setAction("Action", null).show();
			}
		});
		
		tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				sectionsPagerAdapter.changeTab(tab.getId());
			}
			
			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			
			}
			
			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				sectionsPagerAdapter.changeTab(tab.getId());
			}
		});
		
		label.setText("Postman Tests");
	}
}