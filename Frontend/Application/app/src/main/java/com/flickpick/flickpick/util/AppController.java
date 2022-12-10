package com.flickpick.flickpick.util;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.ImageView;

import androidx.core.content.res.ResourcesCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

/**
 * Controls the application, and is also a collection of methods that are helpful in many classes.
 *
 * @author Dustin Cornelison
 */
public class AppController extends Application {
	private static final String SETTINGS_LOC = "com.flickpick.flickpick.settings";
	public static final String TAG = AppController.class.getSimpleName();
	
	private RequestQueue mRequestQueue;
	private SharedPreferences settings;
	
	private static AppController mInstance;
	
	private String[] genres = null;
	private String[] services = new String[]{"Netflix", "Hulu", "HBO Max", "Disney+", "Amazon Prime Video"};
	
	/**
	 * Run when the program starts. Sets the instance variable and loads settings.
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		loadSettings("");
	}
	
	/**
	 * Used to show admins movie ids appended to titles. Use this everywhere
	 * a movie title should be displayed. Don't use this where movie titles
	 * are used as data.
	 * @param title the title of the movie
	 * @param id the id of the movie
	 * @return the title appended with the id, if logged in user is an admin.
	 * Otherwise just the title
	 */
	public static String getMovieTitleId(String title, int id) {
		LoginRepository r = LoginRepository.getInstance();
		if (r != null && r.isLoggedIn()) {
			LoggedInUser u = r.getUser();
			if (u.getUserType().getPrivilege() > LoggedInUser.UserType.User.getPrivilege())
				return title + "#" + id;
		}
		return title;
	}
	
	/**
	 * Saves all settings for the currently logged in user.
	 */
	public void saveSettings() {
		settings.edit()
		        .putInt(Settings.NUM_RECENT_SEARCHES.key, Settings.NUM_RECENT_SEARCHES.value)
		        .putBoolean(Settings.NETFLIX.key, Settings.NETFLIX.value)
		        .putBoolean(Settings.HULU.key, Settings.HULU.value)
		        .putBoolean(Settings.HBO.key, Settings.HBO.value)
		        .putBoolean(Settings.PRIME.key, Settings.PRIME.value)
		        .putBoolean(Settings.DISNEY.key, Settings.DISNEY.value)
		        .apply();
	}
	
	/**
	 * Loads all settings for the currently logged in user.
	 */
	public void loadSettings() {
		int user = -1;
		LoginRepository r = LoginRepository.getInstance();
		if (r != null && r.isLoggedIn() && r.getUser().getUserType().getPrivilege() > LoggedInUser.UserType.Guest.getPrivilege())
			user = r.getUser().getUserId();
		
		if (user < 0) loadSettings("");
		else loadSettings(String.valueOf(user));
	}
	
	/**
	 * Loads all settings
	 * @param userId appended to {@code SETTINGS_LOC}
	 */
	private void loadSettings(String userId) {
		//Load settings
		settings = getSharedPreferences(SETTINGS_LOC + userId, Context.MODE_PRIVATE);
		int search = settings.getInt(Settings.NUM_RECENT_SEARCHES.key, 8);
		boolean netflix = settings.getBoolean(Settings.NETFLIX.key, false);
		boolean hbo = settings.getBoolean(Settings.HBO.key, false);
		boolean hulu = settings.getBoolean(Settings.HULU.key, false);
		boolean prime = settings.getBoolean(Settings.PRIME.key, false);
		boolean disney = settings.getBoolean(Settings.DISNEY.key, false);
		
		Settings.NUM_RECENT_SEARCHES.set(search);
		Settings.NETFLIX.set(netflix);
		Settings.HBO.set(hbo);
		Settings.HULU.set(hulu);
		Settings.PRIME.set(prime);
		Settings.DISNEY.set(disney);
	}
	
