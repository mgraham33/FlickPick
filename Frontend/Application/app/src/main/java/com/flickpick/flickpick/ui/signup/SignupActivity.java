package com.flickpick.flickpick.ui.signup;

import static com.flickpick.flickpick.util.Constants.USERS_URL;
import static com.flickpick.flickpick.util.StringChecker.emailCheck;
import static com.flickpick.flickpick.util.StringChecker.passwordCheck;
import static com.flickpick.flickpick.util.StringChecker.usernameCheck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.ui.login.LoginActivity;
import com.flickpick.flickpick.util.StringChecker;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Code for the signup fragment
 * @author Liam Janda
 */

public class SignupActivity extends AppCompatActivity {

    TextView emailInput;
    TextView passwordInput;
    TextView passwordInput2;
    TextView usernameInput;

    /**
     * When this is activity is created, this method will be called
     * @param savedInstanceState Vital information from last instance of this fragment
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        passwordInput2 = findViewById(R.id.passwordInput2);
        emailInput = findViewById(R.id.emailInput);
    }

    /**
     * collect info for register attempt and tries to register. if not successful, give reason why. if successful, post user to db
     * @param v view that called this method (signup button)
     */

    public void register(View v) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        String password2 = passwordInput2.getText().toString();
        String email = emailInput.getText().toString();

        RequestQueue rq = Volley.newRequestQueue(this);     //create request queue
        checkUsers(username, email, password, password2, rq);                  //signup process
    }

    /**
     * if login button is pressed, send user to login activity
     * @param v view that called this method (register button)
     */

    public void login(View v) {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    /**
     * check requirements and if username and password are available. if so, post user. if not, give reason why
     * @param username username input
     * @param email email input
     * @param password password field 1 input
     * @param password2 password field 2 input
     * @param rq request queue object
     */

    public void checkUsers(String username, String email, String password, String password2, RequestQueue rq) {


        StringChecker.CheckResults usernameResult = usernameCheck(username);                    //CheckResults for username, email, and password
        StringChecker.CheckResults emailResult = emailCheck(email);
        StringChecker.CheckResults passwordResult = passwordCheck(password);

        Context context = getApplicationContext();


        if(!(password.equals(password2))) {                                                                         //if passwords don't match
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
        else if(!usernameResult.passes()){                                                                       //if username does not meet requirements
            Toast.makeText(context, usernameResult.getReason(), Toast.LENGTH_SHORT).show();
        }
        else if(!emailResult.passes()){                                                                      //if email does not meet requirements
            Toast.makeText(context, emailResult.getReason(), Toast.LENGTH_SHORT).show();
        }
        else if(!passwordResult.passes()){                                                                //if password does not meet requirements

            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);

            builder.setMessage(passwordResult.getReason()) .setTitle("Oops!").setPositiveButton("Okay", (dialog, id) -> {

            });
            AlertDialog alert = builder.create();
            alert.show();

        }
        else{
            JsonArrayRequest getUsers = new                                                                 //create request to get all users
                    JsonArrayRequest(Method.GET, USERS_URL, null,
                    response -> {
                        //TODO: server has code that already checks if username/email are taken, fix code to use that
                        try {
                            boolean emailExists = false;
                            boolean usernameExists = false;
                            for (int i = 0; i < response.length(); ++i) {                           // check to see if username or email are taken
                                JSONObject currUser = response.getJSONObject(i);
                                if (email.equals(currUser.getString("email"))) {            //check email
                                    emailExists = true;
                                }
                                if (username.equals(currUser.getString("username"))) {      //check username
                                    usernameExists = true;
                                }
                            }
                            if (usernameExists) {                                                                         //if username exists, do not allow to make account
                                Toast.makeText(context, "Username Already Taken.", Toast.LENGTH_SHORT).show();
                            } else if (emailExists) {                                                                       //if email exists, do not allow to make account
                                Toast.makeText(context, "Email Already Taken.", Toast.LENGTH_SHORT).show();
                            } else {                                                                                            //else, post user
                                postUser(username, email, password, rq);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    });

            rq.add(getUsers);                       //add req to queue

        }

    }

    /**
     * posts user to db
     * @param username username of user
     * @param email email of user
     * @param password password of user
     * @param rq request queue
     */

    public void postUser(String username, String email, String password, RequestQueue rq) {         //post user to db

        JSONObject postParam = new JSONObject();

        try {
            postParam.put("username", username);                                                        //parameters for json request body
            postParam.put("email", email);
            postParam.put("password", password);
            postParam.put("pfp", "/Images/PFPs/defaultpfp.jpg");
            postParam.put("favMID", 1);
            postParam.put("userType", "User");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postUser = new JsonObjectRequest(Method.POST,       //creating request to post new user
                USERS_URL, postParam,
                response -> {
                    //TODO: check if all responses are covered, server has code that already checks if username/email are taken, fix code to use that
                }, error -> {
                    Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        });

        rq.add(postUser);     //add req to queue

        Context context = getApplicationContext();                                              //send user to home page
        Toast.makeText(context, "Account Created", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(i);

    }
}