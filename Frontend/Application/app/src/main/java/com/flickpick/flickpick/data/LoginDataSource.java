package com.flickpick.flickpick.data;

import static com.flickpick.flickpick.util.Constants.BASE_URL;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 *
 * @author Dustin Cornelison
 */
@SuppressWarnings("ALL")
public class LoginDataSource {
	private static final int UNKNOWN = -1;
	private static final int SUCCESS = 0;
	private static final int INVALID = 1;
	private static final int JSON_ERROR = 2;
	private static final int REQUEST_ERROR = 4;
	private static int result = -1;
	
	/**
	 * Attempts login with the given credentials.
	 * <pre>
	 * Warning: This method will pause execution until the request is finished.
	 * DO NOT CALL THIS METHOD ON THE MAIN THREAD!
	 * </pre>
	 * @param username the username to attempt login with
	 * @param password the password to attempt login with
	 * @return a result object that denotes success
	 * (will contain the {@code LoggedInUser} object) or failure
	 * @see Result
	 * @see LoggedInUser
	 */
	public Result login(String username, String password) {
		result = UNKNOWN;
		String url = BASE_URL + "users/" + username + "/" + AppController.HTTPEncode(password);
		System.out.println("Trying signin on url: " + url);
		//AtomicReference so it can be modified inside the JSON request
		AtomicReference<LoggedInUser> user = new AtomicReference<>();
		JsonObjectRequest signInRequest = new JsonObjectRequest(Request.Method.GET,
			url, null,
			response -> {
				try {
					if (response.getString("message").equals("success")) {
						int id = response.getInt("id");
						user.set(new LoggedInUser(id, username));
						result = SUCCESS;
					} else result = INVALID;
				} catch (JSONException e) {
					e.printStackTrace();
					result = JSON_ERROR;
				}
				Log.d("LoginDataSource", response.toString());
			}, error -> {
			VolleyLog.d("Error: " + error.getMessage());
			result = REQUEST_ERROR;
		});
		AppController.getInstance().addToRequestQueue(signInRequest);
		
		try {
			//pause thread while waiting for response
			while (!signInRequest.hasHadResponseDelivered() && result == UNKNOWN) {
				Thread.sleep(100);
			}
			LoggedInUser rUser = user.get();
			if (rUser != null) {
				result = UNKNOWN;
				JsonObjectRequest getPfp = new JsonObjectRequest(Request.Method.GET,
                    Constants.BASE_URL + "users/" + rUser.getUserId() + "/pfp", null, response -> {
						try {
							rUser.setProfilePic(AppController.base64ToBitmap(response.getString("pfp")));
						} catch (JSONException e) {
							Toast.makeText(AppController.getInstance().getCurrentContext(),
							               R.string.fetch_pfp_error, Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}, error -> {
						Toast.makeText(AppController.getInstance().getCurrentContext(),
						               R.string.fetch_pfp_error, Toast.LENGTH_SHORT).show();
						error.printStackTrace();
					});
				
				JsonObjectRequest getUser = new JsonObjectRequest(Request.Method.GET,
                        Constants.BASE_URL + "users/" + rUser.getUserId(), null, response -> {
							try {
								rUser.updateUserFromJson(response);
								result = SUCCESS;
							} catch (JSONException e) {
								result = JSON_ERROR;
								e.printStackTrace();
							}
						}, error -> {
							result = REQUEST_ERROR;
							error.printStackTrace();
						});
				AppController.getInstance().addToRequestQueue(getPfp);
				AppController.getInstance().addToRequestQueue(getUser);
				while (!getUser.hasHadResponseDelivered() && result == UNKNOWN) {
					Thread.sleep(100);
				}
				if (result == SUCCESS) return new Result.Success<>(rUser);
			}
			if (result == INVALID)
				return new Result.Error(new IOException("Credentials incorrect"),
										R.string.invalid_credentials, result);
			if (result == REQUEST_ERROR)
				return new Result.Error(new IOException("Error logging in: check your internet connection"),
				                        R.string.no_internet, result);
			if (result == JSON_ERROR)
				return new Result.Error(new IOException("Error logging in: could not parse server response"),
				                        R.string.server_error_json_short, result);
			else return new Result.Error(new IOException("Error logging in. Code: " + result),
										 R.string.login_failed, result);
		} catch (Exception e) {
			e.printStackTrace();
			return new Result.Error(new IOException("Exception while logging in", e),
									R.string.login_failed, result);
		}
	}
	
	/**
	 * Doesn't do anything (yet).
	 * Reserved for if we do sessions
	 */
	public void logout() {
		// revoke authentication (if we do sessions)
	}
}