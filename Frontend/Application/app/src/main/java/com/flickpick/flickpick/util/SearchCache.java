package com.flickpick.flickpick.util;

import android.content.Intent;

import java.util.Stack;

/**
 * Stores the search results for previous searches.
 *
 * @author Dustin Cornelison
 */
public class SearchCache {
	private final Stack<Intent> searchResults;
	
	public SearchCache() {
		searchResults = new Stack<>();
	}
	
	/**
	 * Adds a search to the cache.
	 * @param data the intent with the search result data
	 * @return true if adding was successful, false otherwise. It is advisable
	 * to give a notification to the user when this returns false, as it should
	 * normally only happen if they do a ton of searches in a row.
	 */
	public boolean addSearch(Intent data) {
		try {
			searchResults.add(data);
			return true;
		} catch (StackOverflowError e) {
			return false;
		}
	}
	
	/**
	 * Removes the most recent search results and returns them.
	 * @return the most recent search results
	 */
	public Intent back() {
		if (searchResults.empty()) return null;
		return searchResults.pop();
	}
}
