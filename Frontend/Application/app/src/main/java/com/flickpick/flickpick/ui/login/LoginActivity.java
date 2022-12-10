package com.flickpick.flickpick.ui.login;

import android.app.Activity;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.ActivityLoginBinding;
import com.flickpick.flickpick.ui.signup.SignupActivity;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.NavigationActivity;

/**
 * The activity that allows the user to sign in.
 *
 * @author Dustin Cornelison
 */
public class LoginActivity extends AppCompatActivity {
	
	private LoginViewModel loginViewModel;
	private ActivityLoginBinding binding;
	
	/**
	 * Creates the view and manages UI.
	 * @param savedInstanceState previous instance, if saved
	 * @see androidx.fragment.app.FragmentActivity#onCreate(Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
			.get(LoginViewModel.class);
		
		final EditText usernameEditText = binding.email;
		final EditText passwordEditText = binding.password;
		final TextView errorText = binding.loginErrorMsg;
		final Button loginButton = binding.loginBtn;
		final Button regButton = binding.registerBtn;
		final ProgressBar loadingProgressBar = binding.loading;
		
		loginViewModel.getLoginFormState().observe(this, loginFormState -> {
			if (loginFormState == null) {
				usernameEditText.setError(getText(R.string.invalid_username_short));
				passwordEditText.setError(getText(R.string.invalid_password_short));
				return;
			}
			//errorText.setAlpha(0f);
			
			int signIn = loginFormState.getUsernameError();
			//int reg = loginFormState.getEmailError();
			int pass = loginFormState.getPasswordError();
			String invalidChars = loginFormState.getInvalidChars();
			
			loginButton.setEnabled(signIn == -1 && pass == -1);
			if (signIn != -1)
				if (invalidChars.length() == 0) usernameEditText.setError(getString(signIn));
				else usernameEditText.setError(
					getResources().getQuantityString(R.plurals.invalid_username_character, invalidChars.length(),
					                                 invalidChars));
			//if (reg != -1) {
			//	regButton.setError(getString(reg));
			//}
			if (pass != -1) passwordEditText.setError(getString(pass));
		});
		
		loginViewModel.getLoginResult().observe(this, loginResult -> {
			loadingProgressBar.setVisibility(View.GONE);
			
			if (loginResult.getSuccess() != null) {
				updateUiWithUser(loginResult.getSuccess());
				setResult(Activity.RESULT_OK);
				//Complete and destroy login activity once successful
				finish();
			} else {
				//errorText.setAlpha(1f);
				//errorText.setText(loginResult.getError());
				showLoginFailed(String.format(getString(loginResult.getError()), loginResult.getCode()));
			}
			
		});
		
		TextWatcher textChangedListener = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
												passwordEditText.getText().toString());
			}
		};
		usernameEditText.addTextChangedListener(textChangedListener);
		passwordEditText.addTextChangedListener(textChangedListener);
		passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//When user clicks enter in the password field:
				if (actionId == EditorInfo.IME_ACTION_DONE && (loginButton.isEnabled())) {
					loadingProgressBar.setVisibility(View.VISIBLE);
					tryLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
				}
				return false;
			}
		});
		
		//
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//LoginFormState loginResult = current[0];
				//if (loginResult == null) {
				//	usernameEditText.setError(getText(R.string.invalid_username_short));
				//	passwordEditText.setError(getText(R.string.invalid_password_short));
				//	return;
				//}
				//int signIn = loginResult.getUsernameError();
				//int pass = loginResult.getPasswordError();
				
				//showLoginFailed(loginResult.getError());
				//if (signIn != -1) usernameEditText.setError(getText(signIn));
				//if (pass != -1) passwordEditText.setError(getText(pass));
				//else if (pass == signIn) {//only executes if pass == -1 == signIn
					loadingProgressBar.setVisibility(View.VISIBLE);
					tryLogin(usernameEditText.getText().toString(), passwordEditText.getText().toString());
					//loginViewModel.setLogin(usernameEditText.getText().toString(),
					//	passwordEditText.getText().toString());
				//}
			}
		});
		
		regButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(LoginActivity.this, SignupActivity.class);
				startActivity(i);
			}
		});
	}
	
	/**
	 * Welcomes the user to the app and starts the home (navigation) activity.
	 * @param user the signed in user to welcome
	 */
	private void updateUiWithUser(LoggedInUser user) {
		String welcome = String.format(getString(R.string.welcome), user.getNameAndId());
		Intent i = new Intent(LoginActivity.this, NavigationActivity.class);
		startActivity(i);
		Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * Notifies the user of a failed login, and the reason why.
	 * @param text the message to display to the user
	 */
	private void showLoginFailed(String text) {
		Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Attempts login with the given credentials.
	 * @param username the username to attempt login with
	 * @param password the password to attempt login with
	 */
	private void tryLogin(String username, String password) {
		loginViewModel.login(username, password);
	}
}
