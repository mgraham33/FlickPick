package com.flickpick.flickpick.ui.admin;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.databinding.FragmentEditMovieBinding;
import com.flickpick.flickpick.util.AppController;
import com.flickpick.flickpick.util.Constants;
import com.flickpick.flickpick.util.TextChangeDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A screen to create a new movie
 *
 * @author Dustin Cornelison
 */
public class EditMovieFragment extends Fragment {
	public static final String TYPE = "type";
	public static final String MOVIE = "movie";
	public static final int CREATE = 0;
	public static final int EDIT = 1;
	public static final int DELETE = 2;
	
	FragmentEditMovieBinding binding;
	private int movieId = -1;
	private final ActivityResultLauncher<String> mGetContent =
		registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
			binding.movieDetailsLoading.setVisibility(View.VISIBLE);
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
					//TODO: find correct size
					options.inSampleSize = AppController.calculateInSampleSize(options, 240, 400);
					
					// Decode bitmap with inSampleSize set
					options.inJustDecodeBounds = false;
					s = AppController.contentResolver().openInputStream(result);
					Bitmap compressedImage = BitmapFactory.decodeStream(s, null, options);
					
					if (compressedImage.getWidth() > compressedImage.getHeight())
						compressedImage = AppController.rotateBitmap(compressedImage, 90);
					s.close();
					
