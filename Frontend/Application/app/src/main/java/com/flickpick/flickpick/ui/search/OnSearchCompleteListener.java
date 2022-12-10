package com.flickpick.flickpick.ui.search;

/**
 * Interface to provide functionality when a search has been completed.
 *
 * @author Dustin Cornelison
 */
public interface OnSearchCompleteListener {
	/**
	 * Called when the main results have been returned by the server.
	 * @param mainResults the main results, or null if not necessary
	 */
	void updateMainResults(Object[][] mainResults);
	
	/**
	 * Called when the secondary results have been returned by the server.
	 * @param secondaryResults the secondary results, or null if not necessary
	 */
	void updateSecondaryResults(Object[][] secondaryResults);
	
	/**
	 * Called when updating the main results fails. Used to display an error to the user.
	 */
	void updateMainFail();
	//TODO: allow special functions on failures?
}