	/**
	 * Sets the star display for a custom rating bar. Parameters for the stars are in display order.
	 *
	 * @param rating out of 10
	 */
	public static void setStars(ImageView star1, ImageView star2, ImageView star3, ImageView star4, ImageView star5, int rating) {
		//Calculate star display
		Drawable full = getDrawableRes(R.drawable.full_star);
		Drawable half = getDrawableRes(R.drawable.half_star);
		Drawable empty = getDrawableRes(R.drawable.empty_star);
		star1.setImageDrawable(empty);
		star2.setImageDrawable(empty);
		star3.setImageDrawable(empty);
		star4.setImageDrawable(empty);
		star5.setImageDrawable(empty);
		
		if (rating > 1) {
			star1.setImageDrawable(full);
			if (rating > 3) {
				star2.setImageDrawable(full);
				if (rating > 5) {
					star3.setImageDrawable(full);
					if (rating > 7) {
						star4.setImageDrawable(full);
						if (rating == 10) star5.setImageDrawable(full);
						else if (rating == 9) star5.setImageDrawable(half);
					} else if (rating == 7) star4.setImageDrawable(half);
				} else if (rating == 5) star3.setImageDrawable(half);
			} else if (rating == 3) star2.setImageDrawable(half);
		} else if (rating == 1) star1.setImageDrawable(half);
	}
	
	/**
	 * Retrieves a drawable resource (from {@code res/drawable})
	 * @param res the id of the resource to retrieve
	 * @return the drawable
	 */
	public static Drawable getDrawableRes(int res) {
		return ResourcesCompat.getDrawable(getInstance().getResources(), res, null);
	}
	
	/**
	 * Adds parameters to a URL, since using parameters on {@code JsonRequests} don't work.
	 * @param URL the URL to add parameters to
	 * @param params the map of parameters to add
	 * @return the URL with the parameters added
	 */
	public static String getURLWithParams(String URL, Map<String, String> params) {
		StringBuilder b = new StringBuilder(URL);
		boolean first = true;
		for (String key : params.keySet()) {
			if (first) {
				b.append("?");
				first = false;
			}
			else b.append("&");
            b.append(key).append("=").append(params.get(key));
		}
		String r = b.toString();
		System.out.println("Doing search on: " + r);
		return r;
	}
	
