package com.example.giftplannerv1;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import activities.LoginActivity;
import data.UserModel;
import ui.EventsFragment;
import ui.LoginFragment;

import androidx.test.rule.ActivityTestRule;
/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppInstrumentTest {
    @Rule
    public ActivityTestRule<LoginActivity> activityRule = new ActivityTestRule<>(LoginActivity.class);
    private UserModel userModel;
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.giftplannerv1", appContext.getPackageName());
    }

    @Test
    public void activityTest(){
        LoginActivity activity = activityRule.getActivity();
        assertNotNull(activity);
    }

    @Test
    public void fragmentTest(){
        LoginActivity activity = activityRule.getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        View fragmentView = fragment.getView();
        assertNotNull(fragment);
    }
    @Test
    public void viewsButtonsAndInputsTest(){
        LoginActivity activity = activityRule.getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);

        onView(withId(R.id.textview_first)).check(matches(isDisplayed()));
        onView(withId(R.id.login_email)).check(matches(isDisplayed()));
        onView(withId(R.id.login_password)).check(matches(isDisplayed()));
        onView(withId(R.id.login_button)).check(matches(isDisplayed()));
        onView(withId(R.id.signup_button)).check(matches(isDisplayed()));
    }
    @Test
    public void fragmentChangeTest(){
        LoginActivity activity = activityRule.getActivity();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);

        onView(withId(R.id.login_button)).perform(click());
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.nav_host_fragment_content_main);
        assertFalse(currentFragment instanceof LoginFragment);
    }



}