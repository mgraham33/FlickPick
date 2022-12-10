package com.flickpick.flickpick.ui.profile;

import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.FragmentBlockedListBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Code for the block list fragment
 * @author Liam Janda
 */

public class BlockedListFragment extends Fragment {



    private FragmentBlockedListBinding binding;
    LinearLayout blockedLinearLayout;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBlockedListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        blockedLinearLayout = binding.blockedLinearLayout;

        Bundle info = getArguments();
        int userId = info.getInt("userId");

        JsonArrayRequest getBlockedUsers = new                                                                 //create request to get all relationships of user
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

                                if (user1 == userId && friendType.equals("blocked")) {                           //if friend type = blocked and user 1 = userId (user1 blocks user2)
                                    addBlockedUserWidget(user2, friendshipId);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                }, error -> {
                     System.out.println(" ");
                     Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(getBlockedUsers);

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
     * adds a widget of a blocked user for the fragment
     * @param blockedId id of blocked user
     * @param friendshipId blocked friendship of the two users
     */

    private void addBlockedUserWidget(int blockedId, int friendshipId){
        View widget = getLayoutInflater().inflate(R.layout.widget_blocked_list, null);

        JsonObjectRequest getBlockedUserInfo = new JsonObjectRequest(Request.Method.GET,         //get info on curr blocked user
                (USERS_BASE_URL + blockedId), null,
                response -> {
                    try {
                        String blocked = response.getString("username");        //get info from JSON object
                        if (LoginRepository.getInstance().getUser().getUserType().getPrivilege() > LoggedInUser.UserType.User.getPrivilege()) {
                            blocked += "#" + blockedId;
                        }
                        String blockedUsername = blocked; //necessary for lambdas
                        
                        Button blockedUserBtn = widget.findViewById(R.id.blockedUserBtn);     //set blocked username
                        blockedUserBtn.setText(blockedUsername);


                            ImageButton pfpBlkBtn = widget.findViewById(R.id.pfpBlkBtn);                //set pfp
                            setUserPfp(blockedId, pfpBlkBtn);
                        pfpBlkBtn.setOnClickListener(view ->    {                           //clicked blocked user's profile button
                            try {
                                Bundle info = new Bundle();
                                info.putInt("userId", response.getInt("id"));
                                ((NavigationActivity)requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });

                        blockedUserBtn.setOnClickListener(view -> {                           //clicked blocked user's profile button
                            try {
                                Bundle info = new Bundle();
                                info.putInt("userId", response.getInt("id"));
                                ((NavigationActivity)requireActivity()).navTo(R.id.otherUserProfileFragment, "UserProfile", info);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        });


                            Button unblockBtn = widget.findViewById(R.id.unblockBtn);                       //unblock button

                            unblockBtn.setOnClickListener(view -> {                                         //when unblock user button is pressed

                                AlertDialog.Builder unblockDialog = new AlertDialog.Builder(getContext());
                                unblockDialog.setTitle("Message");
                                unblockDialog.setMessage("Are you sure you want to unblock " + blockedUsername + "?");


                                unblockDialog.setPositiveButton("Yes", (dialog, which) -> {

                                    JsonObjectRequest deleteFriendshipRequest = new JsonObjectRequest(Request.Method.DELETE,         //delete blocked friendship
                                            FRIENDS_RELATIONS_BASE_URL + friendshipId, null,
                                            response1 -> {
                                                if (response1 != null) {

                                                    unblockDialog.setMessage(blockedUsername + " has been successfully unblocked.");
                                                }
                                                else{
                                                    unblockDialog.setMessage(blockedUsername + "was already unblocked.");
                                                }
                                                blockedLinearLayout.removeView(widget);
                                            }, error -> {
                                                System.out.println(" ");
                                                Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
                                    });

                                    AppController.getInstance().addToRequestQueue(deleteFriendshipRequest);

                                    blockedLinearLayout.removeView(widget);                                   //remove view

                                    Toast.makeText(getContext(), "User Unblocked", Toast.LENGTH_SHORT).show();

                                });
                                unblockDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());                     //unblock user was canceled
                                unblockDialog.show();
                            });

                        } catch (JSONException | NullPointerException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
                        }
                    }, error -> {
                    System.out.println(" ");
                    Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        AppController.getInstance().addToRequestQueue(getBlockedUserInfo);              //send req

        blockedLinearLayout.addView(widget);

    }

    /**
     * sets image button for pfp of user
     * @param userId id of blocked user
     * @param imgBtn img button of blocked user
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