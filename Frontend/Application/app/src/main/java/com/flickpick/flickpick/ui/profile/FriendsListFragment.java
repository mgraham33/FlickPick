package com.flickpick.flickpick.ui.profile;

import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.Constants.FRIENDS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.FRIENDS_RELATIONS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.FRIENDS_RELATIONS_URL;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;
import static java.lang.String.valueOf;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.FragmentFriendsListBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Code for the friend list fragment
 * @author Liam Janda
 */

public class FriendsListFragment extends Fragment {
    FragmentFriendsListBinding binding;
    LinearLayout scrollLayout;
    int userId;

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
        super.onCreate(savedInstanceState);
        binding = FragmentFriendsListBinding.inflate(getLayoutInflater());

        LoginRepository r = LoginRepository.getInstance();      //get user id
        if(r!=null && r.isLoggedIn(true)){
            userId = r.getUser().getUserId();
        }
        else{
            Toast.makeText(getContext(), "Cannot view friends when not logged in.", Toast.LENGTH_SHORT).show();
            //TODO: redirect user to start page or something
        }
        Button friendRequestBtn = binding.friendRequestBtn;
        Button receiveFriendRequestBtn = binding.receiveFriendRequestBtn;


        receiveFriendRequestBtn.setOnClickListener(view -> {                //when 'incoming friend requests' button is clicked
            Bundle info = new Bundle();
            info.putInt("userId", userId);
            ((NavigationActivity)requireActivity()).navTo(R.id.friendRequestFragment, "Friend Requests", info);

        });

