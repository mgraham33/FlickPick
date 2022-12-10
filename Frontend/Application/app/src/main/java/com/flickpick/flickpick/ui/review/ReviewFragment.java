package com.flickpick.flickpick.ui.review;

import static com.flickpick.flickpick.util.Constants.FRIENDS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.MOVIE_REVIEWS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import com.flickpick.flickpick.databinding.FragmentReviewScreenBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Code for the review fragment
 * @author Liam Janda
 */

public class ReviewFragment extends Fragment {

    private FragmentReviewScreenBinding binding;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentReviewScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        LinearLayout reviewLinearLayout = binding.reviewLinearLayout;

        LoginRepository r = LoginRepository.getInstance();      //get user id

        Button createReviewBtn = binding.createReviewBtn;
        TextView reviewTitleText = binding.reviewTitleText;

        boolean loggedIn = (r!=null && r.isLoggedIn(true));                           //checks if user is logged in
        if (r != null && r.isLoggedIn() && r.getUser().getUserType().getPrivilege() >= LoggedInUser.UserType.User.getPrivilege()) {
            createReviewBtn.setVisibility(View.VISIBLE);
        }

        Bundle movInfo = getArguments();
        int movieId = movInfo.getInt("movieId");
        String movieName = movInfo.getString("movieName");

        reviewTitleText.setText("Reviews for " + "\"" +  movieName + "\"");

        if(loggedIn){                               //get reviews as a user
            int userId = r.getUser().getUserId();
            getReviews(movieId, userId, reviewLinearLayout);

            createReviewBtn.setOnClickListener(view -> {       //create review nav
                //TODO: don't allow someone to post more than one review
                Bundle reviewInfo = movInfo;
                reviewInfo.putInt("userId", userId);
                ((NavigationActivity)requireActivity()).navTo(R.id.createReviewFragment, getString(R.string.reviewing_title, movieName), reviewInfo);
            });
        }
        else{                                           //get reviews as a guest
            getReviews(movieId, reviewLinearLayout);

            createReviewBtn.setOnClickListener(view -> Toast.makeText(getContext(), "Can not create a review if you are not signed in!", Toast.LENGTH_SHORT).show());
        }

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
     * adds a review widget to the layout
     * @param currReview JSON of review to show
     * @param reviewLinearLayout layout to add widget to
     */

    private void addReviewWidget(JSONObject currReview, LinearLayout reviewLinearLayout) throws JSONException {
        int rating = currReview.getInt("rating");                   //info on review
        int reviewUserId = currReview.getInt("userid");
        String reason = currReview.getString("reason");

        View view = getLayoutInflater().inflate(R.layout.widget_review_screen, null);
        TextView reviewBodyText = view.findViewById(R.id.reviewBodyText);
        RatingBar ratingBar = view.findViewById(R.id.ratingBar);

        setReviewTitle(reviewUserId, view);
        reviewBodyText.setText("\"" + reason + "\"");
        ratingBar.setRating(rating);

        view.setOnClickListener(v -> {
            Bundle info = new Bundle();
            info.putInt("userId", reviewUserId);
            ((NavigationActivity)requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info);
        });

        reviewLinearLayout.addView(view);               //show review
    }

    /**
     * set title of review widget
     * @param reviewUserId
     * @param view review widget
     */

    private void setReviewTitle(int reviewUserId, View view){
        JsonObjectRequest getUsername = new JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + reviewUserId, null,
                response -> {
                    TextView reviewByText = view.findViewById(R.id.reviewByText);
                    if (response != null) {
                        try {
                            String reviewUserName = response.getString("username");
                            if (LoginRepository.getInstance().getUser().getUserType().getPrivilege() > LoggedInUser.UserType.User.getPrivilege()) {
                                reviewUserName += "#" + reviewUserId;
                            }
    
                            reviewByText.setText("Review By : " + "\"" + reviewUserName + "\"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else reviewByText.setText("Review By : Deleted User");
                }, error -> {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                    System.out.println(" ");

        });
        AppController.getInstance().addToRequestQueue(getUsername);
    }

    /**
     * get reviews and add them to layout. if a blocked user has a review, do not show review
     * @param movieId id of movie
     * @param userId id of user
     * @param reviewLinearLayout layout to add widgets to
     */

    private void getReviews(int movieId, int userId , LinearLayout reviewLinearLayout){

        JsonArrayRequest getReviewsRequest = new JsonArrayRequest(Request.Method.GET, MOVIE_REVIEWS_BASE_URL + movieId, null,			//GET reviews of movie
                response -> {
                    //TODO: do not show review if user is blocked
                    if(response != null){
                        for(int i = 0; i < response.length(); ++i){     //for each review
                            try {
                                JSONObject currReview = response.getJSONObject(i);
                                int reviewUserId = currReview.getInt("userid");
                                JsonArrayRequest getRelations = new JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,			//GET reviews of movie
                                        response2 -> {
                                    boolean isBlocked = false;
                                    for(int j = 0; j < response2.length(); ++j ) {
                                            try {
                                                JSONObject currFriendship = response2.getJSONObject(j);
                                                String relation = currFriendship.getString("friend_type");
                                                int user1 = currFriendship.getInt("user1");
                                                int user2 = currFriendship.getInt("user2");
                                                if(user1 == userId && user2 == reviewUserId && relation.equals("blocked")){
                                                    isBlocked = true;
                                                    break;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                    }
                                    if(!isBlocked){
                                        try {
                                            addReviewWidget(currReview, reviewLinearLayout);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }


                                        }, error -> {
                                            //TODO

                                });
                                AppController.getInstance().addToRequestQueue(getRelations);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Movie does not exist", Toast.LENGTH_SHORT).show();
                    }

                }, error -> {
                     Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                     System.out.println(" ");

        });
        AppController.getInstance().addToRequestQueue(getReviewsRequest);
    }

    /**
     * get reviews and add them to layout. for guest user
     * @param movieId id of movie
     * @param reviewLinearLayout layout to add widgets to
     */

    private void getReviews(int movieId, LinearLayout reviewLinearLayout){
        JsonArrayRequest getReviewsRequest = new JsonArrayRequest(Request.Method.GET, MOVIE_REVIEWS_BASE_URL + movieId, null,			//GET reviews of movie
                response -> {
                    if(response != null) {
                        for (int i = 0; i < response.length(); ++i) {         //for each review
                            try {
                                JSONObject currReview = response.getJSONObject(i);
                                addReviewWidget(currReview, reviewLinearLayout);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else{
                        Toast.makeText(getContext(), "Movie does not exist", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                    System.out.println(" ");

        });
        AppController.getInstance().addToRequestQueue(getReviewsRequest);

    }

}