	/**
	 * Encodes the given string into http format (i.e. space would be {@code %20}
	 * @param value the string to format
	 * @return the formatted string
	 */
	public static String HTTPEncode(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return value;
		}
	}
	
	/**
	 * Calculates the needed sample size to get the requested width/height, which ever is smaller.
	 * @param options the options to get the current height/width from
	 * @param reqWidth the requested width
	 * @param reqHeight the requested height
	 * @return the sample size that should be used
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1; // default to not zoom image
		
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = Math.min(heightRatio, widthRatio);
		}
		return inSampleSize;
	}
	
	/**
	 * Converts a drawable resource to a bitmap.
	 * @param drawable the drawable to convert
	 * @return a bitmap of the drawable
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap;
		
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
			if(bitmapDrawable.getBitmap() != null) {
				return bitmapDrawable.getBitmap();
			}
		}
		
		if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
			bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
		} else {
			bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
		}
		
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);
		return bitmap;
	}
	
	/**
	 * Converts a bitmap to Base64 string
	 * @param b the bitmap to convert
	 * @return Base64 string of the bitmap
	 */
	public static String bitmapToBase64(Bitmap b) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
		byte[] byteArray = byteArrayOutputStream .toByteArray();
		return Base64.encodeToString(byteArray, Base64.NO_WRAP);
	}
	
	/**
	 * Converts a drawable resource to Base64
	 * @param d the drawable to convert
	 * @return Base64 string of the drawable
	 *
	 * @see #drawableToBitmap(Drawable)
	 * @see #bitmapToBase64(Bitmap)
	 */
	public static String drawableToBase64(Drawable d) {
		return bitmapToBase64(drawableToBitmap(d));
	}
	
	/**
	 * Converts a Base64 string to bitmap
	 * @param base the Base64 string to convert
	 * @return null if invalid Base64, or bitmap of the valid Base64
	 */
	public static Bitmap base64ToBitmap(String base) {
		try {
			byte[] byteArray = Base64.decode(base, Base64.DEFAULT);
			return byteArrayToBitmap(byteArray);
		} catch (IllegalArgumentException ignored) {
			return null;
		}
	}
	
	/**
	 * Converts a byte array to bitmap
	 * @param bytes the byte array to convert
	 * @return bitmap of the byte array
	 */
	public static Bitmap byteArrayToBitmap(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}
	
	/**
	 * Rotates a bitmap by the specified angle.
	 * @param source the bitmap to rotate
	 * @param angle the angle to rotate
	 * @return the rotated bitmap
	 */
	public static Bitmap rotateBitmap(Bitmap source, float angle) {
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
	}
	
	/**
	 * Resizes a bitmap.
	 * @param source the bitmap to resize
	 * @param width the width to resize to
	 * @param height the height to resize to
	 * @return the resized bitmap
	 */
	public static Bitmap getBitmapAtSize(Bitmap source, int width, int height) {
		int x = 0;
		int y = 0;
		if (source.getWidth() > width) x = (source.getWidth() - width) / 2;
		if (source.getHeight() > height) y = (source.getHeight() - height) / 2;
		return Bitmap.createBitmap(source, x, y,
			Math.min(width, source.getWidth()), Math.min(height, source.getHeight()));
	}
	
	/**
	 * @return the app's content resolver
	 * @see Application#getContentResolver()
	 */
	public static ContentResolver contentResolver() {
		return getInstance().getContentResolver();
	}
	
	/**
	 * @return the app's current context
	 * @see Application#getApplicationContext()
	 */
	public Context getCurrentContext() {
		return getApplicationContext();
	}
	
	/**
	 * Sets the genres to the given array, after capitalization and sorting.
	 * @param genres the genre list to set to
	 */
	public void setGenres(String[] genres) {
		for (int i = 0; i < genres.length; i++) {
			genres[i] = capitalCase(genres[i]);
		}
		Arrays.sort(genres);
		this.genres = genres;
	}
	
	/**
	 * Capitalizes the first letter of every word.
	 * @param str the string to capital case
	 * @return {@code str}, but capital cased
	 */
	public static String capitalCase(String str) {
		String[] words = str.split("\\s");
		StringBuilder capitalizeWord = new StringBuilder();
		for(String w : words) {
			if (w.length() >= 1) {
				String first = w.substring(0, 1);
				String rest = w.substring(1);
				capitalizeWord.append(first.toUpperCase()).append(rest).append(" ");
			}
		}
		return capitalizeWord.toString().trim();
	}
	
	/**
	 * Sets the services to the given array, after capitalization.
	 * @param services the services list to set to
	 */
	public void setServices(String[] services) {
		for (int i = 0; i < services.length; i++) {
			services[i] = capitalCase(services[i]);
		}
		this.services = services;
	}
	
	/**
	 * @return the current genres
	 */
	public String[] getGenres() {
		return genres;
	}
	
	/**
	 * @return the current services
	 */
	public String[] getServices() {
		return services;
	}
	
	/**
	 * Returns the amount of pixels equivalent to the amount of dp given.
	 * @param dp the dp to change to px
	 * @return amount of pixels in the given dp
	 */
	public static int getPxFromDp(int dp) {
		return (int)(dp * mInstance.getResources().getDisplayMetrics().density + 0.5f);
	}
	
	/**
	 * Returns the amount of dp equivalent to the amount of px given.
	 * @param px the px to change to dp
	 * @return amount of pixels in the given px
	 */
	public static int getDpFromPx(int px) {
		return (int)(px / mInstance.getResources().getDisplayMetrics().density - 0.5f);
	}
	
	/**
	 * @return the instance of this class
	 */
	public static synchronized AppController getInstance() {
		return mInstance;
	}
	
	/**
	 * Retrieves the request queue, or makes one if it doesn't yet exist.
	 * @return the request queue
	 */
	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}
		
		return mRequestQueue;
	}
	
	/**
	 * Add the specified request to the request queue with the given tag.
	 * @param req the request to add to the queue
	 * @param tag the tag that identifies the request should it need to be canceled
	 */
	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}
	
	/**
	 * Add the specified request to the queue with the default tag.
	 * @param req the request to add to the queue
	 */
	public <T> void addToRequestQueue(Request<T> req) {
		addToRequestQueue(req, TAG);
	}
	
	/**
	 * Cancels all pending requests with the given tag.
	 * @param tag the tag to cancel all requests with
	 */
	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) mRequestQueue.cancelAll(tag);
	}
}
