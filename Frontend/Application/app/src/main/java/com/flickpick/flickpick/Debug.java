package com.flickpick.flickpick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.ActivityDebugBinding;
import com.flickpick.flickpick.ui.login.LoginActivity;
import com.flickpick.flickpick.ui.profile.FriendsListFragment;
import com.flickpick.flickpick.ui.profile.UserProfileFragment;
import com.flickpick.flickpick.ui.signup.SignupActivity;
import com.flickpick.flickpick.ui.start.StartActivity;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.NavigationActivity;
import com.flickpick.flickpick.util.TextChangeDialogFragment;

/**
 * The debug screen for the app; not production.
 *
 * @author Dustin Cornelison
 * @author Liam Janda
 */
public class Debug extends AppCompatActivity {
	private ActivityDebugBinding binding;
	
	/**
	 * Creates the view and manages UI.
	 * @param savedInstanceState previous instance, if saved
	 * @see androidx.fragment.app.FragmentActivity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//can't affect tests
		//if (Constants.DEBUG) {
		//	AppController.getInstance().setGenres(new String[]{"Comedy", "Action", "Drama", "Horror", "Adventure", "Science Fiction", "Romance"});
		//	AppController.getInstance().setServices(new String[]{"Netflix", "Hulu", "HBO Max", "Disney+", "Amazon Prime Video"});
		//}
		
		binding = ActivityDebugBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		Button loginBtn = binding.debugBtnLogin;
		Button regBtn = binding.debugBtnRegister;
		Button dia = binding.debugBtnDialog;

		loginBtn.setOnClickListener(view -> {
			Intent i = new Intent(Debug.this, LoginActivity.class);
			startActivity(i);
		});

		regBtn.setOnClickListener(view -> {
			//Toast.makeText(Debug.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(Debug.this, SignupActivity.class);
			startActivity(i);
		});
		
		binding.debugBtnStart.setOnClickListener(view -> {
			Intent i = new Intent(Debug.this, StartActivity.class);
			startActivity(i);
		});
		
		dia.setOnClickListener(view -> {
			TextChangeDialogFragment tc = new TextChangeDialogFragment();
			tc.setTitle(R.string.change_username)
					.setHint(R.string.prompt_new_username)
			.show(getSupportFragmentManager(), "debug");
		});
		
		binding.debugBtnNav.setOnClickListener(view -> {
			Intent i = new Intent(Debug.this, NavigationActivity.class);
			startActivity(i);
		});
		
		binding.debugBtnFakeLogin.setOnClickListener(view -> {
			LoginRepository r = LoginRepository.getInstance();
			if (r != null) {
				r.dummyLogin(LoggedInUser.UserType.User);
				Toast.makeText(this, "Signed in as dummy user", Toast.LENGTH_SHORT).show();
			} else Toast.makeText(this, "Cannot make dummy user. Try opening the login activity first", Toast.LENGTH_LONG).show();
		});
	}
}