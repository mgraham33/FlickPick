package com.flickpick.flickpick.ui.login;

import androidx.annotation.Nullable;

/**
 * Data validation state of the login form.
 */
class LoginFormState {
	
	private Integer usernameError;
	
	private Integer emailError;
	
	private Integer passwordError;
	
	LoginFormState(Integer usernameError, Integer emailError, Integer passwordError) {
		this.usernameError = usernameError;
		this.emailError = emailError;
		this.passwordError = passwordError;
	}
	
	Integer getUsernameError() {
		return usernameError;
	}
	
	Integer getEmailError() {
		return emailError;
	}
	
	Integer getPasswordError() {
		return passwordError;
	}
}