package com.flickpick.flickpick.ui.search;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.ActivitySearchableBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is an inconvenience. Essentially, any screen that should have the menu
 * (to switch screens) needs to be a fragment, but the way searching is implemented
 * in the Android API, when you press go (to search) it wants to go to an activity,
 * which cannot be a fragment. So this class serves as the mediator in between when
 * you click go to search and when the results are received.
 *
 * @author Dustin Cornelison
 */
public class SearchableActivity extends AppCompatActivity {
	public static final String SEARCH = "search";
	public static final String MAIN = "main";
	public static final String SECONDARY = "secondary";
	//Inside each result
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String DESC = "desc";
	public static final String MIN = "min";
	public static final String YEAR = "year";
	public static final String GENRES = "genres";
	//public static final String IMAGE = "image";
	public static final String RATING = "rating";
	
	private static final String TAG = "search";
	//Array index locations
	private static final int ID_I = 0;
	private static final int TITLE_I = 1;
	private static final int DESC_I = 2;
	private static final int MIN_I = 3;
	private static final int YEAR_I = 4;
	private static final int RATING_I = 5;
	private static final int GENRES_I = 6;
	
	ActivitySearchableBinding binding;
	
	/**
	 * Creates the view and manages UI.
	 * @param savedInstanceState previous instance, if saved
	 * @see androidx.fragment.app.FragmentActivity#onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivitySearchableBinding.inflate(getLayoutInflater());
		overridePendingTransition(0, 0);
		setContentView(binding.getRoot());
		
		// Get the intent, verify the action and get the query
		Intent intent = getIntent();
		System.out.println(intent);
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// Handle the normal search query case
			String query = intent.getStringExtra(SearchManager.QUERY);
			if (query == null) query = intent.getDataString();
			search(query);
			SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
				SearchSuggestions.AUTHORITY, SearchSuggestions.MODE);
			suggestions.saveRecentQuery(query, null);
		}
	}
	
	
	/**
	 * Performs a search on the given query and sends the results to the
	 * {@code SearchFragment} to be displayed
	 * @param query what to search for
	 */
	private void search(String query) {
		binding.searchingText.setText(getString(R.string.searching, query));
		Intent i = new Intent();
		doSearch(query, new Filter(), new OnSearchCompleteListener() {
			@Override
			public void updateMainResults(Object[][] mainResults) {
				finishSearch(query, mainResults, i);
				SearchFragment.updateMainResults(i);
			}
			
			@Override
			public void updateSecondaryResults(Object[][] secondaryResults) {
				modifyIntent(i, secondaryResults, false);
				SearchFragment.updateSecondaryResults(i);
			}
			
			@Override
			public void updateMainFail() {
				onBackPressed();
			}
		});
	}
	
	/**
	 * Modifies the current search filters
	 * @param filter the new filter to apply
	 * @param i the intent to modify
	 * @param l the listener to update UI. Note: the arguments of the methods in this listener
	 * will always be null: the intent is already updated; just update the UI.
	 */
	public static void modifySearch(Filter filter, Intent i, OnSearchCompleteListener l) {
		doSearch(i.getStringExtra(SEARCH), filter, new OnSearchCompleteListener() {
			@Override
			public void updateMainResults(Object[][] mainResults) {
				modifyIntent(i, mainResults, true);
				l.updateMainResults(null);
			}
			
			@Override
			public void updateSecondaryResults(Object[][] secondaryResults) {
				modifyIntent(i, secondaryResults, false);
				l.updateSecondaryResults(null);
			}
			
			@Override
			public void updateMainFail() {}
		});
	}
	
