package com.flickpick.flickpick.data.model;

/**
 * Simple interface that responds to the logged in user being changed
 * @author Dustin Cornelison
 */
public interface OnUserChangedListener {
	/**
	 * Called when the user is updated.
	 * @param to the logged in user, for convenience
	 */
	void userChanged(LoggedInUser to);
}
