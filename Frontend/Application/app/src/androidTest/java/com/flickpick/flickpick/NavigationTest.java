package com.flickpick.flickpick;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.NavigationViewActions.navigateTo;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isFocusable;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.IsNot.not;

import android.app.Application;
import android.view.Gravity;
import android.view.View;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.flickpick.flickpick.data.LoginDataSource;
import com.flickpick.flickpick.data.LoginRepository;
import com.flickpick.flickpick.ui.start.StartActivity;
import com.flickpick.flickpick.util.AppController;
import com.google.common.collect.Iterables;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public final class NavigationTest {
	//necessary to set login details without signing in
	//and before the nav activity loads
	@Rule
	public ActivityScenarioRule<Debug> activityRule =
		new ActivityScenarioRule<>(Debug.class);
	
	@Before
	public void intentsInit() {
		// initialize Espresso Intents capturing
		Intents.init();
	}
	
	@After
	public void intentsTeardown() {
		// release Espresso Intents capturing
		Intents.release();
	}
	
	private void goToNavAct() {
		onView(withId(R.id.debugBtnNav)).perform(click());
	}
	
	private void guestLogin() {
		LoginRepository r = LoginRepository.getInstance(new LoginDataSource());
		r.login("guest", "1234567890");
	}
	
	private void userLogin() {
		LoginRepository r = LoginRepository.getInstance(new LoginDataSource());
		r.login("toDelete", "Junit4!");
	}
	
	private void adminLogin() {
		LoginRepository r = LoginRepository.getInstance(new LoginDataSource());
		r.login("dc5", "DC5!dc");
	}
	
	@Test
	public void guestProfileFull() {
		guestLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		
		String guestEmail = getApplicationContext().getString(R.string.guest_sign_in);
		onView(withId(R.id.emailText)).check(matches(withText(guestEmail)));
		
		onView(withId(R.id.searchShowAmntSlider)).perform(swipeRight());
		onView(withId(R.id.searchShowAmnt)).check(matches(not(withText("8"))));
		
		pressBack();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		//check saved
		onView(withId(R.id.searchShowAmnt)).check(matches(not(withText("8"))));
		
		onView(withId(R.id.searchShowAmnt)).perform(replaceText("22"));
		
		pressBack();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		//check saved
		onView(withId(R.id.searchShowAmnt)).check(matches(withText("22")));
		
		onView(withId(R.id.accountSettingsText)).check(matches(anyOf(not(isDisplayed()))));
		
		pressBack();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		onView(withId(R.id.nav_friends)).check(doesNotExist());
	}
	
	@Test
	public void userProfileEmail() {
		userLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		
		String userEmail = "unit@test.com";
		onView(withId(R.id.emailText)).check(matches(withText(userEmail)));
	}
	
	@Test
	public void userProfileServices() throws InterruptedException {
		userLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		onView(withId(R.id.netflixCheckbox)).perform(click()).check(matches(anyOf(isChecked())));
		onView(withId(R.id.hboButton)).perform(click());
		
		pressBack();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		
		onView(withId(R.id.netflixCheckbox)).check(matches(anyOf(isChecked())));
		onView(withId(R.id.hboCheckbox)).check(matches(anyOf(isChecked())));
		//reset for other tests
		onView(withId(R.id.netflixCheckbox)).perform(click());
		onView(withId(R.id.hboButton)).perform(click());
		
		onView(withId(R.id.netflixCheckbox)).check(matches(anyOf(not(isChecked()))));
		onView(withId(R.id.hboCheckbox)).check(matches(anyOf(not(isChecked()))));
	}
	
	@Test
	public void userProfileAllServiceButtons() throws InterruptedException {
		userLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		
		onView(withId(R.id.netflixButton)).perform(scrollTo(), click());
		onView(withId(R.id.hboButton)).perform(scrollTo(), click());
		onView(withId(R.id.huluButton)).perform(scrollTo(), click());
		onView(withId(R.id.amazonPrimeButton)).perform(scrollTo(), click());
		onView(withId(R.id.disneyButton)).perform(scrollTo(), click());
		
		pressBack();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		
		onView(withId(R.id.netflixCheckbox)).check(matches(anyOf(isChecked())));
		onView(withId(R.id.hboCheckbox)).check(matches(anyOf(isChecked())));
		onView(withId(R.id.huluCheckbox)).check(matches(anyOf(isChecked())));
		onView(withId(R.id.amazonPrimeCheckbox)).check(matches(anyOf(isChecked())));
		onView(withId(R.id.disneyCheckbox)).check(matches(anyOf(isChecked())));
		//uncheck to reset for other tests
		onView(withId(R.id.netflixButton)).perform(scrollTo(), click());
		onView(withId(R.id.hboButton)).perform(scrollTo(), click());
		onView(withId(R.id.huluButton)).perform(scrollTo(), click());
		onView(withId(R.id.amazonPrimeButton)).perform(scrollTo(), click());
		onView(withId(R.id.disneyButton)).perform(scrollTo(), click());
		
		onView(withId(R.id.netflixCheckbox)).check(matches(anyOf(not(isChecked()))));
		onView(withId(R.id.hboCheckbox)).check(matches(anyOf(not(isChecked()))));
		onView(withId(R.id.huluCheckbox)).check(matches(anyOf(not(isChecked()))));
		onView(withId(R.id.amazonPrimeCheckbox)).check(matches(anyOf(not(isChecked()))));
		onView(withId(R.id.disneyCheckbox)).check(matches(anyOf(not(isChecked()))));
	}
	
	@Test
	public void userProfileChangeUsername() throws InterruptedException {
		userLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		
		onView(withId(R.id.changeUsernameButton)).perform(click());
		String og = "toDelete";
		onView(withId(R.id.dialog_textbox)).check(matches(anyOf(withText(og))));
		
		String newName = "unitTesting";
		onView(withId(R.id.dialog_textbox)).perform(replaceText(newName));
		onView(allOf(withText(R.string.change_username), isFocusable())).perform(click());
		
		onView(withId(R.id.usernameText)).check(matches(withText(newName)));
		
		onView(withId(R.id.changeUsernameButton)).perform(click());
		onView(withId(R.id.dialog_textbox)).perform(replaceText(og));
		onView(allOf(withText(R.string.change_username), isFocusable())).perform(click());
	}
	
	@Test
	public void userProfileChangeEmail() throws InterruptedException {
		userLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		
		onView(withId(R.id.changeEmailButton)).perform(click());
		String og = "unit@test.com";
		onView(withId(R.id.dialog_textbox)).check(matches(anyOf(withText(og))));
		
		String newName = "test@unit.com";
		onView(withId(R.id.dialog_textbox)).perform(replaceText(newName));
		onView(allOf(withText(R.string.change_email), isFocusable())).perform(click());
		
		onView(withId(R.id.emailText)).check(matches(withText(newName)));
		
		onView(withId(R.id.changeEmailButton)).perform(click());
		onView(withId(R.id.dialog_textbox)).perform(replaceText(og));
		onView(allOf(withText(R.string.change_email), isFocusable())).perform(click());
	}
	
	@Test
	public void userProfileChangePass() throws InterruptedException {
		userLogin();
		goToNavAct();
		String og = "Junit4!";
		LoginRepository r = LoginRepository.getInstance();
		assert r != null; //set in userLogin
		assert r.getUser().getPassword().equals(og);
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		
		onView(withId(R.id.changePasswordButton)).perform(click());
		
		String newName = "unitJ4!";
		onView(withId(R.id.dialog_textbox)).perform(replaceText(newName));
		onView(withId(R.id.dialog_textbox2)).perform(replaceText(newName));
		
		onView(allOf(withText(R.string.change_password), isFocusable())).perform(click());
		
		assert r.getUser().getPassword().equals(newName);
		
		onView(withId(R.id.changePasswordButton)).perform(click());
		onView(withId(R.id.dialog_textbox)).perform(replaceText(og));
		onView(withId(R.id.dialog_textbox2)).perform(replaceText(og));
		onView(allOf(withText(R.string.change_password), isFocusable())).perform(click());
	}
	
	//skip reset, it doesn't do anything
	//@Test not working
	public void userProfileDelete() throws InterruptedException {
		userLogin();
		goToNavAct();
		
		// Open Drawer to click on navigation.
		onView(withId(R.id.drawer_layout))
			.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			.perform(DrawerActions.open()); // Open Drawer
		
		// go to account.
		onView(withId(R.id.nav_view))
			.perform(navigateTo(R.id.nav_account));
		Thread.sleep(1000);
		
		onView(withId(R.id.deleteButton)).perform(scrollTo(), click());
		onView(allOf(withText(android.R.string.ok), isFocusable())).perform(click());
		
		onView(withId(R.id.start_sign_in)).perform(click());
		//TRY LOG IN
		String username = "toDelete";
		
		onView(withId(R.id.email)).perform(typeText(username));
		onView(withId(R.id.password)).perform(typeText("Junit4!"), closeSoftKeyboard());
		onView(withId(R.id.login_btn)).perform(click());
		
		//assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
		//	NavigationActivity.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//can have false positive
		onView(withId(R.id.login_btn)); //verify still on login screen
		
		pressBack();
		onView(withId(R.id.start_reg)).perform(click());
		
		//recreate the account to be used in other unit tests
		onView(withId(R.id.usernameInput)).perform(typeText("toDelete"));
		onView(withId(R.id.emailInput)).perform(typeText("unit@test.com"));
		onView(withId(R.id.passwordInput)).perform(typeText("Junit4!"));
		onView(withId(R.id.passwordInput2)).perform(typeText("Junit4!"), closeSoftKeyboard());
		
		onView(withId(R.id.signinBtn)).perform(click());
	}
	
	@Test
	public void guestMovie() {
		guestLogin();
		goToNavAct();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		onView(first(allOf(withId(R.id.movie1Btn)))).perform(scrollTo(), click());
		
		onView(withId(R.id.setFavoriteBtn)).check(matches(anyOf(not(isDisplayed()))));
		onView(withId(R.id.addWatchlistBtn)).check(matches(anyOf(not(isDisplayed()))));
		
		onView(withId(R.id.reviewsBtn)).perform(click());
		onView(withId(R.id.createReviewBtn)).check(matches(anyOf(not(isDisplayed()))));
	}
	
	@Test
	public void userMovie() {
		userLogin();
		goToNavAct();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		onView(first(allOf(withId(R.id.movie1Btn)))).perform(scrollTo(), click());
		
		onView(withId(R.id.movieEditBtn)).check(matches(anyOf(not(isDisplayed()))));
		
		onView(withId(R.id.setFavoriteBtn)).perform(click());
		onView(withId(R.id.addWatchlistBtn)).perform(click());
		
		onView(withId(R.id.reviewsBtn)).perform(click());
		onView(withId(R.id.createReviewBtn)).perform(click());
		String review = "This review is part of a unit test!";
		onView(withId(R.id.reviewRating)).perform(swipeRight());
		onView(withId(R.id.commentText)).perform(typeText(review), closeSoftKeyboard());
		onView(withId(R.id.postReviewBtn)).perform(click());
		
		onView(withChild(withText("Review By : \"toDelete\""))).check(matches(withChild(withText('"' + review + '"'))));
	}
	
	@Test
	public void adminProfileFull() {
		adminLogin();
		goToNavAct();
		
		try {
			// Open Drawer to click on navigation.
			onView(withId(R.id.drawer_layout))
				.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
				.perform(DrawerActions.open()); // Open Drawer
			
			// go everywhere
			onView(withId(R.id.nav_view))
				.perform(navigateTo(R.id.nav_user_list));
			
			// Open Drawer to click on navigation.
			onView(withId(R.id.drawer_layout))
				.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
				.perform(DrawerActions.open()); // Open Drawer
			
			// go everywhere
			onView(withId(R.id.nav_view))
				.perform(navigateTo(R.id.nav_add_movie));
			
			onView(withText(android.R.string.cancel)).perform(click());
			
			// Open Drawer to click on navigation.
			onView(withId(R.id.drawer_layout))
				.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
				.perform(DrawerActions.open()); // Open Drawer
			//
			//// go everywhere
			//onView(withId(R.id.nav_view))
			//	.perform(navigateTo(R.id.nav_friends));
			//
			//Thread.sleep(100);
			//
			//// Open Drawer to click on navigation.
			//onView(withId(R.id.drawer_layout))
			//	.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
			//	.perform(DrawerActions.open()); // Open Drawer
			
			// go everywhere
			onView(withId(R.id.nav_view))
				.perform(navigateTo(R.id.nav_edit_user));
			
			onView(withText(android.R.string.cancel)).perform(click());
			
			// Open Drawer to click on navigation.
			onView(withId(R.id.drawer_layout))
				.check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
				.perform(DrawerActions.open()); // Open Drawer
			
			// go everywhere
			onView(withId(R.id.nav_view))
				.perform(navigateTo(R.id.nav_logout));
			Thread.sleep(1000);
			onView(withText(android.R.string.yes)).perform(click());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	//private Matcher<View> getAtPosition(final Matcher<View> matcher, final int position) {
	//	return new BaseMatcher<View>() {
	//		int counter = 0;
	//		@Override
	//		public boolean matches(final Object item) {
	//			if (matcher.matches(item)) {
	//				if(counter == position) {
	//					counter++;
	//					return true;
	//				}
	//				counter++;
	//			}
	//			return false;
	//		}
	//
	//		@Override
	//		public void describeTo(final Description description) {
	//			description.appendText("Element at hierarchy position "+position);
	//		}
	//	};
	//}
	
	private <T> Matcher<T> first(final Matcher<T> matcher) {
		return new BaseMatcher<T>() {
			boolean isFirst = true;
			Object matching = null;
			
			@Override
			public boolean matches(final Object item) {
				if (matching != null) return matching == item;
				if (isFirst && matcher.matches(item)) {
					isFirst = false;
					matching = item;
					return true;
				}
				
				return false;
			}
			
			@Override
			public void describeTo(final Description description) {
				description.appendText("should return first matching item");
			}
		};
	}
}
