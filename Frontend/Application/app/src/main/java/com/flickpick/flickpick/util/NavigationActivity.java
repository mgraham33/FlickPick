package com.flickpick.flickpick.util;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.TextView;

import com.flickpick.flickpick.R;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.data.model.LoggedInUser;
import com.flickpick.flickpick.data.model.OnUserChangedListener;
import com.flickpick.flickpick.databinding.ActivityNavigationBinding;
import com.flickpick.flickpick.databinding.NavHeaderNavigationBinding;
import com.flickpick.flickpick.ui.admin.EditMovieFragment;
import com.flickpick.flickpick.ui.search.SearchFragment;
import com.flickpick.flickpick.ui.search.SearchSuggestions;
import com.flickpick.flickpick.ui.search.SearchableActivity;
import com.flickpick.flickpick.ui.start.StartActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Objects;

/**
 * Activity that manages navigation between screens. All screens that should have the navigation
 * side bar should be a fragment that has been added to the {@code navigation/mobile_navigation.xml}.
 *
 * @author Dustin Cornelison
 */
public class NavigationActivity extends AppCompatActivity {
	
	private AppBarConfiguration mAppBarConfiguration;
	private ActivityNavigationBinding binding;
	private NavController navController;
	private SearchView search;
	private Toolbar toolbar;
	private ActionMenuView actionView;
	private TextView username;
	private TextView email;
	private ImageView pfp;
	private LoggedInUser user;
	private OnUserChangedListener userChanged;
	private String currentSearch;
	private static SearchCache cache;
	private static Intent results;
	
	/**
	 * Used for search: sets current search results.
	 * @param i the intent with the results; can be null if
	 * not in the search screen
	 */
	public static void setResults(Intent i) {
		if (results != null) cache.addSearch(results);
		results = i;
	}
	
	/**
	 * Used for search: gives the current search results.
	 * @return the current search results
	 */
	public static Intent getResults() {
		return results;
	}
	