        friendRequestBtn.setOnClickListener(view -> {                                           //when 'add friend' button clicked
            AlertDialog.Builder friendRequestDialog = new AlertDialog.Builder(getContext());        //creating alert dialog
            friendRequestDialog.setTitle("Send Friend Request");

            EditText friendUsername = new EditText(getContext());
            friendUsername.setHint("Enter Friend's Username");
            friendRequestDialog.setView(friendUsername);

            //send friend request
            friendRequestDialog.setPositiveButton("Send", (dialog, which) -> {                      //if send is clicked
                requestAttempt(friendUsername);
            });
            friendRequestDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());        //if cancel pressed
            friendRequestDialog.show();

        });

        JsonArrayRequest getFriends = new                                                                 //create request to get all friends
                JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,
                response -> {
                    //TODO: check if all responses are covered, will figure out later
                    try{
                        for (int i = 0; i < response.length(); ++i) {                                   //for all friendships of user
                            JSONObject currFriendship = response.getJSONObject(i);
                            int user1 = currFriendship.getInt("user1");
                            int user2 = currFriendship.getInt("user2");
                            int friendshipId = currFriendship.getInt("id");

                            String friendType = currFriendship.getString("friend_type");

                            if(user1 != userId && friendType.equals("accepted")){                           //if friend type = accepted
                                addFriendWidget(user1, friendshipId);
                            }
                            else if (user2 != userId && friendType.equals("accepted")) {                    //if friend type = accepted
                                addFriendWidget(user2, friendshipId);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    System.out.println(" ");
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                });

        AppController.getInstance().addToRequestQueue(getFriends);

        scrollLayout = binding.scrollLayout;
        
        return binding.getRoot();
    }

    /**
     * adds widget of a friend
     * @param friendId id of friend
     * @param friendshipId id of friendship
     */

    public void addFriendWidget(int friendId, int friendshipId){

        JsonObjectRequest getFriend = new JsonObjectRequest(Request.Method.GET,         //get info on curr friend
                (USERS_BASE_URL + friendId), null,
                response -> {
                    try {
                        String friend = response.getString("username");
                        if (LoginRepository.getInstance().getUser().getUserType().getPrivilege() > LoggedInUser.UserType.User.getPrivilege()) {
                            friend += "#" + friendId;
                        }
                        String friendName = friend; //necessary for lambdas

                        View view = getLayoutInflater().inflate(R.layout.widget_friends_list, null);            //create widget

                        Button friendBtn = view.findViewById(R.id.friendBtn);
                        ImageButton pfpBtn = view.findViewById(R.id.pfpBtn);
                        Button friendMsgBtn = view.findViewById(R.id.friendMsgBtn);
                        Button friendDelBtn = view.findViewById(R.id.friendDelBtn);
                        friendBtn.setText(friendName);

                        setFriendPfp(friendId, pfpBtn);
                        pfpBtn.setOnClickListener(v -> {                                                           //pressed friend profile button
                            try {
                                Bundle info = new Bundle();
                                info.putInt("userId", response.getInt("id"));
                                ((NavigationActivity) requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });



                        friendBtn.setOnClickListener(v -> {                                                           //pressed friend profile button
                            try {
                                Bundle info = new Bundle();
                                info.putInt("userId", response.getInt("id"));
                                info.putInt("friendshipId", response.getInt("friendshipId"));

                                ((NavigationActivity) requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });

                        friendMsgBtn.setOnClickListener(v -> {                                                  //pressed friend msg button
                            Bundle info = new Bundle();
                            info.putInt("id", friendId);
                            info.putString("friendName", friendName);
                            ((NavigationActivity)requireActivity()).navTo(R.id.messagingFragment, ("Messaging " + friendName), info);
                        });

                        friendDelBtn.setOnClickListener(v -> {                                                           //pressed friend delete button
                            AlertDialog.Builder unfriendDialog = new AlertDialog.Builder(requireContext());
                            unfriendDialog.setTitle("Message");
                            unfriendDialog.setMessage("Are you sure you want to unfriend " + friendName + "?");


                                unfriendDialog.setPositiveButton("Yes", (dialog, which) -> {

                                    JsonObjectRequest deleteFriendshipRequest = new JsonObjectRequest(Request.Method.DELETE,         //delete friend
                                            FRIENDS_RELATIONS_BASE_URL + friendshipId, null,
                                            response1 -> {
                                                try {
                                                    if (response1.getString("message").equals("success")) {
                                                        Toast.makeText(getContext(), "Friend Removed", Toast.LENGTH_SHORT).show();
                                                    } else if (response1.getString("message").equals("Friendship with Id does not exist")) {
                                                        Toast.makeText(getContext(), "You are not friends with this user", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }, error -> {
                                        System.out.println(" ");
                                        Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                                    });

                                    AppController.getInstance().addToRequestQueue(deleteFriendshipRequest);
                                    scrollLayout.removeView(view);
                                });
                                unfriendDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());                     //remove friend was canceled
                                unfriendDialog.show();
                            });

                            scrollLayout.addView(view);
                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                    System.out.println(" ");
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(getFriend);
    }

    /**
     * sets image button for pfp of request user
     * @param friendId id of friend
     * @param imgBtn img button of friend
     */

    private void setFriendPfp(int friendId, ImageButton imgBtn){
        JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + friendId + "/pfp", null,      //get movie pic
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

    /**
     * checks if request can be sent. if not, gives reason why
     * @param response friendships of user
     * @param currUser JSONObject of requested friend
     * @return if friend was added
     */

    private boolean findFriend(JSONArray response, JSONObject currUser) throws JSONException {
        boolean friendFound = false;
        for (int j = 0; j < response.length(); ++j) {                              //for each friend relation

            JSONObject currFriendship = response.getJSONObject(j);
            int user1 = currFriendship.getInt("user1");
            int user2 = currFriendship.getInt("user2");
            String friendType = currFriendship.getString("friend_type");
            if(user1 == currUser.getInt("id") && user2 == userId && friendType.equals("blocked")){        //if request recipient blocked the user
                Toast.makeText(getContext(), "You are unable to send a request to this user.", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }
            else if(user1 == userId && user2 == currUser.getInt("id") && friendType.equals("requested")){        //if request was already sent by you
                Toast.makeText(getContext(), "You already sent a request to this user", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }
            else if(user1 == userId && user2 == currUser.getInt("id") && friendType.equals("blocked")){
                Toast.makeText(getContext(), "You have this user blocked!", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }
            else if(currUser.getString("username").equals("guest")){
                Toast.makeText(getContext(), "You can not friend guest!", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }
            else if(userId == currUser.getInt("id")){
                Toast.makeText(getContext(), "You can not friend yourself!", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }
            else if(user1 == currUser.getInt("id") && user2 == userId && friendType.equals("requested")){           //if you have a request sent by the other person
                Toast.makeText(getContext(), "You already have a request from this user", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }
            else if((user1 == currUser.getInt("id") && user2 == userId && friendType.equals("accepted"))||(user1 == userId && user2 == currUser.getInt("id") && friendType.equals("accepted"))){        //if already friends
                Toast.makeText(getContext(), "You already friends with this user", Toast.LENGTH_SHORT).show();
                friendFound = true;
                break;
            }

        }
        return friendFound;
    }

    /**
     * attempts to send a request. if able, send a request. if not, give reason why
     * @param friendUsername name of user to attempt to send request to
     */

    private void requestAttempt(EditText friendUsername) {      //attempts to send request
        String friendUserString = friendUsername.getText().toString();

        JsonArrayRequest getUsers = new                                                                 //create request to get all users
                JsonArrayRequest(Request.Method.GET, USERS_BASE_URL, null,
                response -> {
                    try {
                        boolean userExists = false;
                        for (int i = 0; i < response.length(); ++i) {                               //search if username is valid
                            JSONObject currUser = response.getJSONObject(i);

                            if(currUser.getString("username").equals(friendUserString)){            //username is valid
                                userExists = true;

                                JsonArrayRequest getFriends = new                                                                 //create request to get friends of user
                                        JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,
                                        response2 -> {
                                            //TODO: check if all responses are covered, will fix later

                                            try{
                                                boolean friendFound = findFriend(response2, currUser);

                                                if(!friendFound){                                                                   //if allowed to be friends
                                                    JSONObject postParam = new JSONObject();

                                                    try {
                                                        postParam.put("user1", valueOf(userId));                            //request parameters for friend request
                                                        postParam.put("user2", valueOf(currUser.getInt("id")));
                                                        postParam.put("friend_type", "requested");

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,         //posts friend request
                                                            FRIENDS_RELATIONS_URL, postParam,
                                                            response1 -> {
                                                                if(response1 == null){
                                                                    Toast.makeText(getContext(), "You or the other user does not exist", Toast.LENGTH_SHORT).show();
                                                                }
                                                                else {
                                                                    try {
                                                                        String resString = response1.getString("message");
                                                                        if (resString.equals("success")) {
                                                                            Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show();
                                                                        } else if (resString.equals("failure")) {
                                                                            Toast.makeText(getContext(), "Failed to send friend request", Toast.LENGTH_SHORT).show();
                                                                        } else if (resString.equals("A user cannot be friends with itself.")) {
                                                                            Toast.makeText(getContext(), "A user cannot be friends with itself.", Toast.LENGTH_SHORT).show();
                                                                        } else if (resString.equals("Friendship with Id does not exist")) {
                                                                            Toast.makeText(getContext(), "Friend request did not go through, please try again later.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }


                                                            }, error -> {
                                                                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                                                                    System.out.println(" ");
                                                    });
                                                    AppController.getInstance().addToRequestQueue(postRequest);
                                                }

                                            } catch (JSONException e) {
                                                    e.printStackTrace();
                                            }

                                        }, error -> {
                                                System.out.println(" ");
                                                Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                                });

                                AppController.getInstance().addToRequestQueue(getFriends);


                            }

                        }
                        if(!userExists){                                                                                //if user not found
                            Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                            e.printStackTrace();
                    }
                }, error -> {
                        System.out.println(" ");
                        Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(getUsers);


    }
}