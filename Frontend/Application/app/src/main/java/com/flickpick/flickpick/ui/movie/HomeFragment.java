package com.flickpick.flickpick.ui.movie;

import static android.view.View.VISIBLE;
import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.AppController.capitalCase;
import static com.flickpick.flickpick.util.Constants.GENRE_MOVIES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.MOVIES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.UNIQUE_GENRES_URL;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.FragmentHomeScreenBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * Code for the home fragment
 * @author Liam Janda
 */
public class HomeFragment extends Fragment {
	
	private FragmentHomeScreenBinding binding;
	LinearLayout scrollLayout;
	int numWidgets;
	static final int INCREMENT = 10;		//how many genres are added when 'more genres' is pressed
	Button addWidgets;

	/**
	 * When this is fragment is created, this method will be called
	 * @param inflater used to create view from corresponding xml file
	 * @param container the ViewGroup that this fragment belongs to
	 * @param savedInstanceState Vital information from last instance of this fragment
	 * @return view
	 */

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
		View root = binding.getRoot();

		Bundle info = getArguments();
		if(info != null){
			if(info.containsKey("newMovie")) {
				Bundle movInt = new Bundle();
				movInt.putInt("movieId", info.getInt("newMovie"));
				((NavigationActivity) requireActivity()).navTo(R.id.movieFragment, getString(R.string.loading), movInt);
			}
		}

		addWidgets = new Button(getContext());
		addWidgets.setText("More Genres");
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(			//'more genres' button formatting
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT
		);
		int x = AppController.getPxFromDp(20);
		int y = AppController.getPxFromDp(10);
		params.setMargins(x, y, x, y);
		addWidgets.setLayoutParams(params);


		scrollLayout = binding.scrollLayout;

		numWidgets = 0;
		numWidgets = viewMoreWidgets(numWidgets);

		addWidgets.setOnClickListener(view -> {						//when 'more genres' is pressed
			scrollLayout.removeView(addWidgets);
			numWidgets = viewMoreWidgets(numWidgets);
		});

		return root;
	}

	/**
	 * when view created from onCreateView is detached from fragment
	 */

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}

	/**
	 * used to add a genre widget to the fragment
	 * @param genreId id of genre
	 * @param genreName name of genre string
	 */

	public void addGenreWidget(int genreId, String genreName){										//add unique genre widget with movie images
		View view = getLayoutInflater().inflate(R.layout.widget_home_screen, null);
		TextView genreText = view.findViewById(R.id.genreText);
		Button viewAllGenreBtn = view.findViewById(R.id.viewAllGenreBtn);
		String capGenreName = capitalCase(genreName);		//make first letter capital
		genreText.setText(capGenreName);

		viewAllGenreBtn.setOnClickListener(v -> {					//when genre 'view all' button pressed
			Bundle movInt = new Bundle();
			movInt.putInt("genreId", genreId);
			movInt.putString("genreName", capGenreName);
			((NavigationActivity)requireActivity()).navTo(R.id.genreFragment, getString(R.string.genre_title, capGenreName), movInt);
		});

		JsonArrayRequest genreMovieRequest = new JsonArrayRequest(Request.Method.GET, GENRE_MOVIES_BASE_URL + genreName, null,			//GET movies from genre
				response -> {
					int[] movieIds= {-1, -1, -1, -1, -1};
					for(int i = 0; i < movieIds.length; ++i){
						if(i >= response.length()){
							break;
						}
						else{

							try {
								movieIds[i] = response.getJSONObject(i).getInt("id");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

					ImageButton mov1 = view.findViewById(R.id.movie1Btn);					//set Image button info and makes visible
					showMovie(mov1, movieIds[0]);
					ImageButton mov2 = view.findViewById(R.id.movie2Btn);					//set Image button info and makes visible
					showMovie(mov2, movieIds[1]);
					ImageButton mov3 = view.findViewById(R.id.movie3Btn);					//set Image button info and makes visible
					showMovie(mov3, movieIds[2]);
					ImageButton mov4 = view.findViewById(R.id.movie4Btn);					//set Image button info and makes visible
					showMovie(mov4, movieIds[3]);
					ImageButton mov5 = view.findViewById(R.id.movie5Btn);					//set Image button info and makes visible
					showMovie(mov5, movieIds[4]);

				}, error -> {
					Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
		});

		AppController.getInstance().addToRequestQueue(genreMovieRequest);

		scrollLayout.addView(view);
	}

	/**
	 * when the button to view more genres is pressed
	 * @param numWidgets curr number of genre widgets on the fragment
	 * @return the new number of widgets on the fragment
	 */

	public int viewMoreWidgets(int numWidgets){
		int localNumWidgets = numWidgets;
		JsonArrayRequest uniqueGenreRequest = new JsonArrayRequest(Request.Method.GET, UNIQUE_GENRES_URL, null,				//get list on unique genres
				response -> {
					String[] genreList = new String[response.length()];
					for(int i = 0; i < response.length(); ++i){
						try {
							genreList[i] = response.getJSONObject(i).getString("genre");
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					AppController.getInstance().setGenres(genreList);

					for (int i = localNumWidgets; i < localNumWidgets + INCREMENT; ++i) {		//iterate INCREMENT times

						if(i >= response.length()){
							break;
						}

						try {
							JSONObject currGenre = response.getJSONObject(i);					//curr genre
							int id = currGenre.getInt("id");
							String genreName = currGenre.getString("genre");

							if(!genreName.equals("")){
								addGenreWidget(id, genreName);									//add genre widget
							}

							if (i == localNumWidgets + INCREMENT - 1){
								scrollLayout.addView(addWidgets);
							}

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, error -> {
					Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
		});
		AppController.getInstance().addToRequestQueue(uniqueGenreRequest);
		return numWidgets + INCREMENT;
	}

	/**
	 * sets movie image of image button
	 * @param movieId id of movie
	 * @param imgBtn image button of movie
	 */

	private void setMovieImage(int movieId, ImageButton imgBtn){
		JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, MOVIES_BASE_URL + movieId + "/picture", null,      //get movie pic
				response -> {
					if(response != null) {
						try {
							String base64 = response.getString("picture");
							Bitmap image = base64ToBitmap(base64);
							imgBtn.setImageBitmap(image);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, error -> {
					Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
		});
		AppController.getInstance().addToRequestQueue(getImage);
	}

	/**
	 * used for the remainder of movies; if there are no movies left, remove imgBtn
	 * @param movieId id of movie; if -1, remove imgBtn
	 * @param imgBtn image button of movie
	 */

	private void showMovie(ImageButton imgBtn, int movieId){
		if(movieId != -1){
			setMovieImage(movieId, imgBtn);
			Bundle movInt = new Bundle();
			movInt.putInt("movieId", movieId);
			imgBtn.setVisibility(VISIBLE);
			imgBtn.setOnClickListener(view2 ->													//go to movie page
					((NavigationActivity)requireActivity()).navTo(R.id.movieFragment, getString(R.string.loading), movInt));
		}
		else{
			((ViewGroup)imgBtn.getParent()).removeView(imgBtn);					//remove if -1
		}
	}
}