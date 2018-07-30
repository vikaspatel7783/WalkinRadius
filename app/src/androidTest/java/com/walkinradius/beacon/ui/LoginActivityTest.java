package com.walkinradius.beacon.ui;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.walkinradius.beacon.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.times;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    private Context appContext;

    @Rule
    public ActivityTestRule<LoginActivity> signInActivity = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        appContext = InstrumentationRegistry.getTargetContext();

        // Given
        onView(ViewMatchers.withId(R.id.edtTxtUsername)).check(matches(isDisplayed()));
        onView(withId(R.id.edtTxtPassword)).check(matches(isDisplayed()));
        onView(withId(R.id.btnLogin)).check(matches(isDisplayed()));
    }

    /**
     * Given- user had not filled username
     * When - user clicks submit button
     * Then - user should see error message
     */
    @Test
    public void userShouldSeeErrorForEmptyUserNameField() throws Exception {
        // Given - refer setup() method

        // When
        onView(withId(R.id.btnLogin)).perform(click());

        // Then
        //onView(withId(R.id.txtViewResult)).check(matches(withText(SignInActivityPresenter.errorMsgCredentialEmpty)));

    }

    @Test
    public void testDashboardActivityLaunchUponSucessfulCredentialsVerification() {
        // Given - refer setup() method

        // When
        Intents.init();

        onView(withId(R.id.btnLogin)).perform(click());
        signInActivity.launchActivity(new Intent());
        intended(hasComponent(DashboardActivity.class.getName()));
        intended(hasComponent(DashboardActivity.class.getName()), times(0));

        Intents.release();

        // Then
    }

    @After
    public void tearDown() throws Exception {
    }

    /*class ToastMatcher extends TypeSafeMatcher<Root> {

        @Override
        public void describeTo(Description description) {
            description.appendText("is toast");
        }

        @Override
        public boolean matchesSafely(Root root) {
            int type = root.getWindowLayoutParams().get().type;
            if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
                IBinder windowToken = root.getDecorView().getWindowToken();
                IBinder appToken = root.getDecorView().getApplicationWindowToken();
                if (windowToken == appToken) {
                    return true;
                }
            }
            return false;
        }

    }*/
}