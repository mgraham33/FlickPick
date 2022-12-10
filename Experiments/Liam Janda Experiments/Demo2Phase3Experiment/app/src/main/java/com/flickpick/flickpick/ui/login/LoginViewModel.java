package com.flickpick.flickpick.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.Result;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.R;

import java.util.Locale;

public class LoginViewModel extends ViewModel {
	
	private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
	private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
	private LoginRepository loginRepository;
	
	
	LoginViewModel(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
	LiveData<LoginFormState> getLoginFormState() {
		return loginFormState;
	}
	
	LiveData<LoginResult> getLoginResult() {
		return loginResult;
	}
	
	public void login(String username, String password) {
		// can be launched in a separate asynchronous job
		Result<LoggedInUser> result = loginRepository.login(username, password);
		
		if (result instanceof Result.Success) {
			LoggedInUser data = ((Result.Success<LoggedInUser>)result).getData();
			loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
		} else {
			loginResult.setValue(new LoginResult(R.string.login_failed));
		}
	}
	
	public void loginDataChanged(String username, String password) {
		CheckResults name = usernameCheck(username);
		CheckResults email = emailCheck(username);
		CheckResults pass = passwordCheck(password);
		
		loginFormState.setValue(new LoginFormState(name.reasonID, email.reasonID, pass.reasonID));
	}
	
	// A placeholder username validation check
	private CheckResults usernameCheck(String username) {
		if (username == null || username.length() < 3) {
			return new CheckResults(false, R.string.invalid_username_short);
		}
		if (username.contains("@")) {
			return new CheckResults(false, R.string.invalid_username_email);
		} else {
			String ch = username.toLowerCase(Locale.ROOT).replaceAll("[a-z]|[0-9]|_", "");
			if (ch.length() == 0) return new CheckResults(true, -1);
			return new CheckResults(false, R.string.invalid_username_character, ch.toCharArray());
		}
	}
	
	private CheckResults emailCheck(String email) {
		if (email == null) return new CheckResults(false, R.string.invalid_email);
		if (email.contains("@")) return new CheckResults(Patterns.EMAIL_ADDRESS.matcher(email).matches(),
														 R.string.invalid_email);
		return new CheckResults(false, R.string.invalid_email);
	}
	
	// A placeholder password validation check
	private CheckResults passwordCheck(String password) {
		if (password == null || password.length() < 6) return new CheckResults(false, R.string.invalid_password_short);
		if (password.charAt(0) == ' ' || password.charAt(password.length() - 1) == ' ')
			return new CheckResults(false, R.string.invalid_password_space);
		if (password.replaceAll("[a-z]", "").length() == password.length() ||
			password.replaceAll("[A-Z]", "").length() == password.length() ||
			password.replaceAll("[0-9]", "").length() == password.length() ||
			password.replaceAll("[a-z]|[A-Z]|[0-9]", "").length() == 0) {
			return new CheckResults(false, R.string.invalid_password_weak);
		}
		return new CheckResults(true, -1);
	}
	
	//Used to return whether something passes a check, and the reason if not.
	public static class CheckResults {
		private final boolean passes;
		private final int reasonID;
		private final char[] args;
		
		public CheckResults(boolean passes, int reason, char... args) {
			this.passes = passes;
			this.reasonID = reason;
			this.args = args;
		}
	}
}