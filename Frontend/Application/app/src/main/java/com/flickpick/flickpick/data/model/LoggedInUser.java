package com.flickpick.flickpick.data.model;

import android.graphics.Bitmap;

import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.util.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository.
 * Client side only methods.
 * @author Dustin Cornelison
 */
public class LoggedInUser {
	private static final String DEFAULT_USERNAME = AppController.getInstance().getString(R.string.default_username);
	private static final String DEFAULT_EMAIL = AppController.getInstance().getString(R.string.default_email);
	private final ArrayList<OnUserChangedListener> listeners;
	
	
	private final int userId; //cannot change
	private String displayName;
	private String email = DEFAULT_EMAIL;
	private UserType userType = UserType.Guest;
	private int favMov = -1;
	private String pass;
	private Bitmap pfp;
	
	/**
	 * Creates a new User object with the given id and name
	 * @param userId the id for the user
	 * @param displayName the user's name
	 */
	public LoggedInUser(int userId, String displayName) {
		this.userId = userId;
		this.displayName = displayName;
		if (userId == -1) userType = UserType.Guest;
		listeners = new ArrayList<>();
	}
	
	/**
	 * @return this user's id
	 */
	public int getUserId() {
		return userId;
	}
	
	/**
	 * @return this user's name
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * @return this user's name. If the logged in user is an admin, will also append the users' id
	 */
	public String getNameAndId() {
		LoginRepository r = LoginRepository.getInstance();
		if (r != null && r.isLoggedIn() && r.getUser().getUserType().getPrivilege() > LoggedInUser.UserType.User.getPrivilege()) {
			return displayName + "#" + userId;
		}
		return displayName;
	}
	
	/**
	 * @return this user's email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @return this user's password
	 */
	public String getPassword() {
		return pass;
	}
	
	/**
	 * @return this user's UserType (Guest/User/Admin)
	 */
	public UserType getUserType() {
		return userType;
	}
	
	/**
	 * @return this user's favorite movie, by id
	 */
	public int getFavMov() {
		return favMov;
	}
	
	/**
	 * @return this user's profile picture
	 */
	public Bitmap getProfilePicture() {
		return pfp;
	}
	
	/**
	 * Sets the users attributes, usually after a request to change or get the user.
	 * @param userType the user type, defined as one of the types found in the {@code UserType} enum
	 * @param movie the user's favorite movie, by id
	 */
	public void setUserAttributes(String username, String email, String password, String userType, int movie) {
		displayName = (username == null || username.trim().equals("")) ? DEFAULT_USERNAME : username;
		this.userType = UserType.fromString(userType);
		this.email = getUserType() == UserType.Guest ?
			AppController.getInstance().getString(R.string.guest_sign_in) :
			(email == null || email.trim().equals("")) ? DEFAULT_EMAIL : email;
		
		favMov = movie;
		pass = password;
		for (OnUserChangedListener l : listeners) l.userChanged(this);
	}
	
	public void updateUserFromJson(JSONObject response) throws JSONException {
		setUserAttributes(
			response.getString("username"),
			response.getString("email"),
			response.getString("password"),
			response.getString("userType"),
			response.getInt("favMID"));
	}
	
	/**
	 * Sets the user's profile picture
	 * @param b bitmap to set the image to
	 */
	public void setProfilePic(Bitmap b) {
		pfp = b;
		for (OnUserChangedListener l : listeners) l.userChanged(this);
	}
	
	/**
	 * Adds a listener for when the current logged in user changes.
	 * @param l the listener to add
	 */
	public void addOnUserChangedListener(OnUserChangedListener l) {
		listeners.add(l);
	}
	
	/**
	 * Removes a listener for user changing.
	 * @param l the listener to remove
	 */
	public void removeOnUserChangedListener(OnUserChangedListener l) {
		listeners.remove(l);
	}
	
	/**
	 * An enum for a user's type. Can convert to and from {@code String}s and {@code int}s.
	 * <pre>
	 * Key:  String     int
	 *       Guest      0     (default)
	 *       User       1
	 *       Admin      2
	 * </pre>
	 */
	public enum UserType {
		Guest(0),
		User(1),
		Admin(2);
		
		int privilege;
		UserType(int p) {
			privilege = p;
		}
		
		/**
		 * Converts a {@code String} to a {@code UserType}.
		 * Defaults to {@code Guest} if the input is not recognized.
		 * @param s The string to convert from
		 * @return the UserType associated with {@code s}
		 */
		public static UserType fromString(String s) {
			switch (s.toLowerCase()) {
				case "user": return User;
				case "admin": return Admin;
				default: return Guest;
			}
		}
		
		/**
		 * Converts an {@code int} to a {@code UserType} (based on privilege level)
		 * @param i user privilege
		 * @return UserType with give privilege
		 */
		public static UserType fromInt(int i) {
			switch (i) {
				case 1: return User;
				case 2: return Admin;
				default: return Guest;
			}
		}
		
		/**
		 * @return privilege level for this UserType
		 */
		public int getPrivilege() {
			return privilege;
		}
	}
}

