package com.flickpick.flickpick.ui.admin;

import static com.flickpick.flickpick.util.AppController.calculateInSampleSize;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.data.model.OnUserChangedListener;
import com.flickpick.flickpick.databinding.FragmentEditUserBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.StringChecker;
import com.flickpick.flickpick.util.TextChangeDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fragment to edit a user.
 *
 * @author Dustin Cornelison
 */
public class EditUserFragment extends Fragment {
	public static final String USER = "user";
	private FragmentEditUserBinding binding;
	private int userId = -1;
	private LoggedInUser user;
	
	private final ActivityResultLauncher<String> mGetContent =
		registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
			//binding.moveDetailsLoading.setVisibility(View.VISIBLE);
			//launch in a separate asynchronous job
			ExecutorService getPic = Executors.newSingleThreadExecutor();
			getPic.execute(() -> {
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
						binding.adminProfilePic.setImageBitmap(finalM);
						//binding.moveDetailsLoading.setVisibility(View.GONE);
					});
					//It can be assumed the user is logged in
					//TODO set pfp request
					JsonObjectRequest setPfp =
						new JsonObjectRequest(Request.Method.PUT, Constants.USERS_URL + userId +
							"updatePfp", null, new Response.Listener<JSONObject>() {
							@Override
							public void onResponse(JSONObject response) {
							
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
							
							}
						});
					//AppController.getInstance().addToRequestQueue(setPfp);
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
		
		binding = FragmentEditUserBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		
		Bundle a = getArguments();
		if (a != null) {
			userId = a.getInt(USER);
			getUserInfo();
		} else selectUser();
		