	/**
	 * Sends a request to the backend to search
	 * @param query the search query
	 * @param filter the filters to apply to the search
	 * @param l listens for server responses
	 */
	private static void doSearch(String query, Filter filter, OnSearchCompleteListener l) {
		//Parameters
		Map<String, String> params = new HashMap<>();
		filter.addToMap(params);
		
		//Don't search by title
		JsonArrayRequest secondarySearch = new JsonArrayRequest(Request.Method.GET,
			AppController.getURLWithParams(Constants.MOVIES_BASE_URL + "filters", params), null,
			response -> {
				try {
					ArrayList<Object[]> secondaryResults = parseResults(response);
					l.updateSecondaryResults(secondaryResults.toArray(new Object[0][]));
				} catch (JSONException e) {
					Toast.makeText(AppController.getInstance().getCurrentContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
				}
			}, error -> {
			Toast.makeText(AppController.getInstance().getCurrentContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
			error.printStackTrace();
		});
		
		//Search by title
		params.put("title", query);
		JsonArrayRequest mainSearch = new JsonArrayRequest(Request.Method.GET,
			AppController.getURLWithParams(Constants.MOVIES_BASE_URL + "filters", params), null,
            response -> {
               try {
	               ArrayList<Object[]> mainResults = parseResults(response);
	               l.updateMainResults(mainResults.toArray(new Object[0][]));
               } catch (JSONException e) {
                   Toast.makeText(AppController.getInstance().getCurrentContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
				   l.updateMainFail();
               }
            }, error -> {
			Toast.makeText(AppController.getInstance().getCurrentContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
			error.printStackTrace();
			l.updateMainFail();
		});
		
		AppController.getInstance().addToRequestQueue(mainSearch, TAG);
		AppController.getInstance().addToRequestQueue(secondarySearch, TAG);
	}
	
	/**
	 * Parses search results.
	 * @param response the server response to parse
	 * @return parsed results in an arraylist
	 * @throws JSONException if there is a problem parsing
	 */
	private static ArrayList<Object[]> parseResults(JSONArray response) throws JSONException {
		ArrayList<Object[]> results = new ArrayList<>();
		for (int i = 0; i < response.length(); i++) {
			//Omit image since caching will likely cause a memory overflow
			Object[] result = new Object[7];
			String[] genres = new String[3];
			result[ID_I] = response.getJSONObject(i).getInt("id");
			result[TITLE_I] = response.getJSONObject(i).getString("title");
			result[DESC_I] = response.getJSONObject(i).getString("description");
			result[MIN_I] = response.getJSONObject(i).getInt("minutes");
			result[YEAR_I] = response.getJSONObject(i).getInt("year");
			result[RATING_I] = (int)Math.round(response.getJSONObject(i).getDouble("rating") * 2);
			genres[0] = response.getJSONObject(i).getString("genre1");
			genres[1] = response.getJSONObject(i).getString("genre2");
			genres[2] = response.getJSONObject(i).getString("genre3");
			if (genres[0].equals("")) result[6] = new String[]{};
			else if (genres[1].equals("")) result[6] = new String[]{genres[0]};
			else if (genres[2].equals("")) result[6] = new String[]{genres[0], genres[1]};
			else result[GENRES_I] = genres;
			results.add(result);
		}
		return results;
	}
	
	/**
	 * Takes a 2d object array returned from results and adds it to the intent
	 * @param i the intent to put the results on
	 * @param results the results to put on the intent
	 * @param main whether the results are main or secondary
	 */
	private static void modifyIntent(Intent i, Object[][] results, boolean main) {
		//for each result, create a bundle with the above static tags
		//numbered by the results & prefixed with main or secondary
		int num = 0;
		while (true) {
			num++;
			Bundle b = i.getBundleExtra((main ? MAIN : SECONDARY) + num);
			if (b == null) break;
			i.removeExtra((main ? MAIN : SECONDARY) + num);
		}
		
		num = 1;
		for (Object[] o : results) {
			Bundle b = new Bundle();
			b.putInt(ID, (int)o[ID_I]);
			b.putString(TITLE, (String)o[TITLE_I]);
			b.putInt(MIN, (int)o[MIN_I]);
			b.putInt(YEAR, (int)o[YEAR_I]);
			b.putStringArray(GENRES, (String[])o[GENRES_I]);
			b.putInt(RATING, (int)o[RATING_I]);
			b.putString(DESC, (String)o[DESC_I]);
			i.putExtra((main ? MAIN : SECONDARY) + num, b);
			num++;
		}
	}
	
	/**
	 * Called when an initial search's (not filter changed) main results are
	 * returned from the server to display to the user.
	 * @param query what the search was
	 * @param mainResults the object array of results
	 * @param i the intent to pass to the navigation activity
	 */
	private void finishSearch(String query, Object[][] mainResults, Intent i) {
		i.putExtra(SEARCH, query);
		modifyIntent(i, mainResults, true);
		NavigationActivity.setResults(i);
		SearchableActivity.super.onBackPressed();
		finish(false); //don't cancel search: secondary might still be searching
	}
	
	/**
	 * Finishes the activity if back is pressed.
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//cancel search & finish activity
		finish(true);
	}
	
	/**
	 * Finishes the activity, but optionally doesn't cancel pending searches
	 * @param cancel whether to cancel pending searches
	 */
	public void finish(boolean cancel) {
		if (cancel)	AppController.getInstance().cancelPendingRequests(TAG);
		super.finish();
	}
}