package com.flickpick.flickpick.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
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
import com.flickpick.flickpick.ui.login.LoginViewModel;
import com.flickpick.flickpick.ui.login.LoginViewModelFactory;
import com.flickpick.flickpick.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
	
	private LoginViewModel loginViewModel;
	private ActivityLoginBinding binding;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
			.get(LoginViewModel.class);
		
		final EditText usernameEditText = binding.username;
		final EditText passwordEditText = binding.password;
		final Button loginButton = binding.loginBtn;
		final Button regButton = binding.registerBtn;
		final ProgressBar loadingProgressBar = binding.loading;
		final LoginFormState[] current = new LoginFormState[1];
		
		loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
			@Override
			public void onChanged(@Nullable LoginFormState loginFormState) {
				if (loginFormState == null) {
					return;
				}
				current[0] = loginFormState;
				//int signIn = loginFormState.getUsernameError();
				//int reg = loginFormState.getEmailError();
				int pass = loginFormState.getPasswordError();
				
				//loginButton.setEnabled(signIn == -1);
				//if (signIn != -1) {
				//	//TODO: Currently, this crashes. I know why. Need to change it so that errors are set when buttons are clicked instead of while editing the text.
				//	//Crash is caused by -1 value given when there are no errors
				//	loginButton.setError(getString(signIn));
				//}
				//if (reg != -1) {
				//	regButton.setError(getString(reg));
				//}
				if (pass != -1) {
					passwordEditText.setError(getString(pass));
				}
			}
		});
		
		loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
			@Override
			public void onChanged(@Nullable LoginResult loginResult) {
				loadingProgressBar.setVisibility(View.GONE);
				
				if (loginResult.getSuccess() != null) {
					updateUiWithUser(loginResult.getSuccess());
				}
				setResult(Activity.RESULT_OK);
				
				//Complete and destroy login activity once successful
				finish();
			}
		});
		
		TextWatcher afterTextChangedListener = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// ignore
			}
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// ignore
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
					passwordEditText.getText().toString());
			}
		};
		usernameEditText.addTextChangedListener(afterTextChangedListener);
		passwordEditText.addTextChangedListener(afterTextChangedListener);
		passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					loginViewModel.login(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());
				}
				return false;
			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LoginFormState loginResult = current[0];
				if (loginResult == null) {
					usernameEditText.setError(getText(R.string.invalid_username_short));
					passwordEditText.setError(getText(R.string.invalid_password_short));
					return;
				}
				int signIn = loginResult.getUsernameError();
				int pass = loginResult.getPasswordError();
				
				//showLoginFailed(loginResult.getError());
				if (signIn != -1) usernameEditText.setError(getText(signIn));
				if (pass != -1) passwordEditText.setError(getText(pass));
				else if (pass == signIn) {//only executes if pass == -1 == signIn
					loadingProgressBar.setVisibility(View.VISIBLE);
					loginViewModel.login(usernameEditText.getText().toString(),
						passwordEditText.getText().toString());
				}
			}
		});
		
		regButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LoginFormState loginResult = current[0];
				if (loginResult == null) {
					usernameEditText.setError(getText(R.string.invalid_email));
					passwordEditText.setError(getText(R.string.invalid_password_short));
					return;
				}
				int reg = loginResult.getEmailError();
				int pass = loginResult.getPasswordError();
				
				if (reg != -1) usernameEditText.setError(getText(reg));
				if (pass != -1) passwordEditText.setError(getText(pass));
				else if (pass == reg) {//only executes if pass == -1 == signIn
				loadingProgressBar.setVisibility(View.VISIBLE);
				loginViewModel.login(usernameEditText.getText().toString(),
					passwordEditText.getText().toString());
				}
			}
		});
	}
	
	private void updateUiWithUser(LoggedInUserView model) {
		String welcome = String.format(getString(R.string.welcome), model.getDisplayName());
		// TODO : initiate successful logged in experience
		Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
	}
	
	private void showLoginFailed(@StringRes Integer errorString) {
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
	}
}