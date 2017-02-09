package org.buildmlearn.toolkit.espresso;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.WindowManager;
import android.widget.TextView;

import org.buildmlearn.toolkit.R;
import org.buildmlearn.toolkit.activity.TemplateEditor;
import org.buildmlearn.toolkit.constant.Constants;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by VOJJALA TEJA on 05-02-2017.
 */
@Ignore
@RunWith(AndroidJUnit4.class)
@LargeTest
public class QuizTest {
    @Rule
    public final ActivityTestRule<TemplateEditor> mActivityRule =
            new ActivityTestRule<TemplateEditor>(TemplateEditor.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, TemplateEditor.class);
                    result.putExtra(Constants.TEMPLATE_ID, 2);
                    return result;
                }
            };

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep() {
        sleep(1000);
    }

    private static void allowPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= 23) {
            UiDevice device = UiDevice.getInstance(getInstrumentation());
            UiObject allowPermissions = device.findObject(new UiSelector().text("Allow"));
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click();
                } catch (UiObjectNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @UiThreadTest
    @Before
    public void setUp() throws Throwable {
        final Activity activity = mActivityRule.getActivity();
        mActivityRule.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                KeyguardManager mKG = (KeyguardManager) activity.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock mLock = mKG.newKeyguardLock(Context.KEYGUARD_SERVICE);
                mLock.disableKeyguard();

                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
            }
        });
    }

    private void toolbarTitle() {
        String title = "Quiz Template";
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(title)));
    }

    private void addQuestions() {
        String question = "Test Quiz Question";

        onView(withId(R.id.button_add_item)).perform(click());
        onView(withId(R.id.quiz_question)).perform(typeText(question));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.quiz_option_1)).perform(scrollTo());
        onView(withId(R.id.quiz_option_1)).perform(typeText("A"));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.quiz_option_2)).perform(scrollTo());
        onView(withId(R.id.quiz_option_2)).perform(typeText("B"));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.quiz_radio_2)).perform(scrollTo()).perform(click());
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.quiz_option_3)).perform(scrollTo());
        onView(withId(R.id.quiz_option_3)).perform(typeText("C"));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.quiz_option_4)).perform(scrollTo());
        onView(withId(R.id.quiz_option_4)).perform(typeText("D"));
        closeSoftKeyboard();
        sleep();
        onView(withText(R.string.quiz_add)).perform(click());

    }

    private void editQuestions() {
        String question = "Edit Quiz Question";

        onView(withId(R.id.template_editor_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));
        sleep();
        onView(withId(R.id.action_edit)).perform(click());
        onView(withId(R.id.quiz_question)).perform(replaceText(question));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.quiz_radio_1)).perform(scrollTo()).perform(click());
        closeSoftKeyboard();
        sleep();
        onView(withText(R.string.quiz_ok)).perform(click());

    }

    private void addTemplate() {

        onView(withId(R.id.author_name)).perform(replaceText("tejavojjala"));
        onView(withId(R.id.template_title)).perform(replaceText("Testing Quiz Template"));
    }

    private void checkSimulator() {
        onView(withId(R.id.action_simulate)).perform(click());
        onView(withText("Testing Quiz Template")).check(matches(isDisplayed()));
        onView(withText("tejavojjala")).check(matches(isDisplayed()));
        onView(withId(R.id.enter)).perform(click());
        String question = "Edit Quiz Question";

        onView(withId(R.id.question)).check(matches(withText(question))).check(matches(isDisplayed()));

        onView(withId(R.id.radioButton1)).perform(scrollTo()).perform(click());
        onView(withId(R.id.next)).perform(scrollTo()).perform(click());
        onView(withId(R.id.correct)).check(matches(withText("Total Correct : 1"))).check(matches(isDisplayed()));
        onView(withId(R.id.wrong)).check(matches(withText("Total Wrong : 0"))).check(matches(isDisplayed()));
        onView(withId(R.id.un_answered)).check(matches(withText("Total Unanswered : 0"))).check(matches(isDisplayed()));

        onView(withId(R.id.exit)).perform(click());

    }

    @Test
    public void testAllSerially() {
        allowPermissionsIfNeeded();
        toolbarTitle();
        addTemplate();
        addQuestions();
        editQuestions();
        checkSimulator();
        //    saveAPK();
    }

}