	/**
	 * Creates the view and manages UI.
	 * @param savedInstanceState previous instance, if saved
	 * @see androidx.fragment.app.FragmentActivity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		results = null;
		cache = new SearchCache();
		LoginRepository r = LoginRepository.getInstance();
		user = null;
		if (r != null && r.isLoggedIn()) {
			user = r.getUser();
			userChanged = this::updateHeader;
			user.addOnUserChangedListener(userChanged);
		}
		
		binding = ActivityNavigationBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		toolbar = binding.appBarTest.toolbar;
		search = binding.appBarTest.searchView;
		actionView = binding.appBarTest.actionView;
		setSupportActionBar(toolbar);
		
		DrawerLayout drawer = binding.drawerLayout;
		NavigationView navigationView = binding.navView;
		if (user == null || user.getUserType().getPrivilege() < LoggedInUser.UserType.Admin.getPrivilege()) {
			//if not admin, remove admin tools
			navigationView.getMenu().removeItem(R.id.nav_add_movie);
			navigationView.getMenu().removeItem(R.id.nav_edit_movie);
			navigationView.getMenu().removeItem(R.id.nav_remove_movie);
			navigationView.getMenu().removeItem(R.id.nav_edit_user);
			navigationView.getMenu().removeItem(R.id.nav_user_list);
			if (user == null || user.getUserType().getPrivilege() < LoggedInUser.UserType.User.getPrivilege()) {
				navigationView.getMenu().removeItem(R.id.nav_friends);
				if (user == null) navigationView.getMenu().removeItem(R.id.nav_account);
			}
		}
		NavHeaderNavigationBinding header = NavHeaderNavigationBinding.bind(navigationView.getHeaderView(0));
		
		username = header.navUsername;
		email = header.navEmail;
		pfp = header.navProfilePic;
		
		if (user != null) updateHeader(user);
		
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		mAppBarConfiguration = new AppBarConfiguration.Builder(
			R.id.nav_home, R.id.nav_friends, R.id.nav_account, R.id.nav_logout)
			.setOpenableLayout(drawer)
			.build();
		drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
			@Override
			public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
				closeKeyboard(drawerView);
				closeSearch();
			}
			
			@Override
			public void onDrawerOpened(@NonNull View drawerView) {
				drawer.requestFocus();
			}
			@Override
			public void onDrawerClosed(@NonNull View drawerView) {}
			@Override
			public void onDrawerStateChanged(int newState) {}
		});
		
		navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);
		toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
		toolbar.setNavigationOnClickListener(view -> {
			Openable o = mAppBarConfiguration.getOpenableLayout();
			if (o != null) o.open();
			closeKeyboard(view);
			closeSearch();
		});
		
		toolbar.setTitle(R.string.home_title);
		navigationView.setNavigationItemSelectedListener(item -> {
			int id = item.getItemId();
			Bundle b = null;
			NavDestination dest = navController.getCurrentDestination();
			int nav;
			if (dest == null) return false;
			if (id == R.id.nav_home) nav = R.id.homeFragment;
			else if (id == R.id.nav_friends) nav = R.id.friendsListFragment;
			else if (id == R.id.nav_account) nav = R.id.userProfileFragment;
			else if (id == R.id.nav_add_movie) {
				nav = R.id.addMovieFragment;
				b = new Bundle();
				b.putInt(EditMovieFragment.TYPE, EditMovieFragment.CREATE);
			} else if (id == R.id.nav_edit_movie) {
				nav = R.id.editMovieFragment;
				b = new Bundle();
				b.putInt(EditMovieFragment.TYPE, EditMovieFragment.EDIT);
			} else if (id == R.id.nav_remove_movie) {
				nav = R.id.removeMovieFragment;
				b = new Bundle();
				b.putInt(EditMovieFragment.TYPE, EditMovieFragment.DELETE);
			} else if (id == R.id.nav_edit_user) {
				nav = R.id.editUserFragment;
			} else if (id == R.id.nav_user_list) {
				nav = R.id.userListFragment;
			} else if (id == R.id.nav_logout) {
				confirmLogout(r);
				return false;
			} else return false;
			
			if (dest.getId() == nav) {
				finishNavigate();
				return false;
			}
			if (b == null) navController.navigate(nav);
			else navController.navigate(nav, b);
			finishNavigate(nav);

			return true;
		});
		search.setOnSearchClickListener(view -> openSearch());
		search.setOnCloseListener(() -> {
			closeSearch();
			return false;
		});
		search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				closeKeyboard(binding.getRoot());
				closeSearch();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
		
		pfp.setOnClickListener(v -> {
			if (r != null && r.isLoggedIn() && r.getUser().getUserType().getPrivilege() > LoggedInUser.UserType.Guest.getPrivilege())
			navTo(R.id.userProfileFragment);
		});
	}
	
	private void confirmLogout(LoginRepository r) {
		AlertDialog d = new AlertDialog.Builder(NavigationActivity.this)
			.setTitle(R.string.logout_confirm_title)
			.setMessage(R.string.logout_confirm_msg)
			.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
				Intent in = new Intent(NavigationActivity.this, StartActivity.class);
				if (r != null) r.logout();
				startActivity(in);
				finish();
			})
			.setNegativeButton(android.R.string.no, null).create();
		d.show();
	}
	
	/**
	 * When the navigation activity is exited, also remove the listener for the user.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (user != null) user.removeOnUserChangedListener(userChanged);
	}
	
	/**
	 * Handles navigate up with respect to the navigation.
	 * @see androidx.appcompat.app.AppCompatActivity#onSupportNavigateUp()
	 * @see NavigationUI#navigateUp(NavController, AppBarConfiguration)
	 * @return true if the navigate up was successful
	 */
	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_test);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
			|| super.onSupportNavigateUp();
	}
	
	/**
	 * Handles when the user presses the back button.
	 * First checks if the navigation bar is open, and closes it if so.
	 * Otherwise checks if search bar is active, closes/deactivates it if so.
	 * Otherwise performs the normal back operation (will set the title/details
	 * of the fragment if still in the NavigationActivity).
	 */
	@Override
	public void onBackPressed() {
		Openable o = mAppBarConfiguration.getOpenableLayout();
		if (o != null && o.isOpen()) o.close();
		else if (!search.isIconified()) closeSearch();
		else {
			AppController.getInstance().cancelPendingRequests(SearchFragment.IMG_REQ_TAG);
			if (navController.getPreviousBackStackEntry() != null)
				super.onBackPressed();
			else {
				LoginRepository r = LoginRepository.getInstance();
				if (r == null) super.onBackPressed();
				else confirmLogout(r);
				return;
			}
			NavDestination dest = navController.getCurrentDestination();
			assert dest != null; //shouldn't happen
			finishNavigate(dest.getId(), true);
		}
	}
	
	/**
	 * Called when activity is first started, when the back button being pressed leads here,
	 * and when the app is resumed from sleep.
	 * If the current page is the search screen, tries to redisplay the results.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		setValuesFromResults();
		if (currentSearch != null) {
			navController.navigate(R.id.searchFragment);
			finishNavigate(R.id.searchFragment);
		}
	}
	
	/**
	 * Sets the currentSearch from the results Intent
	 */
	private void setValuesFromResults() {
		if (results != null) {
			currentSearch = results.getStringExtra(SearchableActivity.SEARCH);
		} else {
			currentSearch = null;
		}
	}
	
	/**
	 * Sets up the search button.
	 * @param menu the menu to create
	 * @return {@code true} to show the menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Get the SearchView and set the searchable configuration
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		//Assumes current activity is the searchable activity
		search.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchableActivity.class)));
		
		return true;
	}
	
	/**
	 * Calls {@code finishNavigate(id, false)}
	 * @see #finishNavigate(int, boolean)
	 * @param id the id of the fragment that's being navigated to
	 */
	private void finishNavigate(int id) {
		finishNavigate(id, false);
	}
	
	/**
	 * Finishes the navigation to the given fragment id;
	 * this means setting the title, visibility of the search button, and navigation icon.
	 * If {@code back} is true, will retrieve previous search results/clear them if necessary.
	 * @param id the id of the fragment that's being navigated to
	 * @param back whether this navigation was because of a back button press
	 */
	private void finishNavigate(int id, boolean back) {
		String t = (String)Objects.requireNonNull(navController.getCurrentDestination()).getLabel();
		setTitle(t);
		closeSearch();
		closeKeyboard(binding.getRoot());
		finishNavigate();
		if (id == R.id.searchFragment) {
			if (back) {
				results = cache.back();
				setValuesFromResults();
			}
			toolbar.setTitle(getString(R.string.search_title, currentSearch));
			search.setVisibility(View.VISIBLE);
		} else {
			setResults(null); //only adds results to stack if not null
			if (id == R.id.homeFragment) {
				toolbar.setTitle(R.string.home_title);
				search.setVisibility(View.VISIBLE);
			} else if (id == R.id.friendsListFragment) {
				toolbar.setTitle(R.string.friend_list_title);
				search.setVisibility(View.GONE);
			} else if (id == R.id.userProfileFragment) {
				toolbar.setTitle(R.string.profile_title);
				search.setVisibility(View.GONE);
			} else if (id == R.id.addMovieFragment) {
				toolbar.setTitle(R.string.admin_add_movie);
				search.setVisibility(View.GONE);
			} else if (id == R.id.editMovieFragment) {
				toolbar.setTitle(R.string.admin_edit_movie);
				search.setVisibility(View.GONE);
			} else if (id == R.id.removeMovieFragment) {
				toolbar.setTitle(R.string.admin_remove_movie);
				search.setVisibility(View.GONE);
			} else if (id == R.id.editUserFragment) {
				toolbar.setTitle(R.string.admin_edit_user);
				search.setVisibility(View.GONE);
			} else if (id == R.id.friendRequestFragment) {
				toolbar.setTitle(R.string.friend_request_title);
				search.setVisibility(View.GONE);
			} else if (id == R.id.userListFragment) {
				toolbar.setTitle(R.string.user_list);
				search.setVisibility(View.GONE);
			}
			setValuesFromResults();
		}
		toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
	}
	
	/**
	 * Closes the navigation bar
	 */
	private void finishNavigate() {
		Openable o = mAppBarConfiguration.getOpenableLayout();
		if (o != null) o.close();
	}
	
	/**
	 * Closes the user's keyboard.
	 * @param v the view to close the keyboard on
	 */
	private void closeKeyboard(View v) {
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
	}
	
	
	/**
	 * Navigates to the given fragment id.
	 * @param id the id of the fragment to navigate to
	 */
	public void navTo(int id){
		navTo(id, (Bundle)null);
	}
	
	/**
	 * Navigates to the given fragment id, and provides an extra bundle
	 * of information if the screen needs it.
	 * @param id the id of the fragment to navigate to
	 * @param bundle the bundle of extra information to give to the fragment
	 */
	public void navTo(int id, Bundle bundle) {
		if (id != Objects.requireNonNull(navController.getCurrentDestination()).getId()) {
			navController.navigate(id, bundle);
			finishNavigate(id);
		} else finishNavigate();
	}
	
	/**
	 * Navigates to the given fragment id, and provides a plain text title that
	 * should be displayed.
	 * @param id the id of the fragment to navigate to
	 * @param title the title to display on the fragment to the user
	 */
	public void navTo(int id, String title) {
		navTo(id);
		setTitleAndLabel(title);
	}
	
	/**
	 * Navigates to the given fragment id, and provides a plain text title that
	 * should be displayed as well as an extra bundle of information if the screen
	 * needs it.
	 * @param id the id of the fragment to navigate to
	 * @param title the title to display on the fragment to the user
	 * @param bundle the bundle of extra information to give to the fragment
	 */
	public void navTo(int id, String title, Bundle bundle) {
		navTo(id, bundle);
		setTitleAndLabel(title);
	}
	
	private void setTitleAndLabel(String title) {
		toolbar.setTitle(title);
		Objects.requireNonNull(navController.getCurrentDestination()).setLabel(title);
	}
	
	/**
	 * Sets the title of the current fragment.
	 * @param title what to set the title to
	 */
	public void setTitle(String title) {
		toolbar.setTitle(title);
	}
	
	/**
	 * Closes the search menu.
	 */
	private void closeSearch() {
		search.onActionViewCollapsed();
		actionView.getLayoutParams().width = ActionBar.LayoutParams.WRAP_CONTENT;
	}
	
	/**
	 * Opens the search menu.
	 */
	private void openSearch() {
		actionView.getLayoutParams().width = ActionBar.LayoutParams.MATCH_PARENT;
		if (currentSearch != null) search.setQuery(currentSearch, false);
	}
	
	/**
	 * Updates the navigation header with updated user information.
	 * @param u the user to update the header to
	 */
	private void updateHeader(LoggedInUser u) {
		user = u;
		username.setText(u.getNameAndId());
		email.setText(u.getEmail());
		pfp.setImageBitmap(u.getProfilePicture());
	}
}