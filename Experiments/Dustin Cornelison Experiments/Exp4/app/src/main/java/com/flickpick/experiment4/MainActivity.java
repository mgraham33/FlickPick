package com.flickpick.experiment4;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.flickpick.experiment4.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.flickpick.experiment4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
	
	private AppBarConfiguration mAppBarConfiguration;
	private ActivityMainBinding binding;
	
	private String username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) username = extras.getString("username");
		
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		setSupportActionBar(binding.appBarMain.toolbar);
		binding.appBarMain.fab.setOnClickListener((View view) -> {
				signOut();
		});
		DrawerLayout drawer = binding.drawerLayout;
		NavigationView navigationView = binding.navView;
		TextView t = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
		String welcome = String.format(getResources().getString(R.string.nav_welcome), username);
		t.setText(welcome);
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		mAppBarConfiguration = new AppBarConfiguration.Builder(
			R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
			.setOpenableLayout(drawer)
			.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
			|| super.onSupportNavigateUp();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(i);
			return true;
		}
		if (id == R.id.signout) {
			signOut();
		}
		if (id == R.id.signout_quit) {
			signOut();
			System.exit(0);
		}
		if (id == R.id.quit) {
			new ExitDialogFragment().show(getSupportFragmentManager(), "exit");
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void signOut() {
		Toast.makeText(getApplicationContext(),
			getResources().getString(R.string.logout, username), Toast.LENGTH_SHORT).show();
		Intent i = new Intent(MainActivity.this, LoginActivity.class);
		i.putExtra("username", username);
		startActivity(i);
	}
	
}