package com.flickpick.flickpick.ui.movie;

import static android.view.View.GONE;
import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.Constants.GENRES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.GENRE_MOVIES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.MOVIES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.MOVIE_REVIEWS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.FragmentMovieScreenBinding;
import com.flickpick.flickpick.ui.admin.EditMovieFragment;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;

import java.util.Random;

/**
 * Code for the movie fragment
 * @author Liam Janda
 */

public class MovieFragment extends Fragment {
    public static final String ID = "movieId";

    private FragmentMovieScreenBinding binding;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMovieScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle info = requireArguments();
        int movieId = info.getInt(ID);
        boolean isFav = false;

        TextView titleText = binding.movieTitleText;
        TextView genreText = binding.genreText;
        TextView ratingText = binding.ratingText;
        TextView minText = binding.minText;
        TextView yearText = binding.yearText;
        TextView descText = binding.descText;

        Button setFavoriteBtn = binding.setFavoriteBtn;
        Button addWatchlistBtn = binding.addWatchlistBtn;
        Button reviewsBtn = binding.reviewsBtn;

        ImageView movieImg = binding.movieImg;

        ImageButton recMovie1ImgBtn = binding.recMovie1ImgBtn;
        ImageButton recMovie2ImgBtn = binding.recMovie2ImgBtn;
        ImageButton recMovie3ImgBtn = binding.recMovie3ImgBtn;

        LoginRepository r = LoginRepository.getInstance();      //get user id

        if (r != null && r.isLoggedIn()) {
            int user = r.getUser().getUserType().getPrivilege();
            if (user > LoggedInUser.UserType.Guest.getPrivilege()) {
                setFavoriteBtn.setVisibility(View.VISIBLE);
                addWatchlistBtn.setVisibility(View.VISIBLE);
                if (user > LoggedInUser.UserType.User.getPrivilege())
                    binding.movieEditBtn.setVisibility(View.VISIBLE);

                if (r.getUser().getFavMov() == movieId) {
                    setFavoriteBtn.setText(R.string.unfavorite_movie);
                    isFav = true;
                }
            }
        }

