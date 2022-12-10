package com.flickpick.flickpick.ui.profile;

import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.Constants.BASE_URL;
import static com.flickpick.flickpick.util.Constants.FRIENDS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.FRIENDS_RELATIONS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.FragmentFriendRequestBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Code for the friend request fragment
 * @author Liam Janda
 */

public class FriendRequestFragment extends Fragment {

    private FragmentFriendRequestBinding binding;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFriendRequestBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout requestsLinearLayout = binding.requestsLinearLayout;

        Bundle info = getArguments();
        int userId = info.getInt("userId");

        getRequests(userId, requestsLinearLayout);

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
     * gets the list of friend requests to user from server and adds widgets to layout
     * @param userId id of curr user
     * @param requestsLinearLayout layout to add widget to
     */

    private void getRequests(int userId, LinearLayout requestsLinearLayout){
        JsonArrayRequest getRequests = new                                                                 //create request to get all relationships of user
                JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,
                response -> {
                    //TODO: all responses, will figure out later
                        try {
                            for (int i = 0; i < response.length(); ++i) {                                   //for all friendships of user
                                JSONObject currFriendship = response.getJSONObject(i);
                                int user1 = currFriendship.getInt("user1");
                                int user2 = currFriendship.getInt("user2");
                                int friendshipId = currFriendship.getInt("id");
                                String friendType = currFriendship.getString("friend_type");

                                if (user2 == userId && friendType.equals("requested")) {                           //if friend type = requested and user2 = userId (user1 sends request to user2)
                                    addRequestWidget(user1, friendshipId, requestsLinearLayout);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }, error -> {
                    System.out.println(" ");
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(getRequests);
    }

    /**
     * adds request widget to layout
     * @param reqUserId id of user that requested freindship
     * @param friendshipId id of requested friendship
     * @param requestsLinearLayout layout to add the widget to
     */

    private void addRequestWidget(int reqUserId, int friendshipId, LinearLayout requestsLinearLayout){

        View widget = getLayoutInflater().inflate(R.layout.widget_friend_request, null);

        JsonObjectRequest getRequestUserInfo = new JsonObjectRequest(Request.Method.GET,         //get info on curr request user
                (USERS_BASE_URL + reqUserId), null,
                response -> {
                    try {
                        String request = response.getString("username");        //get info from JSON object
                        if (LoginRepository.getInstance().getUser().getUserType().getPrivilege() > LoggedInUser.UserType.User.getPrivilege()) {
                            request += "#" + reqUserId;
                        }
                        String requestUsername = request; //necessary for lambdas

                        Button requestUserBtn = widget.findViewById(R.id.requestUserBtn);     //set request username
                        requestUserBtn.setText(requestUsername);


                        ImageButton pfp = widget.findViewById(R.id.pfpReqBtn);              //set pfp
                        setUserPfp(reqUserId, pfp);

                        pfp.setOnClickListener(view -> {                            //clicked requests user's profile button
                            Bundle info = new Bundle();
                            info.putInt("userId", reqUserId);
                            ((NavigationActivity) requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info);
                        });



                        requestUserBtn.setOnClickListener(view ->     {                          //clicked requests user's profile button
                            Bundle info = new Bundle();
                            info.putInt("userId", reqUserId);
                            ((NavigationActivity) requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info);
                        });


                        Button requestAcceptBtn = widget.findViewById(R.id.requestAcceptBtn);

                        requestAcceptBtn.setOnClickListener(view -> {                       //accept req
                            acceptRequest(friendshipId, widget, requestsLinearLayout);
                        });

                        Button requestRejectBtn = widget.findViewById(R.id.requestRejectBtn);

                        requestRejectBtn.setOnClickListener(view -> {                       //reject req
                            rejectRequest(friendshipId, widget, requestsLinearLayout);
                        });

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
                    System.out.println(" ");
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(getRequestUserInfo);              //send req

        requestsLinearLayout.addView(widget);


    }

    /**
     * accepts request: changes friendship_type to accepted
     * @param friendshipId id of friendship
     * @param widget request widget
     * @param requestsLinearLayout layout for request widgets
     */

    private void acceptRequest(int friendshipId, View widget, LinearLayout requestsLinearLayout) {
        JsonObjectRequest acceptRequest = new JsonObjectRequest(Request.Method.PUT, BASE_URL + "/friends/" + friendshipId + "/UpdateFriendsType/accepted", null,
                response -> {
                    if(response != null) {
                        Toast.makeText(getContext(), "Friend Added.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getContext(), "Accept failed, friend request no longer exists.", Toast.LENGTH_SHORT).show();
                    }
                    requestsLinearLayout.removeView(widget);
                }, error -> {
                     Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(acceptRequest);

    }

    /**
     * rejects request: deletes friendship
     * @param friendshipId id of friendship
     * @param widget request widget
     * @param requestsLinearLayout layout for request widgets
     */

    private void rejectRequest(int friendshipId, View widget, LinearLayout requestsLinearLayout){
        JsonObjectRequest rejectRequest = new JsonObjectRequest(Request.Method.DELETE, FRIENDS_RELATIONS_BASE_URL + friendshipId, null,
                response -> {
                    try {
                        String resString = response.getString("message");
                        if(resString.equals("success")) {
                            Toast.makeText(getContext(), "Request Rejected.", Toast.LENGTH_SHORT).show();
                            requestsLinearLayout.removeView(widget);
                        }
                        else if (resString.equals("Friendship with Id does not exist")){
                            Toast.makeText(getContext(), "Reject failed, friend request no longer exists.", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(rejectRequest);
    }

    /**
     * sets image button for pfp of request user
     * @param userId id of request user
     * @param imgBtn img button of request user
     */

    private void setUserPfp(int userId, ImageButton imgBtn){
        JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + userId + "/pfp", null,      //get movie pic
                response -> {
                    if(response != null) {
                        try {
                            String base64 = response.getString("pfp");
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

}