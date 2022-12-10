package com.flickpick.experiment3.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import com.flickpick.experiment3.MainActivity;
import com.flickpick.experiment3.R;
import com.flickpick.experiment3.ui.login.LoginViewModel;
import com.flickpick.experiment3.ui.login.LoginViewModelFactory;
import com.flickpick.experiment3.databinding.ActivityLoginBinding;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {
	
	private LoginViewModel loginViewModel;
	private ActivityLoginBinding binding;
	private long lastBackPress = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityLoginBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
			.get(LoginViewModel.class);
		
		final EditText usernameEditText = binding.username;
		final EditText passwordEditText = binding.password;
		final Button loginButton = binding.login;
		final ProgressBar loadingProgressBar = binding.loading;
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) usernameEditText.setText(extras.getString("username"));
		
		loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
			@Override
			public void onChanged(@Nullable LoginFormState loginFormState) {
				if (loginFormState == null) {
					return;
				}
				loginButton.setEnabled(loginFormState.isDataValid());
				if (loginFormState.getUsernameError() != null) {
					usernameEditText.setError(getString(loginFormState.getUsernameError()));
				}
				if (loginFormState.getPasswordError() != null) {
					passwordEditText.setError(getString(loginFormState.getPasswordError()));
				}
			}
		});
		
		loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
			@Override
			public void onChanged(@Nullable LoginResult loginResult) {
				if (loginResult == null) {
					return;
				}
				loadingProgressBar.setVisibility(View.GONE);
				if (loginResult.getError() != null) {
					showLoginFailed(loginResult.getError());
				}
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
				loadingProgressBar.setVisibility(View.VISIBLE);
				loginViewModel.login(usernameEditText.getText().toString(),
					passwordEditText.getText().toString());
				if (loginViewModel.getLoginResult().getValue().getSuccess() != null) {
					Intent i = new Intent(LoginActivity.this, MainActivity.class);
					i.putExtra("username", loginViewModel.getLoginResult().getValue().getSuccess().getDisplayName());
					startActivity(i);
				}
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		long cur = System.currentTimeMillis();
		//Give 5 seconds between back presses to quit
		if (lastBackPress != -1 && lastBackPress >= cur - 5000) {
			System.exit(0);
		} else {
			lastBackPress = cur;
			Toast.makeText(getApplicationContext(), R.string.quit_confirm, Toast.LENGTH_SHORT).show();
		}
	}
	
	private void updateUiWithUser(LoggedInUserView model) {
		String welcome = getString(R.string.nav_welcome, model.getDisplayName());
		// TODO : initiate successful logged in experience
		Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
	}
	
	private void showLoginFailed(@StringRes Integer errorString) {
		Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
	}
}