					Bitmap finalM = compressedImage;
					requireActivity().runOnUiThread(() -> {
						binding.setMoviePosterBtn.setImageBitmap(finalM);
						binding.movieDetailsLoading.setVisibility(View.GONE);
					});
				} catch (IOException e) {
					e.printStackTrace();
					requireActivity().runOnUiThread(() -> message(getString(R.string.image_select_error, e.getMessage())));
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
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		binding = FragmentEditMovieBinding.inflate(inflater, container, false);
		View root = binding.getRoot();
		
		binding.movieRating.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				AppController.setStars(binding.movieStar1, binding.movieStar2, binding.movieStar3, binding.movieStar4, binding.movieStar5, progress);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
		});
		
		Bundle a = requireArguments();
		int type = a.getInt(TYPE);
		
		binding.movieCancelButton.setOnClickListener(v -> requireActivity().onBackPressed());
		binding.setMoviePosterBtn.setOnClickListener(v -> mGetContent.launch("image/*"));
		
		switch (type) {
			case CREATE:
				binding.movieRating.setActivated(true);
				binding.movieOkButton.setOnClickListener(v -> {
					setLoading(true);
					JsonObjectRequest addMovie = new JsonObjectRequest(Request.Method.POST, Constants.MOVIES_BASE_URL, null, new Response.Listener<JSONObject>() {
						@Override
						public void onResponse(JSONObject response) {
							try {
								if (response.getString("message").equals("success"))
									exitMessage(getString(R.string.add_movie_success));
							} catch (JSONException e) {
								exitMessage(getString(R.string.unknown_error));
								e.printStackTrace();
							}
						}
					}, error -> {
						exitMessage(getString(R.string.add_movie_fail));
						error.printStackTrace();
					}) {
						@SuppressLint("DefaultLocale")
						@Override
						public byte[] getBody() {
							//TODO fix error 500
							 return String.format(
								"{\"title\":\"%1$s\", \"description\":\"%2$s\", \"minutes\":%3$s, \"year\":%4$s, \"rating\":%5$f, \"picture\":\"%6$s\"}",
								binding.movieNameText.getText(), binding.movieDescText.getText(),
								binding.movieMinutesText.getText(), binding.movieYearText.getText(),
								binding.movieRating.getProgress() / 2d,
								AppController.drawableToBase64(binding.setMoviePosterBtn.getDrawable())).getBytes(StandardCharsets.UTF_8);
						}
					};
					AppController.getInstance().addToRequestQueue(addMovie);
				});
				break;
			case EDIT:
				movieId = a.getInt(MOVIE);
				if (movieId == 0) selectMovie(true);
				else loadMovie(true);
				binding.movieRating.setActivated(false);
				binding.movieOkButton.setOnClickListener(v -> {
					setLoading(true);
					JsonObjectRequest editMovie = new JsonObjectRequest(Request.Method.PUT, Constants.MOVIES_BASE_URL + movieId, null, response -> {
						try {
							String msg = response.getString("message");
							if (msg.equals("success"))
								exitMessage(getString(R.string.edit_movie_success));
						} catch (JSONException e) {
							exitMessage(getString(R.string.unknown_error));
							e.printStackTrace();
						}
					}, error -> {
						exitMessage(getString(R.string.edit_movie_fail));
						error.printStackTrace();
					}) {
						@SuppressLint("DefaultLocale")
						@Override
						public byte[] getBody() {
							//TODO trouble shoot error 500
							return String.format(
								"{\"title\":\"%1$s\", \"description\":\"%2$s\", \"minutes\":%3$s, \"year\":%4$s, \"rating\":%5$f, \"picture\":%6$s}",
								binding.movieNameText.getText(), binding.movieDescText.getText(),
								binding.movieMinutesText.getText(), binding.movieYearText.getText(),
								binding.movieRating.getProgress() / 2f,
								AppController.drawableToBase64(binding.setMoviePosterBtn.getDrawable())).getBytes(StandardCharsets.UTF_8);
						}
					};
					AppController.getInstance().addToRequestQueue(editMovie);
				});
				break;
			case DELETE:
				if (a.getInt(MOVIE) == 0) selectMovie(false);
				binding.movieRating.setActivated(false);
				binding.movieOkButton.setOnClickListener(v -> {
					setLoading(true);
					JsonObjectRequest deleteMovie = new JsonObjectRequest(Request.Method.DELETE, Constants.MOVIES_BASE_URL + movieId, null, response -> {
						try {
							String msg = response.getString("message");
							if (msg.equals("success"))
								exitMessage(getString(R.string.delete_movie_success));
							else {
								exitMessage(getString(R.string.unknown_error) + " " + msg);
								
							}
						} catch (JSONException e) {
							exitMessage(getString(R.string.unknown_error));
							e.printStackTrace();
						}
					}, error -> {
						exitMessage(getString(R.string.delete_movie_fail));
						error.printStackTrace();
					});
					AppController.getInstance().addToRequestQueue(deleteMovie);
				});
				break;
				
		}
		
		// Inflate the layout for this fragment
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
	 * Gives the user a prompt to select a movie by id.
	 * @param reactivate whether to reactivate the menu items that
	 * contain the movie information. In other words, whether to let
	 * the user edit the movie information
	 */
	private void selectMovie(boolean reactivate) {
		setLoading(true); //loading spinner active
		TextChangeDialogFragment getId = new TextChangeDialogFragment();
		getId.setInputType(InputType.TYPE_CLASS_NUMBER)
		     .setTitle(R.string.movie_id)
		     .setHint(R.string.movie_id_hint).setOkListener((dialog, which) -> {
			     try {
					 movieId = Integer.parseInt(getId.getTextBoxText());
				     loadMovie(reactivate);
			     } catch (NumberFormatException ignored) {
				     exitMessage(getString(R.string.user_input_error, getId.getTextBoxText(),
				                           getString(R.string.user_input_movie_id)));
			     }
		     }).setCancelListener((dialog, which) -> requireActivity().onBackPressed()).show(getParentFragmentManager(), "editMovie");
	}
	
	/**
	 * Selects a movie by id.
	 * @param reactivate whether to reactivate the menu items that
	 * contain the movie information. In other words, whether to let
	 * the user edit the movie information after it's loaded
	 */
	private void loadMovie(boolean reactivate) {
		setLoading(true);
		JsonObjectRequest getMovie =
			new JsonObjectRequest(Request.Method.GET, Constants.MOVIES_BASE_URL + movieId, null, response -> {
				try {
					binding.movieNameText.setText(response.getString("title"));
					binding.movieDescText.setText(response.getString("description"));
					binding.movieMinutesText.setText(response.getString("minutes"));
					binding.movieYearText.setText(response.getString("year"));
					binding.movieRating.setProgress((int)Math.round(response.getDouble("rating") * 2));
					//loading spinner not active
					if (reactivate) setLoading(false);
					else {
						binding.movieDetailsLoading.setVisibility(View.GONE);
						binding.movieOkButton.setEnabled(true);
						binding.movieCancelButton.setEnabled(true);
					}
				} catch (JSONException e) {
					exitMessage(getString(R.string.server_error_json, response));
					e.printStackTrace();
				}
			}, error -> {
				exitMessage(getString(R.string.server_error_basic, String.valueOf(error.getMessage())));
				error.printStackTrace();
			});
		JsonObjectRequest imageReq = new JsonObjectRequest(Request.Method.GET, Constants.MOVIES_BASE_URL + movieId + "/picture", null,
			response -> {
				try {
					binding.setMoviePosterBtn.setImageBitmap(AppController.base64ToBitmap(response.getString("picture")));
				} catch (JSONException e) {
					message(getString(R.string.parse_movie_image_fail));
					e.printStackTrace();
				}
			}, error -> {
			message(getString(R.string.fetch_movie_image_error));
			error.printStackTrace();
		});
		AppController.getInstance().addToRequestQueue(imageReq);
		AppController.getInstance().addToRequestQueue(getMovie);
	}
	
	/**
	 * Sets whether the information is loading or not. Disables all
	 * menu items, and displays a loading spinner if loading,
	 * re-enables the menu items and removes the spinner if not.
	 * @param loading whether the movie information is loading
	 */
	private void setLoading(boolean loading) {
		if (loading) binding.movieDetailsLoading.setVisibility(View.VISIBLE);
		else binding.movieDetailsLoading.setVisibility(View.GONE);
		
		binding.movieRating.setEnabled(!loading);
		binding.movieYearText.setEnabled(!loading);
		binding.movieMinutesText.setEnabled(!loading);
		binding.movieNameText.setEnabled(!loading);
		binding.movieDescText.setEnabled(!loading);
		binding.movieOkButton.setEnabled(!loading);
		binding.movieCancelButton.setEnabled(!loading);
		binding.setMoviePosterBtn.setEnabled(!loading);
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
}