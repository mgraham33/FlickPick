package com.flickpick.flickpick.ui.profile;

import static com.flickpick.flickpick.util.AppController.base64ToBitmap;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;
import static com.flickpick.flickpick.util.Constants.WS_CHAT_BASE_URL;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.LinearLayout;
import android.widget.TextView;

import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.databinding.FragmentMessagingScreenBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.NavigationActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Code for the message fragment
 * @author Liam Janda
 */

public class MessagingFragment extends Fragment {

    private FragmentMessagingScreenBinding binding;

    /**
     * When this is fragment is created, this method will be called
     * @param inflater used to create view from corresponding xml file
     * @param container the ViewGroup that this fragment belongs to
     * @param savedInstanceState Vital information from last instance of this fragment
     * @return view
     */

    private WebSocketClient cc;
    
    private String lastMsg = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMessagingScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Bundle info = getArguments();
        int friendId = info.getInt("id");
        int friendshipId = info.getInt("friendshipId");


        LoginRepository r = LoginRepository.getInstance();      //get user id

        ImageButton pfpBtn = binding.pfpMsgBtn;
        Button friendInfoBtn = binding.friendInfoBtn;
        ImageButton sendMsgBtn = binding.sendMsgBtn;
        LinearLayout layout = binding.msgLinearLayout;
        EditText sendMessageText = binding.sendMessageText;

        setFriendPfp(friendId, pfpBtn);

        String friendName = info.getString("friendName");
        friendInfoBtn.setText(friendName);

        pfpBtn.setOnClickListener(v -> {
            Bundle info2 = new Bundle();
            info2.putInt("userId", friendId);
            ((NavigationActivity)requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info2);
        });
        friendInfoBtn.setOnClickListener(v -> {
            Bundle info2 = new Bundle();
            info2.putInt("userId", friendId);
            ((NavigationActivity)requireActivity()).navTo(R.id.otherUserProfileFragment, "User Profile", info2);
        });


        Draft[] drafts = {
                new Draft_6455()
        };

        String url = WS_CHAT_BASE_URL + friendshipId;


        try {

            cc = new WebSocketClient(new URI(url), (Draft) drafts[0]) {
                @Override
                public void onMessage(String message) {

                    if(!(message.contains("null"))&&!(message.contains("\n"))) {
                        ((Activity)requireContext()).runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                if (message.equals(lastMsg)) lastMsg = null;
                                else {
                                    View friendMessage = getLayoutInflater().inflate(R.layout.widget_friend_message, null);
                                    TextView messageTxt = friendMessage.findViewById(R.id.friendMessage);
                                    messageTxt.setText(message);
                                    layout.addView(friendMessage);
                                }
                            }
                        });
                    }

                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    //TODO
                    System.out.println("");

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    //TODO
                    System.out.println("");

                }

                @Override
                public void onError(Exception e) {
                    //TODO
                    System.out.println("");

                }
            };
        } catch (URISyntaxException e) {

            e.printStackTrace();
        }
        cc.connect();





        sendMsgBtn.setOnClickListener(v -> {
            try {
                View userMessage = getLayoutInflater().inflate(R.layout.widget_user_message, null);
                TextView messageTxt = userMessage.findViewById(R.id.userMessage);
                String message = sendMessageText.getText().toString();
                messageTxt.setText(message);
                layout.addView(userMessage);
                cc.send(message);
                lastMsg = message;
                sendMessageText.setText("");
            } catch (Exception e) {
                //TODO
                System.out.println("");
            }
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
     * sets image button for pfp of friend
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
}