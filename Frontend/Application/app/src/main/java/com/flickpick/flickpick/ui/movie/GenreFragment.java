package com.flickpick.flickpick.ui.movie;

import static android.view.View.VISIBLE;
import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.AppController.capitalCase;
import static com.flickpick.flickpick.util.Constants.GENRE_MOVIES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.MOVIES_BASE_URL;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.flickpick.flickpick.databinding.FragmentGenreScreenBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;

/**
 * Code for the genre fragment
 * @author Liam Janda
 */

public class GenreFragment extends Fragment {

    private FragmentGenreScreenBinding binding;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGenreScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LinearLayout genreLinearLayout = binding.genreLinearLayout;

        Bundle info = getArguments();
        String genreName = info.getString("genreName");
        TextView genreTitle = binding.genreName;
        String capGenreName = capitalCase(genreName);		//make first letter capital

        genreTitle.setText(capGenreName);

        addMovies(genreLinearLayout, genreName);    //add movie widgets

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
     * used to get all movies from the genre and show them on the fragment
     * @param genreLinearLayout the layout to add the movies to
     * @param genreName genre name string
     */

    private void addMovies(LinearLayout genreLinearLayout, String genreName){
        JsonArrayRequest getMovies = new JsonArrayRequest(Request.Method.GET, GENRE_MOVIES_BASE_URL + genreName, null,      //get genre movies
                response -> {
                    for(int i = 0; i < response.length(); i = i + 3){                   //for each movie, group into 3's plus remainder

                        try {
                            int movie1, movie2 = -1, movie3 = -1;
                            if (i < response.length()) {
                                movie1 = response.getJSONObject(i).getInt("id");
                                if (i + 1 < response.length()) {
                                    movie2 = response.getJSONObject(i+1).getInt("id");
                                    if (i + 2 < response.length()) {
                                        movie3 = response.getJSONObject(i+2).getInt("id");
                                    }
                                }
                            } else break;
                            addMovieWidget(movie1, movie2, movie3, genreLinearLayout);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });
        AppController.getInstance().addToRequestQueue(getMovies);

    }

    /**
     * adds one row of movies to the layout
     * @param movie1 id of movie 1
     * @param movie2 id of movie 2
     * @param movie3 id of movie 3
     * @param genreLinearLayout layout to add movies to
     */

    private void addMovieWidget(int movie1, int movie2, int movie3, LinearLayout genreLinearLayout){
        if (movie1 == -1) return; //shouldn't happen, but is safeguard
        View view = getLayoutInflater().inflate(R.layout.widget_genre_screen, null);


        showMovie(movie1, view, R.id.movie1ImgBtn);
        showMovie(movie2, view, R.id.movie2ImgBtn);
        showMovie(movie3, view, R.id.movie3ImgBtn);

        genreLinearLayout.addView(view);
    }

    /**
     * used to set movieBtn image by converting the base64 from server to bitmap
     * @param movieId id of movie
     * @param imgBtn image button of the movie
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
     * used for the remainder of movies; if there are no movies left, do not show imgBtn
     * @param movieId id of movie; if -1, do not show imgBtn
     * @param view the view of the widget that the imgBtn belongs to
     * @param imgBtn image button of movie
     */

    private void showMovie(int movieId, View view, int imgBtn){
        if (movieId != -1) {
            ImageButton movieImgBtn = view.findViewById(imgBtn);
            setMovieImage(movieId, movieImgBtn);
            movieImgBtn.setOnClickListener(btn -> {        //movie 3
                Bundle movInt = new Bundle();
                movInt.putInt("movieId", movieId);
                ((NavigationActivity)requireActivity()).navTo(R.id.movieFragment, movInt);
            });
            movieImgBtn.setVisibility(VISIBLE);
        }
    }
}