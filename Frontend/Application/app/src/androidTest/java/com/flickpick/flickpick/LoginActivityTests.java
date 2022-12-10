package com.flickpick.flickpick;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.ext.truth.content.IntentSubject.assertThat;

import static org.hamcrest.CoreMatchers.anyOf;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.flickpick.flickpick.ui.login.LoginActivity;
import com.flickpick.flickpick.ui.signup.SignupActivity;
import com.google.common.collect.Iterables;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTests {
	@Rule
	public ActivityScenarioRule<LoginActivity> activityRule =
		new ActivityScenarioRule<>(LoginActivity.class);
	
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
	public void userLogin() throws InterruptedException { //this test doesn't work all the time
		String username = "Username";
		
		onView(withId(R.id.email)).perform(typeText(username));
		onView(withId(R.id.password)).perform(typeText("P@ssword123"), closeSoftKeyboard());
		Thread.sleep(100);
		onView(withId(R.id.login_btn)).perform(click());
		
		//assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
		//	NavigationActivity.class);
		Thread.sleep(1000);
		onView(withId(R.id.navUsername)).check(matches(withText(username)));
		onView(withId(R.id.navEmail)).check(matches(withText("emailaddress@gmail.com")));
	}
	
	@Test
	public void toUserReg() {
		onView(withId(R.id.register_btn)).perform(click());
		
		assertThat(Iterables.getOnlyElement(Intents.getIntents())).hasComponentClass(
			SignupActivity.class);
		
		//while we're here, create a new account to be deleted in other unit tests
		onView(withId(R.id.usernameInput)).perform(typeText("toDelete"));
		onView(withId(R.id.emailInput)).perform(typeText("unit@test.com"));
		onView(withId(R.id.passwordInput)).perform(typeText("Junit4!"));
		onView(withId(R.id.passwordInput2)).perform(typeText("Junit4!"), closeSoftKeyboard());
		
		onView(withId(R.id.signinBtn)).perform(click());
	}
	
	
	@Test
	public void validUsername() {
		onView(withId(R.id.email)).perform(typeText("ab"));
		onView(withId(R.id.password)).perform(typeText("P@ssword123"), closeSoftKeyboard());
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		//text is cleared
		onView(withId(R.id.email)).perform(replaceText("abb"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		//text not cleared
		onView(withId(R.id.email)).perform(replaceText("ab_"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.email)).perform(replaceText("ab1"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.email)).perform(replaceText("ab@"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		
		onView(withId(R.id.email)).perform(replaceText("ab#"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
	}
	
	@Test
	public void validPassword() {
		onView(withId(R.id.email)).perform(typeText("abb"));
		onView(withId(R.id.password)).perform(typeText("P@5s"), closeSoftKeyboard());
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		
		onView(withId(R.id.password)).perform(typeText("bb"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.password)).perform(typeText(" "));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		
		onView(withId(R.id.password)).perform(typeText("b"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.password)).perform(replaceText(" P@5sbb"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		
		onView(withId(R.id.password)).perform(replaceText("P@5SBB"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		onView(withId(R.id.password)).perform(typeText("b"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.password)).perform(replaceText("p@5sbb"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		onView(withId(R.id.password)).perform(typeText("B"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.password)).perform(replaceText("Pa5sbb"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		onView(withId(R.id.password)).perform(typeText("@"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
		
		onView(withId(R.id.password)).perform(replaceText("P@ssbb"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isNotEnabled())));
		onView(withId(R.id.password)).perform(typeText("6"));
		onView(withId(R.id.login_btn)).check(matches(anyOf(isEnabled())));
	}
	
	
}
