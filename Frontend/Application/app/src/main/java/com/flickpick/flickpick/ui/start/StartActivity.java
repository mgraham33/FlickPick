package com.flickpick.flickpick.ui.start;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginDataSource;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.Result;
import com.flickpick.flickpick.databinding.ActivityStartBinding;
import com.flickpick.flickpick.ui.login.LoginActivity;
import com.flickpick.flickpick.ui.signup.SignupActivity;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.ErrorDialogFragment;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The start screen for the application.
 */
public class StartActivity extends AppCompatActivity {
	ActivityStartBinding binding;
	
	/**
	 * Creates the view and manages UI.
	 * @param savedInstanceState previous instance, if saved
	 * @see androidx.fragment.app.FragmentActivity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivityStartBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Button login = binding.startSignIn;
		Button signup = binding.startReg;
		
		login.setOnClickListener(view -> {
			Intent i = new Intent(StartActivity.this, LoginActivity.class);
			startActivity(i);
		});
		
		signup.setOnClickListener(view -> {
			Intent i = new Intent(StartActivity.this, SignupActivity.class);
			startActivity(i);
		});
		
		binding.startGuest.setOnClickListener(view -> {
			//It is not guaranteed that the user opened the login page, so
			//create the repository instance if needed.
			binding.startSpinner.setVisibility(View.VISIBLE);
			LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());
			ExecutorService guest = Executors.newSingleThreadExecutor();
			guest.execute(() -> {
				//TODO: get guest user from server instead of login?
				Result result = loginRepository.login("guest", "1234567890");
				if (result instanceof Result.Success) {
					Intent i = new Intent(StartActivity.this, NavigationActivity.class);
					startActivity(i);
					runOnUiThread(() -> binding.startSpinner.setVisibility(View.GONE));
				} else {
					Result.Error data = ((Result.Error)result);
					runOnUiThread(() -> showGuestLoginError(getString(data.getMessage())));
				}
			});
		});
		
		//Get genres and services
		JsonArrayRequest getGenres = new JsonArrayRequest(Request.Method.GET, Constants.BASE_URL +
			"genres/unique", null, response -> {
			String[] genres = new String[response.length()];
			for (int i = 0; i < response.length(); i++) {
				try {
					genres[i] = response.getJSONObject(i).getString("genre");
				} catch (JSONException e) {
					showGenreError(getString(R.string.server_error_json, response));
				}
			}
			AppController.getInstance().setGenres(genres);
		}, error -> showGenreError(getString(R.string.server_error, String.valueOf(error.networkResponse.statusCode))));
		//TODO: add to serverside: getServices?
		//JsonArrayRequest getServices = new JsonArrayRequest(Request.Method.GET, Constants.BASE_URL +
		//	"services", null, response -> {
		//	String[] services = new String[response.length()];
		//	for (int i = 0; i < response.length(); i++) {
		//		try {
		//			services[i] = response.getString(i);
		//		} catch (JSONException e) {
		//			showServiceError(getString(R.string.server_error_json, response));
		//		}
		//	}
		//	AppController.getInstance().setServices(services);
		//}, error -> showServiceError(getString(R.string.server_error, String.valueOf(error.networkResponse.statusCode))));
		AppController.getInstance().addToRequestQueue(getGenres);
		//AppController.getInstance().addToRequestQueue(getServices);
		AppController.getInstance().setServices(new String[]{"Netflix", "Hulu", "HBO Max", "Disney+", "Amazon Prime Video"});
	}
	
	/**
	 * Since this is the first screen (unless debugging), prompt user to exit when pressing back.
	 * Do not ever allow user to go back to previous screen: logging out goes here, you shouldn't
	 * be able to "log back in" by pressing back.
	 */
	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
		builder.setMessage(R.string.exit_confirm_title)
		       .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int id) {
				       finishAffinity();
			       }
		       })
		       .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
			       public void onClick(DialogInterface dialog, int id) {
				       // User cancelled the dialog
			       }
		       }).show();
	}
	
	/**
	 * Show an error if logging in as a guest fails.
	 * @param msg the message to give the user
	 */
	private void showGuestLoginError(String msg) {
		showError(msg, R.string.guest_sign_in_error);
		binding.startSpinner.setVisibility(View.GONE);
	}
	
	/**
	 * Shows an error if there is a problem fetching the genres from the server.
	 * @param msg the message to give the user
	 */
	private void showGenreError(String msg) {
		showError(msg, R.string.genre_error_title);
	}
	
	/**
	 * Shows an error if there is a problem fetching the services from the server.
	 * @param msg the message to give the user
	 */
	private void showServiceError(String msg) {
		showError(msg, R.string.service_error_title);
	}
	
	/**
	 * Shows a generic error to the user with the given title and message.
	 * @param msg the message to give the user
	 * @param title the title of the message
	 */
	private void showError(String msg, int title) {
		ErrorDialogFragment e = new ErrorDialogFragment();
		e.setMessage(msg).setTitle(title)
		 .show(getSupportFragmentManager(), "Start");
	}
}