package com.flickpick.flickpick.ui.login;

import androidx.annotation.StringRes;

/**
 * Data validation state of the login form.
 *
 * @author Dustin Cornelison
 */
class LoginFormState {
	
	private Integer usernameError;
	private Integer passwordError;
	private String invalidChars;
	
	/**
	 * Creates a new form state with the given errors, if they exist.
	 * @param usernameError id of the string resource for the current problem
	 * with the user's username
	 * @param passwordError id of the string resource for the current problem
	 * with the user's password
	 * @param invChars list form of invalid character input into the username.
	 * Should be formatted how it is to be displayed to the user (inside quotes),
	 * i.e.
	 * <pre>a", "b", "c</pre>
	 * will be displayed as
	 * <pre>"a", "b", "c"</pre>
	 */
	LoginFormState(@StringRes Integer usernameError, @StringRes Integer passwordError, String invChars) {
		this.usernameError = usernameError;
		this.passwordError = passwordError;
		invalidChars = invChars;
	}
	
	/**
	 * @return the problem with the username
	 */
	@StringRes Integer getUsernameError() {
		return usernameError;
	}
	
	/**
	 * @return the problem with the password
	 */
	@StringRes Integer getPasswordError() {
		return passwordError;
	}
	
	/**
	 * @return all the invalid characters used in the username, formatted as a readable list
	 */
	String getInvalidChars() {
		return invalidChars;
	}
}