        binding.movieEditBtn.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt(EditMovieFragment.TYPE, EditMovieFragment.EDIT);
            b.putInt(EditMovieFragment.MOVIE, movieId);
            ((NavigationActivity)requireActivity()).navTo(R.id.editMovieFragment, b);
        });

        boolean finalIsFav = isFav; //needed for use in lambda
        setFavoriteBtn.setOnClickListener(view -> {             //set fav movie
            int userId;

            assert r != null; //button only visible when logged in
            if(r.isLoggedIn(true)){          //checks not dummy user
                userId = r.getUser().getUserId();
                int toSet = finalIsFav ? -1 : movieId;
                JsonObjectRequest updateFavMovie = new JsonObjectRequest(Request.Method.PUT, USERS_BASE_URL + userId + "/updateMovie/" + toSet, null,     //sets fav movie
                    response -> {
                        if (response != null) //TODO is this how to check if success?
                            if (finalIsFav) Toast.makeText(getContext(), getString(R.string.unfavorite_success, titleText.getText()), Toast.LENGTH_SHORT).show();
                            else Toast.makeText(getContext(), getString(R.string.favorite_success, titleText.getText()), Toast.LENGTH_SHORT).show();
                        else
                            if (finalIsFav) Toast.makeText(getContext(), getString(R.string.unfavorite_fail, titleText.getText()), Toast.LENGTH_SHORT).show();
                            else Toast.makeText(getContext(), getString(R.string.favorite_fail, titleText.getText()), Toast.LENGTH_SHORT).show();
                    }, error -> {
                        System.out.println(" ");
                        Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
                );
                AppController.getInstance().addToRequestQueue(updateFavMovie);
            }
            else{                                               //if not
                Toast.makeText(getContext(), "Can not set favorite movie when not logged in.", Toast.LENGTH_SHORT).show();
            }

        });

        addWatchlistBtn.setOnClickListener(view -> {                        //add movie to watchlist
            //TODO: add when watchlist is implemented for backend
            Toast.makeText(getContext(), "Not yet Implemented!", Toast.LENGTH_SHORT).show();
        });

        JsonObjectRequest movieInfoRequest = new JsonObjectRequest(Request.Method.GET, MOVIES_BASE_URL + movieId, null,         //get info on movie
                response -> {
                    try {
                        String title = AppController.getMovieTitleId(response.getString("title"), movieId);
                        int minutes = response.getInt("minutes");
                        int year = response.getInt("year");
                        double rating = response.getDouble("rating");
                        //TODO: cannot use text in place of rating bar. Is this necessary?

                        // JsonArrayRequest getReviewsRequest = new JsonArrayRequest(Request.Method.GET, MOVIE_REVIEWS_BASE_URL + movieId, null,			//GET reviews of movie
                        //         response2 -> {
                        //             if(response2.length() == 0){
                        //                 ratingText.setText("No Ratings");
                        //             }
                        //             else{
                        //                 ratingText.setText("Rating: " + ((Math.round(rating * 100.0))/100.0) + "/5.0");
                        //             }

                        //         }, error -> {
                        //     //TODO
                        // });
                        // AppController.getInstance().addToRequestQueue(getReviewsRequest);
                        String description = response.getString("description");

                        titleText.setText(title);                                   //set xml info
                        ((NavigationActivity)requireActivity()).setTitle(title);
                        binding.movieFragmentRating.setRating((float)rating);
                        minText.setText("Minutes: " + minutes);
                        yearText.setText("Year: " + year);
                        descText.setText(description);
                        setMovieImage(movieId, movieImg);

                        reviewsBtn.setOnClickListener( view -> {                    //nav to review screen for movie
                            Bundle movInfo = new Bundle();
                            movInfo.putInt("movieId", movieId);
                            movInfo.putString("movieName", title);
                            ((NavigationActivity)requireActivity()).navTo(R.id.reviewFragment, getString(R.string.review_title, title), movInfo);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                ,error -> {
                     System.out.println(" ");
                     Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();

        });

        AppController.getInstance().addToRequestQueue(movieInfoRequest);

        JsonObjectRequest movieGenreRequest = new JsonObjectRequest(Request.Method.GET, GENRES_BASE_URL + movieId, null,        //get genres of movie
                response2 -> {
                    try {
                        int numGenres = 0;
                        String genre1 = response2.getString("genre1");
                        String genre2 = response2.getString("genre2");
                        String genre3 = response2.getString("genre3");

                        String text = "";
                        if (!genre3.equals("")) {           //3 genres
                            text = ", " + AppController.capitalCase(genre3);
                            numGenres++;
                        }
                        if (!genre2.equals("")){            //2 genres
                            text = ", " + AppController.capitalCase(genre2) + text;
                            numGenres++;
                        }
                        if (!genre1.equals("")){            //1 genres
                            text = AppController.capitalCase(genre1) + text;
                            numGenres++;
                        }
                        else {
                            text = getString(R.string.genre_list_zero);         //if there are no genres, remove suggestions
                            recMovie1ImgBtn.setVisibility(GONE);
                            recMovie2ImgBtn.setVisibility(GONE);
                            recMovie3ImgBtn.setVisibility(GONE);
                            numGenres = 0;
                        }
                        genreText.setText(text);

                        if(numGenres != 0){
                            decideSuggMovies(recMovie1ImgBtn, recMovie2ImgBtn, recMovie3ImgBtn, movieId, genre1);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    System.out.println(" ");
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });
        AppController.getInstance().addToRequestQueue(movieGenreRequest);


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
     * sets movie image of image view
     * @param movieId id of movie
     * @param imgView image view of movie
     */

    private void setMovieImage(int movieId, ImageView imgView){
        JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, MOVIES_BASE_URL + movieId + "/picture", null,      //get movie pic
                response -> {
                    if(response != null){
                        try {
                            String base64 = response.getString("picture");
                            Bitmap image = base64ToBitmap(base64);
                            imgView.setImageBitmap(image);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
                     Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });
        AppController.getInstance().addToRequestQueue(getImage);
    }

    private void setMovieImage(int movieId, ImageButton imgBtn){
        JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, MOVIES_BASE_URL + movieId + "/picture", null,      //get movie pic
                response -> {
                    try {
                        String base64 = response.getString("picture");
                        Bitmap image = base64ToBitmap(base64);
                        imgBtn.setImageBitmap(image);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    //TODO
        });
        AppController.getInstance().addToRequestQueue(getImage);
    }

    private void setSugg(ImageButton imgBtn, int movieId){
        setMovieImage(movieId, imgBtn);
        imgBtn.setOnClickListener(v -> {
            Bundle movInt = new Bundle();
            movInt.putInt("newMovie", movieId);
            ((NavigationActivity)requireActivity()).navTo(R.id.homeFragment, getString(R.string.loading), movInt);
        });
    }
    private void decideSuggMovies(ImageButton recMovie1ImgBtn, ImageButton recMovie2ImgBtn, ImageButton recMovie3ImgBtn, int movieId, String genre1){
        JsonArrayRequest getMovies = new JsonArrayRequest(Request.Method.GET, GENRE_MOVIES_BASE_URL + genre1, null,      //get genre movies
                response -> {
                    int currBtn = 1;
                    Random randNum = new Random();
                    while(response.length() != 0){
                        int randMovie = randNum.nextInt(response.length());
                        try {
                            if(response.getJSONObject(randMovie).getInt("id") == movieId){
                                response.remove(randMovie);
                                continue;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(currBtn == 1){
                            try {
                                setSugg(recMovie1ImgBtn, response.getJSONObject(randMovie).getInt("id"));
                                response.remove(randMovie);
                                ++currBtn;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(currBtn == 2){
                            try {
                                setSugg(recMovie2ImgBtn, response.getJSONObject(randMovie).getInt("id"));
                                response.remove(randMovie);
                                ++currBtn;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else if(currBtn == 3){
                            try {
                                setSugg(recMovie3ImgBtn, response.getJSONObject(randMovie).getInt("id"));
                                response.remove(randMovie);
                                break;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, error -> {
            //TODO
        });
        AppController.getInstance().addToRequestQueue(getMovies);
    }
}