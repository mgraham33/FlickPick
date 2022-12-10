package com.flickpick.flickpick.ui.profile;

import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.Constants.FRIENDS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.FRIENDS_RELATIONS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.FRIENDS_RELATIONS_URL;
import static com.flickpick.flickpick.util.Constants.MOVIES_BASE_URL;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;
import static java.lang.String.valueOf;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.flickpick.flickpick.databinding.FragmentOtherUserProfileBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OtherUserProfileFragment extends Fragment {

    private FragmentOtherUserProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOtherUserProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView usernameTxt = binding.usernameTxt;
        TextView userTypeTxt = binding.userTypeTxt;
        ImageView profPicImg = binding.profPicImg;
        TextView favMovTxt = binding.favMovTxt;
        Button blkUnblkBtn = binding.blkUnblkBtn;
        Button friendReqBtn = binding.friendReqBtn;
        Button favMovieBtn = binding.favMovieBtn;

        Bundle info = getArguments();

        int userId = info.getInt("userId");

        favMovieBtn.setOnClickListener(v -> {
            JsonObjectRequest getUser = new                                                                 //create request to get all users
                    JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + userId, null,
                    response -> {
                        if (response != null) {
                            try {
                                Bundle movieInfo = new Bundle();
                                movieInfo.putInt("movieId", response.getInt("id"));
                                ((NavigationActivity) requireActivity()).navTo(R.id.movieFragment, "Movie", movieInfo);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            //TODO: user dne
                        }
                    },error -> {

            });
            AppController.getInstance().addToRequestQueue(getUser);

            //TODO: send user to movie fragment

        });


        LoginRepository r = LoginRepository.getInstance();      //get user id
        if(r!=null && r.isLoggedIn(true) && !(r.getUser().getDisplayName().equals("guest"))){
            int id = r.getUser().getUserId();

            setBlockButtonFunctionality(id, userId, blkUnblkBtn);

            setInfo(userId, userTypeTxt, usernameTxt, profPicImg, favMovTxt);

            friendReqBtn.setOnClickListener(v -> requestAttempt(id, userId));

        }
        else{
            setInfo(userId, userTypeTxt, usernameTxt, profPicImg, favMovTxt);

            friendReqBtn.setOnClickListener(v -> Toast.makeText(getContext(), "Cannot send friend request when not logged in!", Toast.LENGTH_SHORT).show());

            blkUnblkBtn.setOnClickListener(view -> Toast.makeText(getContext(), "Cannot block when not logged in!", Toast.LENGTH_SHORT).show());

            //TODO: not logged in
        }


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setInfo(int userId, TextView userTypeTxt, TextView usernameTxt, ImageView profPicImg, TextView favMovTxt) {
        JsonObjectRequest setUsername = new JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + userId, null, response -> {
            try {
                String username = response.getString("username");
                String userType = response.getString("userType");
                int movieId = response.getInt("id");

                usernameTxt.setText(username);
                userTypeTxt.setText(userType);

                setPfpImage(userId, profPicImg);

                setMovieTxt(movieId, favMovTxt);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //TODO: all responses
        }, error -> {
            //TODO: error response
        });
        AppController.getInstance().addToRequestQueue(setUsername);
    }

    private void setPfpImage(int userId, ImageView imgView) {
        JsonObjectRequest getImage = new JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + userId + "/pfp", null,      //get movie pic
                response -> {
                    try {
                        String base64 = response.getString("pfp");
                        Bitmap image = base64ToBitmap(base64);
                        imgView.setImageBitmap(image);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
        });
        AppController.getInstance().addToRequestQueue(getImage);
    }

    private void setMovieTxt(int movieId, TextView favMovTxt) {
        JsonObjectRequest setMovie = new JsonObjectRequest(Request.Method.GET, MOVIES_BASE_URL + movieId, null, response -> {
            try {
                String favMovString = response.getString("title");
                favMovTxt.setText("Favorite Movie: " + favMovString);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            //TODO: error response
        });
        AppController.getInstance().addToRequestQueue(setMovie);

        //TODO

    }

    private boolean findFriend(JSONArray response, JSONObject currUser, int userId) throws JSONException {
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

        }
        return friendFound;
    }


    public void setBlockButtonFunctionality(int userId, int currUser, Button blkUnblkBtn){
        JsonArrayRequest findBlock = new
                JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,
                response2 -> {
                    try {
                        boolean blockFound = false;
                        int friendshipId;
                        for (int j = 0; j < response2.length(); ++j) {                              //for each friend relation
                            JSONObject currFriendship = response2.getJSONObject(j);

                            int user1 = currFriendship.getInt("user1");
                            int user2 = currFriendship.getInt("user2");
                            String friendType = currFriendship.getString("friend_type");
                            if (user1 == userId && user2 == currUser && friendType.equals("blocked")) {        //if user blocked the 'friend'
                                blockFound = true;
                                friendshipId = currFriendship.getInt("id");
                                unblockButton(blkUnblkBtn, friendshipId, userId, currUser);

                                break;
                            }
                        }
                        if(!blockFound){
                            blockButton(blkUnblkBtn, userId, currUser);
                        }
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                }, error -> {
                     System.out.println(" ");

                        //TODO: error response if needed
        });
        AppController.getInstance().addToRequestQueue(findBlock);

    }

    private void unblockButton(Button blkUnblkBtn, int friendshipId, int userId, int currUser){
        blkUnblkBtn.setText("Unblock");

        blkUnblkBtn.setOnClickListener(v -> {
            //TODO: unblock
            JsonObjectRequest deleteFriendshipRequest = new JsonObjectRequest(Request.Method.DELETE,         //delete friend request
                    FRIENDS_RELATIONS_BASE_URL + friendshipId, null,
                    response1 -> {
                        Toast.makeText(getContext(), "User Unblocked", Toast.LENGTH_SHORT).show();
                        setBlockButtonFunctionality(userId, currUser, blkUnblkBtn);

                    }, error -> {
                Toast.makeText(getContext(), "Failed to unblock user", Toast.LENGTH_SHORT).show();

                System.out.println(" ");
                //TODO: error response if needed
            });

            AppController.getInstance().addToRequestQueue(deleteFriendshipRequest);
        });
    }

    private void blockButton(Button blkUnblkBtn, int userId, int currUser){
        blkUnblkBtn.setText("Block");
        blkUnblkBtn.setOnClickListener(v -> {
            //TODO: block
            if(!(userId == currUser)) {
                JsonArrayRequest getFriends = new                                                                 //create request to get friends of user
                        JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,
                        response2 -> {
                            try {

                                for (int j = 0; j < response2.length(); ++j) { //for each friend relation

                                    JSONObject currFriendship = response2.getJSONObject(j);

                                    int friendshipId = currFriendship.getInt("id");
                                    int user1 = currFriendship.getInt("user1");
                                    int user2 = currFriendship.getInt("user2");
                                    String friendType = currFriendship.getString("friend_type");
                                    if ((user1 == currUser && user2 == userId && friendType.equals("accepted")) || (user1 == userId && user2 == currUser && friendType.equals("accepted")) || user1 == currUser && user2 == userId && friendType.equals("requested") || user1 == userId && user2 == currUser && friendType.equals("requested")) {
                                        JsonObjectRequest deleteFriendshipRequest = new JsonObjectRequest(Request.Method.DELETE,         //delete friend request
                                                FRIENDS_RELATIONS_BASE_URL + friendshipId, null,
                                                response1 -> {
                                                    //TODO
                                                }, error -> {
                                            System.out.println(" ");
                                            //TODO: error response if needed
                                        });
                                        AppController.getInstance().addToRequestQueue(deleteFriendshipRequest);

                                    }
                                }
                                JSONObject postParam = new JSONObject();

                                postParam.put("user1", valueOf(userId));                            //request parameters for friend request
                                postParam.put("user2", valueOf(currUser));
                                postParam.put("friend_type", "blocked");

                                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST,         //posts friend request
                                        FRIENDS_RELATIONS_URL, postParam,
                                        response1 -> {
                                            Toast.makeText(getContext(), "User Blocked", Toast.LENGTH_SHORT).show();
                                            setBlockButtonFunctionality(userId, currUser, blkUnblkBtn);
                                        }, error -> {
                                    System.out.println(" ");
                                    Toast.makeText(getContext(), "Failed to Block User", Toast.LENGTH_SHORT).show();

                                });
                                AppController.getInstance().addToRequestQueue(postRequest);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }, error -> {

                });
                AppController.getInstance().addToRequestQueue(getFriends);
            }
            else{
                Toast.makeText(getContext(), "You can not block yourself", Toast.LENGTH_SHORT).show();
            }


        });
    }


    //TODO: fix after fixing friends list requestAttempt
    private void requestAttempt(int userId, int friendId) {      //attempts to send request
        JsonObjectRequest getUser = new                                                                 //create request to get all users
                JsonObjectRequest(Request.Method.GET, USERS_BASE_URL + friendId, null,
                response -> {
                    if(response != null) {
                        JSONObject currUser = response;

                        JsonArrayRequest getFriends = new                                                                 //create request to get friends of user
                                JsonArrayRequest(Request.Method.GET, FRIENDS_BASE_URL + userId, null,
                                response2 -> {

                                    try {
                                        boolean friendFound = findFriend(response2, currUser, userId);

                                        if (!friendFound) {                                                                   //if allowed to be friends
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
                                                    response1 -> Toast.makeText(getContext(), "Request sent", Toast.LENGTH_SHORT).show(), error -> {
                                                         System.out.println(" ");
                                                         Toast.makeText(getContext(), "Failed to send friend request", Toast.LENGTH_SHORT).show();

                                            });
                                            AppController.getInstance().addToRequestQueue(postRequest);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }, error -> {
                            System.out.println(" ");
                            //TODO: error response if needed
                        });

                        AppController.getInstance().addToRequestQueue(getFriends);


                    }
                    else{
                        //TODO: user dne
                    }

                }, error -> {
            System.out.println(" ");
            //TODO: error response if needed
        });

        AppController.getInstance().addToRequestQueue(getUser);
    }


}