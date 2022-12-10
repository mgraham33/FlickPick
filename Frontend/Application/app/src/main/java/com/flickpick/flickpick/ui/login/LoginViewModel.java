package com.flickpick.flickpick.ui.login;

import static com.flickpick.flickpick.util.StringChecker.*;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.Result;
import com.flickpick.flickpick.data.model.LoggedInUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The view model for the login activity. Manages the form.
 *
 * @author Dustin Cornelison
 */
public class LoginViewModel extends ViewModel {
	
	private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
	private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
	private LoginRepository loginRepository;
	
	/**
	 * Creates the view model with the given repository.
	 * @param loginRepository the repository to link to the view model
	 */
	LoginViewModel(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	
	/**
	 * @return a live data form state, which can be observed and acted upon
	 * when changed.
	 */
	LiveData<LoginFormState> getLoginFormState() {
		return loginFormState;
	}
	
	/**
	 * @return a live data login result, which can be observed and acted upon
	 * when changed.
	 */
	LiveData<LoginResult> getLoginResult() {
		return loginResult;
	}
	
	/**
	 * Attempts login with the given credentials. Launches the login attempt
	 * in an asynchronous thread.
	 * @param username the username to attempt login with
	 * @param password the password to attempt login with
	 */
	public void login(String username, String password) {
		//launch in a separate asynchronous job
		ExecutorService login = Executors.newSingleThreadExecutor();
		login.execute(() -> {
			Result result = loginRepository.login(username, password);
			if (result instanceof Result.Success) {
				LoggedInUser data = ((Result.Success<LoggedInUser>)result).getData();
				loginResult.postValue(new LoginResult(data));
			} else {
				Result.Error data = ((Result.Error)result);
				loginResult.postValue(new LoginResult(data.getMessage(), data.getType()));
			}
		});
	}
	
	/**
	 * Checks the given username and password for incorrect inputs (via {@code CheckResults}).
	 * Call this when the login data is edited so that it can be checked.
	 * @param username the currently input username
	 * @param password the currently input password
	 *
	 * @see CheckResults
	 */
	public void loginDataChanged(String username, String password) {
		CheckResults name = usernameCheck(username);
		CheckResults pass = passwordCheck(password);
		
		loginFormState.setValue(new LoginFormState(name.getReason(), pass.getReason(), name.getArgsAsString()));
	}
}