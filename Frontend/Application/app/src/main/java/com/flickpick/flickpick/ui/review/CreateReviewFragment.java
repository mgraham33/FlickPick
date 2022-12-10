package com.flickpick.flickpick.ui.review;

import static com.flickpick.flickpick.util.Constants.REVIEWS_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.FragmentCreateReviewScreenBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Code for the create review fragment
 * @author Liam Janda
 */

public class CreateReviewFragment extends Fragment {

    private FragmentCreateReviewScreenBinding binding;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateReviewScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button postReviewBtn = binding.postReviewBtn;
        RatingBar reviewRating = binding.reviewRating;
        EditText commentText = binding.commentText;
        TextView createReviewText = binding.createReviewText;

        Bundle reviewInfo = getArguments();
        String movieName = reviewInfo.getString("movieName");

        createReviewText.setText("Create a Review for " + "\"" + movieName + "\"");

        postReviewBtn.setOnClickListener(view -> {
            int rating = (int)(reviewRating.getRating());
            String reason = commentText.getText().toString();
            reviewInfo.putInt("rating", rating);
            reviewInfo.putString("reason", reason);
            JSONObject requestBody = createRequestBody(reviewInfo);         //create request body for POST req
            postReview(requestBody);                            //POST req
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
     * posts review that was created by user to db
     * @param requestBody json request body with info of review
     */

    private void postReview(JSONObject requestBody){

        JsonObjectRequest postReview = new JsonObjectRequest(Request.Method.POST, REVIEWS_URL, requestBody,
                response -> {
                    try {
                        String resString = response.getString("message");
                        if(resString.equals("success")){
                            Toast.makeText(getContext(), "Review successfully created!", Toast.LENGTH_SHORT).show();
                            ((NavigationActivity)requireActivity()).onBackPressed();
                            //nav back to review page
                        }
                        else if(resString.equals("User does not exist")){
                            Toast.makeText(getContext(), "There was an issue while creating review, please try restarting the app", Toast.LENGTH_SHORT).show();
                        }
                        else if(resString.equals("Movie does not exist")){
                            Toast.makeText(getContext(), resString, Toast.LENGTH_SHORT).show();
                        }
                        else if (resString.equals("Rating is out of the given range")){
                            Toast.makeText(getContext(), resString, Toast.LENGTH_SHORT).show();
                        }
                        else if(resString.equals("Review with Id does not exist")){
                            Toast.makeText(getContext(), "There was an issue while creating review, please try restarting the app", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });
        AppController.getInstance().addToRequestQueue(postReview);
    }

    /**
     * creates request body for review
     * @param reviewInfo info on review
     * @return request body for post request
     */

    private JSONObject createRequestBody(Bundle reviewInfo){
        int rating = reviewInfo.getInt("rating");
        int movieId = reviewInfo.getInt("movieId");
        int userId = reviewInfo.getInt("userId");
        String reason = reviewInfo.getString("reason");

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("rating", rating);
            requestBody.put("movieid", movieId);
            requestBody.put("userid", userId);
            requestBody.put("reason", reason);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return requestBody;
    }

}