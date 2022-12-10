package com.flickpick.flickpick.ui.login;

import androidx.annotation.StringRes;

import com.flickpick.flickpick.data.model.LoggedInUser;

/**
 * Authentication result: success (user details) or error message.
 * Practically an interface for {@code Result}
 * @see com.flickpick.flickpick.data.Result
 *
 * @author Dustin Cornelison
 */
class LoginResult {
	private LoggedInUser success;
	private Integer error;
	private int code;
	
	/**
	 * Creates a new {@code LoginResult} with the given error,
	 * and the error code to display to the user.
	 * @param error the id of the error message to display
	 * @param code the error code to give the user
	 */
	LoginResult(@StringRes Integer error, int code) {
		this.error = error;
		this.code = code;
	}
	
	/**
	 * Creates a new {@code LoginResult} with the given user.
	 * @param success the user that should now be signed in
	 */
	LoginResult(LoggedInUser success) {
		this.success = success;
	}
	
	/**
	 * @return the signed in user
	 */
	LoggedInUser getSuccess() {
		return success;
	}
	
	/**
	 * @return the id of the error message to display
	 */
	@StringRes Integer getError() {
		return error;
	}
	
	/**
	 * @return the error code to display to the user
	 */
	int getCode() {
		return code;
	}
}