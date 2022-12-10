package com.flickpick.flickpick.ui.profile;

import static android.app.Activity.RESULT_OK;

import static com.flickpick.flickpick.util.AppController.calculateInSampleSize;
import static com.flickpick.flickpick.util.AppController.setStars;
import static com.flickpick.flickpick.util.Constants.USERS_BASE_URL;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.databinding.FragmentUserProfileBinding;
import com.flickpick.flickpick.databinding.WidgetSearchResultMainBinding;
import com.flickpick.flickpick.ui.movie.MovieFragment;
import com.flickpick.flickpick.ui.search.SearchFragment;
import com.flickpick.flickpick.ui.search.SearchSuggestions;
import com.flickpick.flickpick.ui.start.StartActivity;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.NavigationActivity;
import com.flickpick.flickpick.util.Settings;
import com.flickpick.flickpick.util.StringChecker;
import com.flickpick.flickpick.util.TextChangeDialogFragment;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manages the user profile screen.
 */
public class UserProfileFragment extends Fragment {
	private FragmentUserProfileBinding binding;
	
	private final ActivityResultLauncher<String> mGetContent =
		registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
			//binding.moveDetailsLoading.setVisibility(View.VISIBLE);
			//launch in a separate asynchronous job
			ExecutorService getPic = Executors.newSingleThreadExecutor();
			getPic.execute(() -> {
				if (result == null) return;
				try {
					InputStream s = AppController.contentResolver().openInputStream(result);
					
					final BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeStream(s, null, options);
					s.close();
					// Calculate inSampleSize based on a preset ratio
					options.inSampleSize = calculateInSampleSize(options, 200, 200);
					
					// Decode bitmap with inSampleSize set
					options.inJustDecodeBounds = false;
					s = AppController.contentResolver().openInputStream(result);
					Bitmap compressedImage = BitmapFactory.decodeStream(s, null, options);
					
					s.close();
					//Make it square
					Bitmap finalM = AppController.getBitmapAtSize(compressedImage, 200, 200);
					requireActivity().runOnUiThread(() -> {
						binding.userProfilePic.setImageBitmap(finalM);
						//binding.moveDetailsLoading.setVisibility(View.GONE);
					});

					//TODO set pfp request.
					//It can be assumed the user is logged in
					//TODO set pfp request
					JsonObjectRequest setPfp =
						new JsonObjectRequest(Request.Method.PUT, Constants.USERS_URL +
							Objects.requireNonNull(LoginRepository.getInstance()).getUser().getUserId() +
							"updatePfp/" + AppController.bitmapToBase64(finalM), null, response -> {
							try {
								LoginRepository.getInstance().getUser().updateUserFromJson(response);
							} catch (JSONException e) {
								showSetAttributeFail(R.string.pfp_error, e.getMessage());
								e.printStackTrace();
							}
						}, error -> showSetAttributeFail(R.string.pfp_error, error.getMessage()));
					AppController.getInstance().addToRequestQueue(setPfp);
				} catch (IOException e) {
					e.printStackTrace();
					requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), getString(R.string.image_select_error, e.getMessage()), Toast.LENGTH_SHORT).show());
				}
			});
		});
	
	/**
	 * Creates the view and manages UI.
	 * @param inflater used to inflate the binding
	 * @param container the parent object for the binding
	 * @param savedInstanceState previous instance, if saved
	 * @return the created view
	 */
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {
		
		binding = FragmentUserProfileBinding.inflate(getLayoutInflater());
		View root = binding.getRoot();
		
		FragmentActivity a = requireActivity();
		FragmentManager m  = a.getSupportFragmentManager();
		
		TextView username = binding.usernameText;
		TextView email = binding.emailText;
		
		ShapeableImageView pfp = binding.userProfilePic;
		ImageView pfpCam = binding.pfpCamIcon;
		
		Button changeUsername = binding.changeUsernameButton;
		Button changeEmail = binding.changeEmailButton;
		Button changePassword = binding.changePasswordButton;

		Button reset = binding.resetButton;
		Button delete = binding.deleteButton;
		
		Button friends = binding.viewFriendsListButton;
		Button blockList = binding.viewBlockListButton;
		
		CheckBox netflixC = binding.netflixCheckbox;
		CheckBox hboC = binding.hboCheckbox;
		CheckBox huluC = binding.huluCheckbox;
		CheckBox amazonC = binding.amazonPrimeCheckbox;
		CheckBox disneyC = binding.disneyCheckbox;
		
		LoginRepository r = LoginRepository.getInstance();
		if (r != null && r.isLoggedIn()) {
			LoggedInUser user = r.getUser();
			username.setText(user.getNameAndId());
			email.setText(user.getEmail());
			pfp.setImageBitmap(user.getProfilePicture());
			user.addOnUserChangedListener(to -> {
				username.setText(to.getNameAndId());
				email.setText(to.getEmail());
			});
			//If user type is above user or above
			if (user.getUserType().getPrivilege() >= LoggedInUser.UserType.User.getPrivilege()) {
				binding.userSettings.setVisibility(View.VISIBLE);
				binding.dangerZone.setVisibility(View.VISIBLE);
				pfpCam.setVisibility(View.VISIBLE);
				View.OnClickListener changePfp = view -> mGetContent.launch("image/*");

				pfp.setOnClickListener(changePfp);
				pfpCam.setOnClickListener(changePfp);

				if (user.getFavMov() >= 0) {
					binding.favoriteMovie.getRoot().setVisibility(View.VISIBLE);
					binding.removeFavMovieBtn.setText(R.string.unfavorite_movie);
					binding.removeFavMovieBtn.setEnabled(true);
					binding.removeFavMovieBtn.setOnClickListener(v -> {
						//removes fav movie
						JsonObjectRequest updateFavMovie = new JsonObjectRequest(Request.Method.PUT,
							USERS_BASE_URL + user.getUserId() + "/updateMovie/-1", null,
							response -> {
								String title = AppController.getMovieTitleId(binding.favoriteMovie.searchTitle.getText().toString(), user.getFavMov());
								Toast.makeText(getContext(), getString(R.string.unfavorite_success, title), Toast.LENGTH_SHORT).show();
								binding.favoriteMovie.getRoot().setVisibility(View.GONE);
								binding.removeFavMovieBtn.setText(R.string.no_favorite);
								binding.removeFavMovieBtn.setEnabled(false);
							}, error -> {
								Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
								error.printStackTrace();
						}
						);
						AppController.getInstance().addToRequestQueue(updateFavMovie);
					});
					
					View.OnClickListener c = v -> {
						Bundle b2 = new Bundle();
						b2.putInt(MovieFragment.ID, user.getFavMov());
						String title = AppController.getMovieTitleId(binding.favoriteMovie.searchTitle.getText().toString(), user.getFavMov());
						((NavigationActivity)requireActivity()).navTo(R.id.movieFragment,
							title, b2);
					};
					
					binding.favoriteMovie.getRoot().setOnClickListener(c);

					binding.favoriteMovie.description.setOnLongClickListener(v -> {
						String title = binding.favoriteMovie.searchTitle.getText().toString();
						new AlertDialog.Builder(getContext())
							.setTitle(title)
							.setMessage(binding.favoriteMovie.description.getText().toString()).show();
						return false;
					});
					
					binding.favoriteMovie.description.setOnClickListener(c);

					JsonObjectRequest favMov = new JsonObjectRequest(Request.Method.GET, Constants.MOVIES_BASE_URL + user.getFavMov(), null, response -> {
						WidgetSearchResultMainBinding f = binding.favoriteMovie;
						try {
							f.searchTitle.setText(AppController.getMovieTitleId(response.getString("title"), user.getFavMov()));
							f.description.setText(response.getString("description"));
							setStars(f.star1, f.star2, f.star3, f.star4, f.star5, (int)Math.round(response.getDouble("rating") * 2));
						} catch (JSONException e) {
							Toast.makeText(getContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}, error -> {
						Toast.makeText(getContext(), R.string.no_internet, Toast.LENGTH_SHORT).show();
						error.printStackTrace();
					});
					SearchFragment.imageRequest(binding.favoriteMovie.movieImage, user.getFavMov(),
						() -> binding.favoriteMovie.mainResultProgress.setVisibility(View.GONE), getContext());
					JsonObjectRequest genres = new JsonObjectRequest(Request.Method.GET, Constants.GENRES_BASE_URL + user.getFavMov(), null, response -> {
						try {
							String[] p;
							String[] gs = new String[3];
							gs[0] = response.getString("genre1");
							gs[1] = response.getString("genre2");
							gs[2] = response.getString("genre3");
							if (gs[0].equals("")) p = new String[]{};
							else if (gs[1].equals("")) p = new String[]{gs[0]};
							else if (gs[2].equals("")) p = new String[]{gs[0], gs[1]};
							else p = gs;
							SearchFragment.setGenres(binding.favoriteMovie.searchGenres, p, getResources());
						} catch (JSONException e) {
							Toast.makeText(getContext(), R.string.server_error_json_short, Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}, error -> {
						Toast.makeText(getContext(), R.string.genre_error_title, Toast.LENGTH_SHORT).show();
						error.printStackTrace();
					});

					AppController.getInstance().addToRequestQueue(favMov);
					AppController.getInstance().addToRequestQueue(genres);
				}
			}
		} else showNotLoggedIn();
		
		//Retrieve settings
		int searches = Settings.NUM_RECENT_SEARCHES.get();
		binding.searchShowAmnt.setText(String.format("%s", searches));
		binding.searchShowAmntSlider.setProgress(searches);
		netflixC.setChecked(Settings.NETFLIX.get());
		hboC.setChecked(Settings.HBO.get());
		huluC.setChecked(Settings.HULU.get());
		amazonC.setChecked(Settings.PRIME.get());
		disneyC.setChecked(Settings.DISNEY.get());

		//TODO: add checkboxes dynamically
		linkCheckBox(binding.netflixButton, netflixC, Settings.NETFLIX);
		linkCheckBox(binding.hboButton, hboC, Settings.HBO);
		linkCheckBox(binding.huluButton, huluC, Settings.HULU);
		linkCheckBox(binding.amazonPrimeButton, amazonC, Settings.PRIME);
		linkCheckBox(binding.disneyButton, disneyC, Settings.DISNEY);

		changeUsername.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				LoggedInUser user = r.getUser();
				TextChangeDialogFragment tc = new TextChangeDialogFragment();
				tc.setTitle(R.string.change_username)
				  .setHint(R.string.prompt_new_username)
				  .setPrefillText(user.getDisplayName())
				  .setOkListener((dialogInterface, i) -> {
					  JsonObjectRequest setUsername =
						  new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							  "users/" + user.getUserId() + "/updateUsername/" +
							  tc.getTextBoxText(), null, response -> {
							  try {
								  user.updateUserFromJson(response);
							  } catch (JSONException e) {
								  showSetAttributeFail(R.string.username_error, e.getMessage());
								  e.printStackTrace();
							  }
						  }, e -> showSetAttributeFail(R.string.username_error, e.getMessage()));
					  AppController.getInstance().addToRequestQueue(setUsername);
				  })
					.setTextChangeListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
						@Override
						public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
						@Override
						public void afterTextChanged(Editable editable) {
							StringChecker.CheckResults results =
								StringChecker.usernameCheck(editable.toString());
							boolean allow = results.passes()
								&& !editable.toString().equals(user.getDisplayName());
							tc.setOkEnabled(allow);
							if (!results.passes()) {
								if (results.getArgs().length == 0)
									tc.setTextboxError(results.getReason());
								else {
									String args = results.getArgsAsString();
									tc.setTextboxError(getResources().getQuantityString(
										results.getReason(), args.length(), args));
								}
							}
							else if (!allow) tc.setTextboxError(R.string.invalid_username_same);
						}
					})
                  .initiallyDisableOk()
				  .show(m, "usernameChange");
			} else showNotLoggedIn();
		});
		
		changeEmail.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				LoggedInUser user = r.getUser();
				TextChangeDialogFragment tc = new TextChangeDialogFragment();
				tc.setTitle(R.string.change_email)
				  .setHint(R.string.prompt_new_email)
				  .setPrefillText(user.getEmail())
				  .setOkListener((dialogInterface, i) -> {
					  JsonObjectRequest setEmail =
						  new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							  "users/" + user.getUserId() + "/updateEmail/" +
							  tc.getTextBoxText(), null, response -> {
							  try {
								  user.updateUserFromJson(response);
							  } catch (JSONException e) {
								  showSetAttributeFail(R.string.email_error, e.getMessage());
								  e.printStackTrace();
							  }
						  }, e -> showSetAttributeFail(R.string.email_error, e.getMessage()));
					  AppController.getInstance().addToRequestQueue(setEmail);
				  }).setTextChangeListener(new TextWatcher() {
					  @Override
					  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
					  @Override
					  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
					  @Override
					  public void afterTextChanged(Editable editable) {
						  StringChecker.CheckResults results =
							  StringChecker.emailCheck(editable.toString());
						  boolean allow = results.passes()
							  && !editable.toString().equals(user.getEmail());
						  tc.setOkEnabled(allow);
						  if (!results.passes()) tc.setTextboxError(results.getReason());
						  else if (!allow) tc.setTextboxError(R.string.invalid_email_same);
					  }
				  })
				  .initiallyDisableOk()
				  .show(m, "emailChange");
			} else showNotLoggedIn();
		});
		
		changePassword.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				LoggedInUser user = r.getUser();
				TextChangeDialogFragment tc = new TextChangeDialogFragment();
				tc.setTitle(R.string.change_password)
				  .setHint(R.string.prompt_new_password)
				  .setAsPasswordField()
				  .setOkListener((dialogInterface, i) -> {
					  JsonObjectRequest setPassword =
						  new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							  "users/" + user.getUserId() + "/updatePassword/" +
							  AppController.HTTPEncode(tc.getTextBoxText()), null, response -> {
							  try {
								  user.updateUserFromJson(response);
							  } catch (JSONException e) {
								  showSetAttributeFail(R.string.password_error, e.getMessage());
								  e.printStackTrace();
							  }
						  }, e -> showSetAttributeFail(R.string.password_error, e.getMessage()));
					  AppController.getInstance().addToRequestQueue(setPassword);
				  }).setTextChangeListener(new TextWatcher() {
					  @Override
					  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
					  @Override
					  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
					  @Override
					  public void afterTextChanged(Editable editable) {
						  StringChecker.CheckResults results =
							  StringChecker.passwordCheck(editable.toString());
						  boolean allow = tc.areTextBoxesSame();
						  tc.setOkEnabled(results.passes() && allow);
						  if (!results.passes() && tc.isTextbox1(editable))
							  tc.setTextboxError(results.getReason());
						  if (!allow) tc.setTextbox2Error(R.string.invalid_password_match);
						  else tc.setTextbox2Error(null);
					  }
				  })
				  .initiallyDisableOk()
				  .show(m, "emailChange");
			} else showNotLoggedIn();
		});

		friends.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				Bundle userId = new Bundle();
				LoggedInUser user = r.getUser();
				userId.putInt("userId", user.getUserId());
				((NavigationActivity)a).navTo(R.id.friendsListFragment, userId);
			}
			else {
				Toast.makeText(a, "Can not view Friends list while not signed in!", Toast.LENGTH_SHORT).show();
			}

		});

		blockList.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				Bundle userId = new Bundle();
				LoggedInUser user = r.getUser();
				userId.putInt("userId", user.getUserId());
				((NavigationActivity)a).navTo(R.id.blockedListFragment, userId);
				((NavigationActivity)requireActivity()).navTo(R.id.blockedListFragment, "Blocked List", userId);

			}
			else {
				Toast.makeText(a, "Can not view Blocked list while not signed in!", Toast.LENGTH_SHORT).show();
			}
		});
		
		binding.searchShowAmntSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				if (fromUser)
					binding.searchShowAmnt.setText(String.format("%s",
						binding.searchShowAmntSlider.getProgress()));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				Settings.NUM_RECENT_SEARCHES.set(seekBar.getProgress());
			}
		});

		binding.searchShowAmnt.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}

			@Override
			public void afterTextChanged(Editable s) {
				String st = s.toString();
				int n;
				if (st.trim().equals("")) n = 0;
				else n = Integer.parseInt(st);
				n = Math.max(0, Math.min(100, n)); //limit 0-100
				binding.searchShowAmntSlider.setProgress(n);
				Settings.NUM_RECENT_SEARCHES.set(n);
			}
		});

		binding.clearSearchBtn.setOnClickListener(v ->
			new AlertDialog.Builder(getContext())
				.setTitle(R.string.clear_search_history_title)
				.setMessage(R.string.clear_search_history)
				.setPositiveButton(android.R.string.ok, (dialog, which) ->
					SearchSuggestions.deleteAllSuggestions())
				.setNegativeButton(android.R.string.cancel, null).show());

		reset.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				LoggedInUser user = r.getUser();

				AlertDialog.Builder b = new AlertDialog.Builder(a);
				b.setTitle(R.string.reset_account)
				 .setMessage(R.string.reset_dialog)
				 .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
				       @Override
				       public void onClick(DialogInterface dialogInterface, int i) {
					       JsonObjectRequest reset1 = new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
						       "users/" + user.getUserId(), null, response -> {
						       try {
							       user.updateUserFromJson(response);
						       } catch (JSONException e) {
							       showSetAttributeFail(R.string.reset_error, e.getMessage());
							       e.printStackTrace();
						       }
					       }, e -> showSetAttributeFail(R.string.reset_error, e.getMessage()))
					       {
						       @Override
						       public byte[] getBody() {
							       return (String.format(
									   "{\"username\":\"%1$s\", \"email\":\"%2$s\", " +
										   "\"userType\":\"%3$s\", \"password\":\"%4$s\", " +
										   "\"movieid\":\"%5$s\", \"pfp\":\"%6$s\"}",
									   user.getDisplayName(), user.getEmail(),
									   user.getUserType(), user.getPassword(), -1, ""))
								       .getBytes(StandardCharsets.UTF_8);
						       }
					       };
					       AppController.getInstance().addToRequestQueue(reset1);
				       }
			       })
				 .setNegativeButton(android.R.string.cancel, null).show();
			}
		});
		
		delete.setOnClickListener(view -> {
			if (r != null && r.isLoggedIn(true)) {
				LoggedInUser user = r.getUser();

				AlertDialog.Builder b = new AlertDialog.Builder(a);
				b.setTitle(R.string.delete_account)
				 .setMessage(R.string.delete_dialog)
				 .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
					 @Override
					 public void onClick(DialogInterface dialogInterface, int i) {
						 JsonObjectRequest reset12 = new JsonObjectRequest(Request.Method.DELETE, Constants.BASE_URL +
							 "users/" + user.getUserId(), null, response -> {
							 try {
								if (response.getString("message").contains("does not exist"))
									Toast.makeText(a.getApplicationContext(), R.string.delete_already, Toast.LENGTH_SHORT).show();
								 Intent in = new Intent(a, StartActivity.class);
								 startActivity(in);
								 a.setResult(RESULT_OK);
								 a.finish();
							 } catch (JSONException e) {
								 showSetAttributeFail(R.string.delete_error, e.getMessage());
								 e.printStackTrace();
							 }
						 }, e -> showSetAttributeFail(R.string.delete_error, e.getMessage()));
						 AppController.getInstance().addToRequestQueue(reset12);
					 }
				 })
				 .setNegativeButton(android.R.string.cancel, null).show();
			}
		});
		
		pfp.requestFocus();
		
		return root;
	}
	
	/**
	 * Links a button to a checkbox, so that clicking the button toggles the checkbox.
	 * Will also the given setting respectively.
	 * @param b the button to link
	 * @param c the checkbox to link
	 * @param s the setting to link
	 */
	public void linkCheckBox(Button b, CheckBox c, Settings.Setting<Boolean> s) {
		b.setOnClickListener(view -> {
			boolean set = !c.isChecked();
			c.setChecked(set);
			s.set(set);
		});

		c.setOnClickListener(view -> s.set(c.isChecked()));
	}

	/**
	 * Resets the binding.
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		binding = null;
	}
	
	/**
	 * Called when the fragment is left. (Navigating back, forward, or out of the app).
	 * Saves the settings as they are.
	 */
	@Override
	public void onPause() {
		super.onPause();
		AppController.getInstance().saveSettings();
	}

	/**
	 * Displays to the user that they must be signed in to edit their profile.
	 * Serves as a safe guard; should not be called in production, technically.
	 */
	private void showNotLoggedIn() {
		Toast.makeText(getActivity(), R.string.not_signed_in_error, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Displays an error to the user if editing their profile failed.
	 * @param res the id of the string to display as a message to the user
	 * @param errorMsg the error message thrown in the code, displayed to the user
	 * through formatting of {@code res}
	 */
	private void showSetAttributeFail(@StringRes int res, String errorMsg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.generic_error)
		       .setMessage(getString(res, errorMsg))
		       .create().show();
	}
}