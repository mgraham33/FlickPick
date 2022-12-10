package com.flickpick.flickpick.ui.search;

import static com.flickpick.flickpick.ui.search.SearchableActivity.*;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.FragmentSearchBinding;
import com.flickpick.flickpick.databinding.WidgetSearchResultMainBinding;
import com.flickpick.flickpick.databinding.WidgetSearchResultSecondaryBinding;
import com.flickpick.flickpick.ui.movie.MovieFragment;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.NavigationActivity;
import com.flickpick.flickpick.util.Settings;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * Displays search results
 *
 * @author Dustin Cornelison
 */
public class SearchFragment extends Fragment {
	public static final String IMG_REQ_TAG = "movie_img";
	
	private FragmentSearchBinding binding;
	private CheckBox[] genres;
	private CheckBox[] services;
	
	private final HashMap<String, Integer> mainIds = new HashMap<>();
	
	private static final String FILTER = "filter";
	private static final String FILTER_RATING_FROM = "rating_from";
	private static final String FILTER_RATING_TO = "rating_to";
	private static final String FILTER_YEAR_FROM = "year_from";
	private static final String FILTER_YEAR_TO = "year_to";
	private static final String FILTER_DURATION_FROM = "duration_from";
	private static final String FILTER_DURATION_TO = "duration_to";
	private static final String FILTER_GENRES = "genres";
	private static final String FILTER_SERVICES = "services";
	
	private static final String MAIN_READY = "main_ready";
	private static final String SEC_READY = "secondary_ready";
	
	private static OnSearchCompleteListener staticListener;
	private final OnSearchCompleteListener listener = new OnSearchCompleteListener() {
		@Override
		public void updateMainResults(Object[][] mainResults) {
			populateMainResults();
		}
		
		@Override
		public void updateSecondaryResults(Object[][] secondaryResults) {
			populateSecondaryResults();
		}
		
		@Override
		public void updateMainFail() {
		}
	};
	
	/**
	 * Creates the view and manages UI.
	 * @param inflater used to inflate the binding
	 * @param container the parent object for the binding
	 * @param savedInstanceState previous instance, if saved
	 * @return the created view
	 */
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		binding = FragmentSearchBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		Intent results = NavigationActivity.getResults();
		if (results == null) {
			displayError();
			return root;
		}
		staticListener = listener;
		if (results.getBooleanExtra(MAIN_READY, false)) {
			listener.updateMainResults(null);
			results.removeExtra(MAIN_READY);
		}
		if (results.getBooleanExtra(SEC_READY, false)) {
			listener.updateSecondaryResults(null);
			results.removeExtra(SEC_READY);
		}
		
		binding.resetFilterBtn.setOnClickListener(v -> resetFilters(results));
		
		binding.resetFilterBtn.setOnLongClickListener(v -> {
			hardResetFilters();
			return true;
		});
		
		binding.applyFilterBtn.setOnClickListener(v -> SearchableActivity.modifySearch(getFilter(), results, listener));
		
		int x = (int)(AppController.getPxFromDp(16)/1.5);
		int y = (int)(AppController.getPxFromDp(12)/1.5);
		Drawable icoDown = AppController.getDrawableRes(android.R.drawable.arrow_down_float);
		Drawable icoUp = AppController.getDrawableRes(android.R.drawable.arrow_up_float);
		icoDown.setBounds(0, 0, x, y);
		icoUp.setBounds(0, 0, x, y);
		binding.filterButton.setCompoundDrawables(null, null, icoDown, null);
		
		binding.filterButton.setOnClickListener(view -> {
			if (binding.filters.getHeight() == 0) {
				binding.filters.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
				binding.filterButton.setCompoundDrawables(null, null, icoUp, null);
			} else {
				binding.filters.getLayoutParams().height = 0;
				binding.filterButton.setCompoundDrawables(null, null, icoDown, null);
			}
		});
		
