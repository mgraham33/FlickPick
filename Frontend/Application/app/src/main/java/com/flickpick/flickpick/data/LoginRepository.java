package com.flickpick.flickpick.data;

import androidx.annotation.Nullable;

import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.util.AppController;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 *
 * @author Dustin Cornelison
 */
public class LoginRepository {
	
	private static volatile LoginRepository instance;
	
	private LoginDataSource dataSource;
	
	private final int DUMMY_ID = -9;
	
	// If user credentials will be cached in local storage, it is recommended it be encrypted
	// @see https://developer.android.com/training/articles/keystore
	private LoggedInUser user = null;
	
	/**
	 * Private constructor: singleton access.
	 * @param dataSource the data source to link to
 	 */
	private LoginRepository(LoginDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Retrieves the instance of the {@code LoginRepository}, or creates one
	 * (with the given data source) if one doesn't exist yet.
	 * @param dataSource the data source to create a new repository with if required
	 * @return the instance of the repository
	 */
	public static LoginRepository getInstance(LoginDataSource dataSource) {
		if (instance == null) {
			instance = new LoginRepository(dataSource);
		}
		return instance;
	}
	
	/**
	 * Retrieves the instance of the {@code LoginRepository}.
	 * @return the instance of the repository, or {@code null} if one doesn't exist
	 */
	public static @Nullable LoginRepository getInstance() {
		return instance;
	}
	
	/**
	 * Returns whether a user is logged in.
	 * @return true if there is a signed-in user, false otherwise
	 */
	public boolean isLoggedIn() {
		return isLoggedIn(false);
	}
	
	/**
	 * Returns whether the user is logged in. Can also return false if the currently signed in user
	 * is a dummy. This should be used when making calls to the server. If {@code notDummy} is false,
	 * it has no effect and will just return whether a user is signed in.
	 * @param notDummy Whether you also want the user to not be a dummy.
	 * @return true if the user is signed in and (if {@code notDummy} is true) is not a dummy.
	 *         false if the user is not signed in or (if {@code notDummy} is true) the user is a dummy
	 */
	public boolean isLoggedIn(boolean notDummy) {
		return user != null && !(notDummy && user.getUserId() == DUMMY_ID);
	}
	
	/**
	 * Signs the user out.
	 */
	public void logout() {
		user = null;
		dataSource.logout();
		AppController.getInstance().loadSettings();
	}
	
	/**
	 * Creates a dummy login for testing purposes.
	 * @param type privilege level to sign in as
	 */
	public void dummyLogin(LoggedInUser.UserType type) {
		this.user = new LoggedInUser(DUMMY_ID, "Dummy");
		this.user.setUserAttributes("Dummy", "dummy@example.com", "aA.Aa6", type.name(), -1);
	}
	
	/**
	 * Sets the logged in user, essentially signing them in.
	 * @param user the user to sign in
	 */
	private void setLoggedInUser(LoggedInUser user) {
		this.user = user;
		AppController.getInstance().loadSettings();
		
		// If user credentials will be cached in local storage, it is recommended it be encrypted
		// @see https://developer.android.com/training/articles/keystore
	}
	
	/**
	 * Retrieves the signed in user.
	 * @return the currently signed in user.
	 */
	public LoggedInUser getUser() {
		return user;
	}
	
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
		// handle login
		Result result = dataSource.login(username, password);
		if (result instanceof Result.Success) {
			setLoggedInUser(((Result.Success<LoggedInUser>)result).getData());
		}
		return result;
	}
}