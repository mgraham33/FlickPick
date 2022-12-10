package com.flickpick.flickpick.ui.search;

import static android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.SearchRecentSuggestions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.flickpick.flickpick.R;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.Settings;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

public class SearchSuggestions extends SearchRecentSuggestionsProvider {
	//private WebSocketClient ws;
	public final static String AUTHORITY = "com.flickpick.SearchSuggestions";
	public final static int MODE = DATABASE_MODE_QUERIES;
	private static SearchSuggestions instance;
	
	public SearchSuggestions() {
		setupSuggestions(AUTHORITY, MODE);
		instance = this;
	}
	
	@Override
	public boolean onCreate() {
		//try {
		//	ws = new WebSocketClient(new URI(Constants.SEARCH_SUGGEST_WEBSOCKET)) {
		//		@Override
		//		public void onOpen(ServerHandshake serverHandshake) {
		//
		//		}
		//
		//		@Override
		//		public void onMessage(String s) {
		//
		//		}
		//
		//		@Override
		//		public void onClose(int i, String s, boolean b) {
		//
		//		}
		//
		//		@Override
		//		public void onError(Exception e) {
		//
		//		}
		//	};
		//} catch (URISyntaxException ignore) {}
		return super.onCreate();
	}
	
	/*
	Note: If your search suggestions aren't stored in a table format (such as an SQLite table)
	using the columns required by the system, then you can search your suggestion data for
	 matches and then format them into the necessary table on each request. To do so, create
	  a MatrixCursor using the required column names and then add a row for each suggestion
	   using addRow(Object[]). Return the final product from your Content Provider's query()
	    method.
	 */
	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		assert selectionArgs != null;
		Cursor recent = super.query(uri, projection, selection, selectionArgs, sortOrder);
		String query = selectionArgs[0];
		String[] columns = new String[]{"_ID", SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_ICON_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA};
		MatrixCursor c = new MatrixCursor(columns);
		if (!query.trim().equals("")) {
			Object[] r = new Object[]{"0", query, R.drawable.ic_baseline_search_24, query};
			c.addRow(r);
		}
		
		for (int i = 0; i < Settings.NUM_RECENT_SEARCHES.get(); i++) {
			if (!recent.moveToNext()) break;
			int id = recent.getInt(recent.getColumnIndexOrThrow("_id"));
			String suggest = recent.getString(recent.getColumnIndexOrThrow(SearchManager.SUGGEST_COLUMN_TEXT_1));
			if (!Objects.equals(suggest, query))
				c.addRow(new Object[]{id, suggest, R.drawable.history, suggest});
		}
		recent.close();
		return c;
	}
	
	private void clearHistory() {
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this.getContext(),
			SearchSuggestions.AUTHORITY, SearchSuggestions.MODE);
		suggestions.clearHistory();
	}
	
	public static void deleteAllSuggestions() {
		instance.clearHistory();
	}
}