		binding.ratingFilterFrom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (binding.ratingFilterTo.getProgress() < progress) {
					binding.ratingFilterTo.setProgress(progress);
				}
				AppController.setStars(binding.star1From, binding.star2From, binding.star3From, binding.star4From, binding.star5From, progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		binding.ratingFilterTo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (binding.ratingFilterFrom.getProgress() > progress) {
					binding.ratingFilterFrom.setProgress(progress);
				}
				AppController.setStars(binding.star1To, binding.star2To, binding.star3To, binding.star4To, binding.star5To, progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		return root;
	}
	
	/**
	 * @return a filter object with the currently selected filters
	 */
	private Filter getFilter() {
		Filter f = new Filter(true);
		f.setLowRating(binding.ratingFilterFrom.getProgress() / 2d);
		f.setHighRating(binding.ratingFilterTo.getProgress() / 2d);
		String lowYear = binding.yearFilterFrom.getText().toString();
		if (!lowYear.equals("")) f.setLowYear(Integer.parseInt(lowYear));
		String highYear = binding.yearFilterTo.getText().toString();
		if (!highYear.equals("")) f.setHighYear(Integer.parseInt(highYear));
		String lowMin = binding.durationFilterFrom.getText().toString();
		if (!lowMin.equals("")) f.setLowMin(Integer.parseInt(lowMin));
		String highMin = binding.durationFilterTo.getText().toString();
		if (!highMin.equals("")) f.setHighMin(Integer.parseInt(highMin));
		
		boolean serviceFiltered = false;
		for (CheckBox b : services) if (b.isChecked()) {
			serviceFiltered = true;
			break;
		}
		
		if (serviceFiltered) for (int i = 0; i < services.length; i++)
			f.setService(i, services[i].isChecked());
		
		boolean genreFiltered = false;
		for (CheckBox b : genres) if (b.isChecked()) {
			genreFiltered = true;
			break;
		}
		
		if (genreFiltered) for (int i = 0; i < genres.length; i++)
			f.setGenre(i, genres[i].isChecked());
		
		return f;
	}
	
	/**
	 * Creates and shows main results.
	 */
	private void populateMainResults() {
		Intent results = NavigationActivity.getResults();
		mainIds.clear();
		binding.mainSearchResults.removeAllViews();
		if (results == null) {
			displayError();
		} else {
			int num = 1;
			boolean changed = false;
			while (true) {
				Bundle b = results.getBundleExtra(MAIN + num);
				if (b == null) break;
				int id = b.getInt(ID);
				String title = AppController.getMovieTitleId(b.getString(TITLE), id);
				changed = true;
				View.OnClickListener l = v -> {
					Bundle b2 = new Bundle();
					b2.putInt(MovieFragment.ID, id);
					((NavigationActivity)requireActivity()).navTo(R.id.movieFragment, title, b2);
				};
				WidgetSearchResultMainBinding mResult =
					createMainResult(title, b.getString(DESC),
						b.getStringArray(GENRES), b.getInt(RATING), l);
				mResult.mainResult.setOnClickListener(l);
				mainIds.put(title, id);
				imageRequest(mResult.movieImage, id,
					() -> mResult.mainResultProgress.setVisibility(View.GONE));
				num++;
			}
			if (!changed)
				binding.primaryResultsText.setText(getString(R.string.search_no_results, results.getStringExtra(SEARCH)));
			else
				binding.primaryResultsText.setText(getString(R.string.primary_results, results.getStringExtra(SEARCH)));
		}
	}
	
	/**
	 * Creates and shows secondary results.
	 * Should happen after showing main results.
	 */
	private void populateSecondaryResults() {
		Intent results = NavigationActivity.getResults();
		binding.searchResultProgress.setVisibility(View.GONE);
		binding.secondarySearchResults.removeAllViews();
		if (results == null) {
			displayError();
		} else {
			int num = 0;
			boolean changed = false;
			while (true) {
				num++;
				Bundle b = results.getBundleExtra(SECONDARY + num);
				if (b == null) break;
				if (isInMain(b)) continue;
				int id = b.getInt(ID);
				String title = AppController.getMovieTitleId(b.getString(TITLE), id);
				changed = true;
				View.OnClickListener l = v -> {
					Bundle b2 = new Bundle();
					b2.putInt(MovieFragment.ID, id);
					((NavigationActivity)requireActivity()).navTo(R.id.movieFragment, title, b2);
				};
				WidgetSearchResultSecondaryBinding sResult =
					createSecondaryResult(title, b.getString(DESC),
						b.getStringArray(GENRES), b.getInt(RATING), l);
				sResult.secondaryResult.setOnClickListener(l);
				imageRequest(sResult.movieImage, id,
					() -> sResult.secondaryResultProgress.setVisibility(View.GONE));
			}
			if (!changed) binding.secondaryResultsText.setVisibility(View.GONE);
			else binding.secondaryResultsText.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * Checks if a results is in the main results.
	 * @param b the result to check
	 * @return whether the result is in the main search results
	 */
	private boolean isInMain(Bundle b) {
		Integer id = mainIds.get(b.getString(TITLE));
		if (id == null) return false;
		return id == b.getInt(ID);
	}
	
	/**
	 * Requests an image from the server to display on the result.
	 * @param img the image view to display the image on
	 * @param id the id of the movie to get the image for
	 * @param hideLoading called when the image is shown: should hide the loading spinner
	 */
	private void imageRequest(ImageView img, int id, Runnable hideLoading) {
		imageRequest(img, id, hideLoading, getContext());
	}
	
	/**
	 * Requests an image from the server to display on the result.
	 * @param img the image view to display the image on
	 * @param id the id of the movie to get the image for
	 * @param hideLoading called when the image is shown: should hide the loading spinner
	 * @param c the context in which to show errors
	 */
	public static void imageRequest(ImageView img, int id, Runnable hideLoading, Context c) {
		imageRequest(img, hideLoading, c, Constants.MOVIES_BASE_URL + id + "/picture", "picture");
	}
	
	
	/**
	 * Requests an image from the server to display on the result.
	 * @param img the image view to display the image on
	 * @param hideLoading called when the image is shown: should hide the loading spinner
	 * @param c the context in which to show errors
	 * @param url the url to fetch the image from
	 * @param loc the key/tag in the response where the image will be
	 */
	public static void imageRequest(ImageView img, Runnable hideLoading, Context c, String url, String loc) {
		JsonObjectRequest imgReq = new JsonObjectRequest(Request.Method.GET, url, null,
			response -> {
				try {
					img.setImageBitmap(AppController.base64ToBitmap(response.getString(loc)));
					hideLoading.run();
				} catch (JSONException e) {
					e.printStackTrace();
					Toast.makeText(c, R.string.parse_movie_image_fail, Toast.LENGTH_SHORT).show();
					hideLoading.run();
					//display broken image
					img.setImageDrawable(AppController.getDrawableRes(R.drawable.broken_image));
				}
			}, error -> Toast.makeText(c, R.string.fetch_movie_image_error, Toast.LENGTH_SHORT).show());
		
		AppController.getInstance().addToRequestQueue(imgReq, IMG_REQ_TAG);
	}
	
	/**
	 * Resets the filters to the previous search (previously applied).
	 * @param results the intent to modify the filters on
	 */
	private void resetFilters(Intent results) {
		if (results != null) {
			Bundle filters = results.getBundleExtra(FILTER);
			if (filters != null) {
				binding.ratingFilterFrom.setProgress(filters.getInt(FILTER_RATING_FROM));
				binding.ratingFilterTo.setProgress(filters.getInt(FILTER_RATING_TO));
				
				binding.yearFilterFrom.setText(filters.getString(FILTER_YEAR_FROM));
				binding.yearFilterTo.setText(filters.getString(FILTER_YEAR_TO));
				
				binding.durationFilterFrom.setText(filters.getString(FILTER_DURATION_FROM));
				binding.durationFilterTo.setText(filters.getString(FILTER_DURATION_TO));
				
				boolean[] genresB = filters.getBooleanArray(FILTER_GENRES);
				for (int i = 0; i < genresB.length; i++) {
					genres[i].setChecked(genresB[i]);
				}
				
				boolean[] servicesB = filters.getBooleanArray(FILTER_SERVICES);
				for (int i = 0; i < servicesB.length; i++) {
					services[i].setChecked(servicesB[i]);
				}
			} else hardResetFilters();
		} else hardResetFilters();
	}
	
	/**
	 * Resets the filters to default values.
	 */
	private void hardResetFilters() {
		binding.ratingFilterFrom.setProgress(0);
		binding.ratingFilterTo.setProgress(10);
		
		binding.yearFilterFrom.setText("");
		binding.yearFilterTo.setText("");
		
		binding.durationFilterFrom.setText("");
		binding.durationFilterTo.setText("");
		for (CheckBox genre : genres) genre.setChecked(false);
		
		for (CheckBox service : services) {
			String title = service.getText().toString();
			//TODO: do this dynamically
			if (title.toLowerCase().contains("netflix")) {
				service.setChecked(Settings.NETFLIX.get());
			} else if (title.toLowerCase().contains("hbo")) {
				service.setChecked(Settings.HBO.get());
			} else if (title.toLowerCase().contains("hulu")) {
				service.setChecked(Settings.HULU.get());
			} else if (title.toLowerCase().contains("prime")) {
				service.setChecked(Settings.PRIME.get());
			} else if (title.toLowerCase().contains("disney")) {
				service.setChecked(Settings.DISNEY.get());
			}
		}
	}
	
	/**
	 * Resets the binding.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
	
	/**
	 * Called when created and when returning to this view.
	 */
	@Override
	public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		staticListener = listener;
		binding.filterGenreRows.removeAllViews();
		String[] genres = AppController.getInstance().getGenres();
		ArrayList<CheckBox> genresList = new ArrayList<>();
		int dp = AppController.getDpFromPx(getResources().getDisplayMetrics().widthPixels);
		int numCol = (int)((dp - 75) / 100f);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
		p.weight = 1;
		
		for (int i = 0; i < numCol; i++) {
			LinearLayout l = new LinearLayout(binding.filterGenreRows.getContext());
			l.setOrientation(LinearLayout.VERTICAL);
			l.setLayoutParams(p);
			binding.filterGenreRows.addView(l);
			boolean extra = (genres.length % numCol) > i;
			
			for (int j = 0; j < genres.length / numCol; j++) {
				genresList.add(createFilterCheckbox(genres[genresList.size()], l));
			}
			if (extra) genresList.add(createFilterCheckbox(genres[genresList.size()], l));
		}
		this.genres = genresList.toArray(new CheckBox[0]);
		
		binding.filterServicesRows.removeAllViews();
		String[] services = AppController.getInstance().getServices();
		ArrayList<CheckBox> servicesList = new ArrayList<>();
		
		for (int i = 0; i < numCol; i++) {
			LinearLayout l = new LinearLayout(binding.filterServicesRows.getContext());
			l.setOrientation(LinearLayout.VERTICAL);
			l.setLayoutParams(p);
			binding.filterServicesRows.addView(l);
			boolean extra = (services.length % numCol) > i;
			
			for (int j = 0; j < services.length / numCol; j++) {
				servicesList.add(createFilterCheckbox(services[servicesList.size()], l));
			}
			if (extra) servicesList.add(createFilterCheckbox(services[servicesList.size()], l));
		}
		
		this.services = servicesList.toArray(new CheckBox[0]);
		
		Intent results = NavigationActivity.getResults();
		binding.primaryResultsText.setText(getString(R.string.primary_results, results.getStringExtra(SEARCH)));
		resetFilters(results);
	}
	
	/**
	 * Updates the main results, or if the Fragment is not initialized yet, sets a flag to
	 * update the results when initialized.
	 * @param i the intent that will be used for results; the flag is set here
	 */
	public static void updateMainResults(Intent i) {
		if (staticListener != null)
			staticListener.updateMainResults(null);
		else i.putExtra(MAIN_READY, true);
	}
	
	/**
	 * Updates the secondary results, or if the Fragment is not initialized yet, sets a flag to
	 * update the results when initialized.
	 * @param i the intent that will be used for results; the flag is set here
	 */
	public static void updateSecondaryResults(Intent i) {
		if (staticListener != null) {
			staticListener.updateSecondaryResults(null);
		}
		else i.putExtra(SEC_READY, true);
	}
	
	/**
	 * Will update both results if the fragment is initialized; will not set flags if not
	 * initialized. Used to reload results that are already retrieved.
	 */
	public static void reloadResults() {
		if (staticListener != null) {
			staticListener.updateMainResults(null);
			staticListener.updateSecondaryResults(null);
		}
	}
	
	/**
	 * Sets the static listener and reloads results
	 */
	@Override
	public void onResume() {
		super.onResume();
		staticListener = listener;
		reloadResults();
	}
	
	/**
	 * Resets the static listener
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (staticListener == listener) staticListener = null;
	}
	
	/**
	 * Creates a single filter checkbox.
	 * @param title the name of the checkbox; displayed to the user
	 * @param col the column to put the checkbox in
	 * @return the created checkbox, already added to the {@code col}
	 */
	private CheckBox createFilterCheckbox(String title, LinearLayout col) {
		CheckBox b = new CheckBox(col.getContext());
		b.setText(title);
		b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		p.rightMargin = AppController.getPxFromDp(5);
		b.setLayoutParams(p);
		col.addView(b);
		//TODO: do this dynamically
		if (title.toLowerCase().contains("netflix")) {
			b.setChecked(Settings.NETFLIX.get());
		} else if (title.toLowerCase().contains("hbo")) {
			b.setChecked(Settings.HBO.get());
		} else if (title.toLowerCase().contains("hulu")) {
			b.setChecked(Settings.HULU.get());
		} else if (title.toLowerCase().contains("prime")) {
			b.setChecked(Settings.PRIME.get());
		} else if (title.toLowerCase().contains("disney")) {
			b.setChecked(Settings.DISNEY.get());
		}
		return b;
	}
	
	/**
	 * Creates a main result.
	 * @param title the title for the movie
	 * @param desc the description for the movie
	 * @param genres the genres for the movie
	 * @param rating the rating for the movie, out of 10
	 * @return the binding object associated with the result
	 */
	private WidgetSearchResultMainBinding createMainResult(String title, String desc, String[] genres, int rating, View.OnClickListener l) {
		WidgetSearchResultMainBinding result = WidgetSearchResultMainBinding.inflate(getLayoutInflater());
		result.movieImage.setImageDrawable(AppController.getDrawableRes(android.R.drawable.gallery_thumb));
		result.searchTitle.setText(title);
		setDesc(result.description, desc, title, l);
		setGenres(result.searchGenres, genres);
		AppController.setStars(result.star1, result.star2, result.star3, result.star4, result.star5, rating);
		binding.mainSearchResults.addView(result.root);
		return result;
	}
	/**
	 * Creates a secondary result.
	 * @param title the title for the movie
	 * @param desc the description for the movie
	 * @param genres the genres for the movie
	 * @param rating the rating for the movie, out of 10
	 * @return the binding object associated with the result
	 */
	private WidgetSearchResultSecondaryBinding createSecondaryResult(String title, String desc, String[] genres, int rating, View.OnClickListener l) {
		WidgetSearchResultSecondaryBinding result = WidgetSearchResultSecondaryBinding.inflate(getLayoutInflater());
		result.movieImage.setImageDrawable(AppController.getDrawableRes(android.R.drawable.gallery_thumb));
		result.secondarySearchTitle.setText(title);
		setDesc(result.secondaryDescription, desc, title, l);
		setGenres(result.secondarySearchGenres, genres);
		AppController.setStars(result.star1, result.star2, result.star3, result.star4, result.star5, rating);
		binding.secondarySearchResults.addView(result.root);
		return result;
	}
	
	/**
	 * Displays a generic error if there is a problem.
	 */
	private void displayError() {
		Toast.makeText(getContext(), "Could not find results", Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Sets the description text and sets onClick methods.
	 * @param desc the TextView to display the description on
	 * @param description the description for the movie
	 * @param title the title of the movie
	 * @param l the click listener to add to {@code desc}
	 */
	private void setDesc(TextView desc, String description, String title, View.OnClickListener l) {
		desc.setText(description);
		desc.setOnLongClickListener(v -> {
			new AlertDialog.Builder(getContext()).setTitle(title).setMessage(description).show();
			return false;
		});
		desc.setOnClickListener(l);
	}
	
	/**
	 * Sets the genre text to display in a meaningful way.
	 * @param genreText the TextView to display the genres on
	 * @param genres the genres for the movie
	 */
	private void setGenres(TextView genreText, String[] genres) {
		setGenres(genreText, genres, getResources());
	}
	
	/**
	 * Sets the genre text to display in a meaningful way.
	 * @param genreText the TextView to display the genres on
	 * @param genres the genres for the movie
	 * @param r the resources from which to supply the genre string preset
	 */
	public static void setGenres(TextView genreText, String[] genres, Resources r) {
		String genreList;
		StringBuilder b = new StringBuilder();
		for (String g : genres) {
			b.append(AppController.capitalCase(g));
			b.append(", ");
		}
		if (genres.length > 0) {
			b.delete(b.length() - 2, b.length());
			genreList = r.getQuantityString(R.plurals.genre_list, genres.length, b.toString());
		} else genreList = r.getString(R.string.genre_list_zero);
		
		genreText.setText(genreList);
	}
}