		binding.userPrivilege.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (user != null && user.getUserType().getPrivilege() != position) {
					JsonObjectRequest setUsername =
						new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							"users/" + user.getUserId() + "/updateUsertype/" +
							LoggedInUser.UserType.fromInt(position), null, response -> tryUpdateUser(response),
						                      e -> exitMessage(getString(R.string.username_error, e.getMessage())));
					AppController.getInstance().addToRequestQueue(setUsername);
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			
			}
		});
		
		binding.adminChangeUsernameButton.setOnClickListener(view -> {
			if (user != null) {
				TextChangeDialogFragment tc = new TextChangeDialogFragment();
				tc.setTitle(R.string.change_username)
				  .setHint(R.string.prompt_new_username)
				  .setPrefillText(user.getDisplayName())
				  .setOkListener((dialogInterface, i) -> {
					  JsonObjectRequest setUsername =
						  new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							  "users/" + user.getUserId() + "/updateUsername/" +
							  tc.getTextBoxText(), null, this::tryUpdateUser,
	                          e -> exitMessage(getString(R.string.username_error, e.getMessage())));
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
				  .show(requireActivity().getSupportFragmentManager(), "usernameChange");
			} else exitMessage(getString(R.string.user_not_specified));
		});
		
		binding.adminChangeEmailButton.setOnClickListener(view -> {
			if (user != null) {
				TextChangeDialogFragment tc = new TextChangeDialogFragment();
				tc.setTitle(R.string.change_email)
				  .setHint(R.string.prompt_new_email)
				  .setPrefillText(user.getEmail())
				  .setOkListener((dialogInterface, i) -> {
					  JsonObjectRequest setEmail =
						  new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							  "users/" + user.getUserId() + "/updateEmail/" +
							  tc.getTextBoxText(), null, this::tryUpdateUser,
	                          e -> exitMessage(getString(R.string.email_error, e.getMessage())));
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
				  .show(requireActivity().getSupportFragmentManager(), "emailChange");
			} else exitMessage(getString(R.string.user_not_specified));
		});
		
		binding.adminChangePasswordButton.setOnClickListener(view -> {
			if (user != null) {
				TextChangeDialogFragment tc = new TextChangeDialogFragment();
				tc.setTitle(R.string.change_password)
				  .setHint(R.string.prompt_new_password)
				  .setAsPasswordField()
				  .setOkListener((dialogInterface, i) -> {
					  JsonObjectRequest setPassword =
						  new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
							  "users/" + user.getUserId() + "/updatePassword/" +
							  AppController.HTTPEncode(tc.getTextBoxText()), null, this::tryUpdateUser,
	                          e -> exitMessage(getString(R.string.password_error, e.getMessage())));
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
				  .show(requireActivity().getSupportFragmentManager(), "emailChange");
			} else exitMessage(getString(R.string.user_not_specified));
		});
		
		View.OnClickListener changePfp = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (user != null) mGetContent.launch("image/*");
				else exitMessage(getString(R.string.user_not_specified));
			}
		};
		
		binding.adminProfilePic.setOnClickListener(changePfp);
		binding.adminCamIcon.setOnClickListener(changePfp);
		
		binding.adminResetButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (user != null) {
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					b.setTitle(R.string.reset_account)
					 .setMessage(R.string.reset_dialog)
					 .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
						 @Override
						 public void onClick(DialogInterface dialogInterface, int i) {
							 JsonObjectRequest reset = new JsonObjectRequest(Request.Method.PUT, Constants.BASE_URL +
								 "users/" + user.getUserId(), null, EditUserFragment.this::tryUpdateUser,
                                 e -> exitMessage(getString(R.string.reset_error, e.getMessage())))
							 {
								 @Override
								 public byte[] getBody() {
									 return (String.format(
										 "{\"username\":\"%1$s\", \"email\":\"%2$s\", " +
											 "\"userType\":\"%3$s\", \"password\":\"%4$s\", " +
											 "\"movieid\":\"%5$s\", \"pfp\":\"%6$s\"}",
										 user.getDisplayName(), user.getEmail(),
										 //TODO pfp
										 user.getUserType(), user.getPassword(), -1, ""))
										 .getBytes(StandardCharsets.UTF_8);
								 }
							 };
							 AppController.getInstance().addToRequestQueue(reset);
						 }
					 })
					 .setNegativeButton(android.R.string.cancel, null).show();
				}
			}
		});
		
		binding.adminDeleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (user != null) {
					AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
					b.setTitle(R.string.delete_account)
					 .setMessage(R.string.delete_dialog)
					 .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
						 @Override
						 public void onClick(DialogInterface dialogInterface, int i) {
							 JsonObjectRequest reset = new JsonObjectRequest(Request.Method.DELETE, Constants.BASE_URL +
								 "users/" + user.getUserId(), null, response -> {
								 try {
									 if (response.getString("message").contains("does not exist"))
										 exitMessage(getString(R.string.delete_already));
								 } catch (JSONException e) {
									 exitMessage(getString(R.string.delete_error, e.getMessage()));
									 e.printStackTrace();
								 }
							 }, e -> exitMessage(getString(R.string.delete_error, e.getMessage())));
							 AppController.getInstance().addToRequestQueue(reset);
						 }
					 })
					 .setNegativeButton(android.R.string.cancel, null).show();
				}
			}
		});
		
		return root;
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
	 * Gives the user a prompt to select a user by id.
	 */
	private void selectUser() {
		setLoading(true); //loading spinner active
		TextChangeDialogFragment getId = new TextChangeDialogFragment();
		getId.setInputType(InputType.TYPE_CLASS_NUMBER)
		     .setTitle(R.string.user_id)
		     .setHint(R.string.user_id_hint).setOkListener((dialog, which) -> {
			     try {
				     userId = Integer.parseInt(getId.getTextBoxText());
				     getUserInfo();
			     } catch (NumberFormatException ignored) {
				     exitMessage(getString(R.string.user_input_error, getId.getTextBoxText(),
				                           getString(R.string.user_input_movie_id)));
			     }
		     }).setCancelListener((dialog, which) -> requireActivity().onBackPressed()).show(getParentFragmentManager(), "editUser");
	}
	
	/**
	 * Sets whether the information is loading or not. Disables all
	 * menu items, and displays a loading spinner if loading,
	 * re-enables the menu items and removes the spinner if not.
	 * @param loading whether the user information is loading
	 */
	private void setLoading(boolean loading) {
		if (loading) binding.adminAccountLoading.setVisibility(View.VISIBLE);
		else binding.adminAccountLoading.setVisibility(View.GONE);
		
		binding.adminCamIcon.setEnabled(!loading);
		binding.adminProfilePic.setEnabled(!loading);
		binding.adminChangeEmailButton.setEnabled(!loading);
		binding.adminChangeUsernameButton.setEnabled(!loading);
		binding.adminChangePasswordButton.setEnabled(!loading);
		binding.adminDeleteButton.setEnabled(!loading);
		binding.adminResetButton.setEnabled(!loading);
		binding.userPrivilege.setEnabled(!loading);
	}
	
	/**
	 * Exits the screen and displays the given message.
	 * @param msg the message to display to the user
	 */
	private void exitMessage(String msg) {
		message(msg);
		requireActivity().onBackPressed();
	}
	
	/**
	 * Gives the user a message.
	 * @param msg the message to give to the user
	 */
	private void message(String msg) {
		Toast.makeText(AppController.getInstance().getCurrentContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Attempts to update the user with the given server response.
	 * @param response the response from the server after a request
	 */
	private void tryUpdateUser(JSONObject response) {
		try {
			user.updateUserFromJson(response);
		} catch (JSONException e) {
			exitMessage(getString(R.string.unknown_edit_user_error, e.getMessage()));
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves the user's information from the server given the id.
	 */
	private void getUserInfo() {
		if (LoginRepository.getInstance() != null && LoginRepository.getInstance().isLoggedIn() && LoginRepository.getInstance().getUser().getUserId() == userId)
			exitMessage(getString(R.string.edit_current_user));
		else {
			JsonObjectRequest getUser =
				new JsonObjectRequest(Request.Method.GET, Constants.USERS_URL + "/" + userId, null, response -> {
					try {
						user = new LoggedInUser(userId, null);
						user.addOnUserChangedListener(to -> {
							binding.adminUsernameText.setText(user.getDisplayName());
							binding.adminEmailText.setText(user.getEmail());
							binding.adminProfilePic.setImageBitmap(user.getProfilePicture());
							binding.userPrivilege.setSelection(user.getUserType().getPrivilege());
						});
						user.updateUserFromJson(response);
						user.setProfilePic(AppController.base64ToBitmap(response.getString("pfp")));
						//loading spinner not active
						setLoading(false);
					} catch (JSONException e) {
						exitMessage(getString(R.string.server_error_json, response));
						e.printStackTrace();
					}
				}, error -> {
					exitMessage(getString(R.string.server_error_basic, String.valueOf(error.getMessage())));
					error.printStackTrace();
				});
			AppController.getInstance().addToRequestQueue(getUser);
		}
	}
}