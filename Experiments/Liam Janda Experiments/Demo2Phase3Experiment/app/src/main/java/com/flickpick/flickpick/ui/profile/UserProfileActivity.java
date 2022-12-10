package com.flickpick.flickpick.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.ActivityDebugBinding;
import com.flickpick.flickpick.databinding.ActivityUserProfileBinding;

public class UserProfileActivity extends AppCompatActivity {
    ActivityUserProfileBinding binding;
    String urlEmail = "https://be1b044d-e3c9-4058-9a93-9d7439f0dd3f.mock.pstmn.io/user/email?id=27";
    String urlUsername = "https://be1b044d-e3c9-4058-9a93-9d7439f0dd3f.mock.pstmn.io/user/username?id=27";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        Button netflixB = binding.netflixButton;
        Button hboB = binding.hboButton;
        Button huluB = binding.huluButton;
        Button amazonB = binding.amazonPrimeButton;
        Button disneyB = binding.disneyButton;
    
        CheckBox netflixC = binding.netflixCheckbox;
        CheckBox hboC = binding.hboCheckbox;
        CheckBox huluC = binding.huluCheckbox;
        CheckBox amazonC = binding.amazonPrimeCheckbox;
        CheckBox disneyC = binding.disneyCheckbox;
        
        netflixB.setOnClickListener(view -> netflixC.setChecked(!netflixC.isChecked()));
        hboB.setOnClickListener(view -> hboC.setChecked(!hboC.isChecked()));
        huluB.setOnClickListener(view -> huluC.setChecked(!huluC.isChecked()));
        amazonB.setOnClickListener(view -> amazonC.setChecked(!amazonC.isChecked()));
        disneyB.setOnClickListener(view -> disneyC.setChecked(!disneyC.isChecked()));

        TextView email = (TextView)findViewById(R.id.emailText);
        TextView username = (TextView)findViewById(R.id.usernameText);
        RequestQueue rq = Volley.newRequestQueue(UserProfileActivity.this);
        StringRequest emailReq = new StringRequest(Request.Method.GET, urlEmail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        email.setText(response);
                        rq.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){


            }

        });

        StringRequest usernameReq = new StringRequest(Request.Method.GET, urlUsername,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        username.setText(response);
                        rq.stop();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){


            }

        });

        rq.add(emailReq);
        rq.add(usernameReq);
    }


}