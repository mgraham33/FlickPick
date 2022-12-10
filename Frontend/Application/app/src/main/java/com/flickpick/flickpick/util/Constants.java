package com.flickpick.flickpick.util;

/**
 * Data class for constants, mainly URLs.
 *
 * @author Dustin Cornelison
 * @author Liam Janda
 */
public class Constants {
	private static final String BASE = "coms-309-017.class.las.iastate.edu:8080/";
	public static final String BASE_URL = "http://" + BASE;
	public static final String USERS_URL = BASE_URL + "users";
	public static final String USERS_BASE_URL = BASE_URL + "users/";
	public static final String FRIENDS_RELATIONS_URL = BASE_URL +  "friends";
	public static final String FRIENDS_RELATIONS_BASE_URL = BASE_URL + "friends/";
	public static final String FRIENDS_BASE_URL = BASE_URL + "friends/list/";
	public static final String UNIQUE_GENRES_URL = BASE_URL + "genres/unique";
	public static final String GENRES_URL = BASE_URL + "genres";
	public static final String GENRES_BASE_URL = BASE_URL + "genres/";
	public static final String MOVIES_BASE_URL = BASE_URL + "movies/";
	public static final String GENRE_MOVIES_BASE_URL = BASE_URL + "genres/genre/";
	public static final String REVIEWS_URL = BASE_URL + "reviews";
	public static final String MOVIE_REVIEWS_BASE_URL = BASE_URL + "reviews/movie/";
	public static final String BASE_WEBSOCKET = "ws://" + BASE;
	public static final String SEARCH_SUGGEST_WEBSOCKET = BASE_WEBSOCKET + "search/suggestions";
	public static final String 	WS_CHAT_BASE_URL = BASE_WEBSOCKET + "chat/";



	//Note: use this variable where you need something different to happen for testing reasons
	//This should not remain in permanent use; whenever you can remove it, do so.
	//For example: if the server is broken, but you want to sign in, add this to the if
	//statement that allows you to sign in, but once the server works again,
	//remove it from the if statement.
	public static final boolean DEBUG = true;
}
