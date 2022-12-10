package com.flickpick.flickpick;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;

import com.flickpick.flickpick.ui.login.LoginActivity;
import com.flickpick.flickpick.ui.signup.SignupActivity;
import com.google.common.collect.Iterables;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.flickpick.flickpick.ui.start.StartActivity;
import com.flickpick.flickpick.util.NavigationActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public final class StartActivityTests {
	
	@Rule
	public ActivityScenarioRule<StartActivity> activityRule =
		new ActivityScenarioRule<>(StartActivity.class);
	
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
	
	@Test
	public void guestLogin() {
		onView(withId(R.id.start_guest)).perform(click());
		
		assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
			NavigationActivity.class);
		
		onView(withId(R.id.navUsername)).check(matches(withText("guest")));
		
		onView(withId(R.id.navEmail)).check(matches(withText(R.string.guest_sign_in)));
	}
	
	@Test
	public void toUserLogin() {
		onView(withId(R.id.start_sign_in)).perform(click());
		
		assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
			LoginActivity.class);
	}
	
	@Test
	public void toUserReg() {
		onView(withId(R.id.start_reg)).perform(click());
		
		assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
			SignupActivity.class);
	}
}
