package org.buildmlearn.toolkit.espresso;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.buildmlearn.toolkit.R;
import org.buildmlearn.toolkit.activity.TemplateEditor;
import org.buildmlearn.toolkit.constant.Constants;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
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
public class FlashCardsTest {

    private Instrumentation.ActivityResult mActivityResult;

    @Rule
    public final ActivityTestRule<TemplateEditor> mActivityRule =
            new ActivityTestRule<TemplateEditor>(TemplateEditor.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, TemplateEditor.class);
                    result.putExtra(Constants.TEMPLATE_ID, 3);
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
        setupImageUri();
    }

    private void toolbarTitle() {
        String title = "Flash Template";
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(title)));
    }

    public void setupImageUri() {

        Resources resources = InstrumentationRegistry.getTargetContext().getResources();
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources
                .getResourcePackageName(
                        R.mipmap.ic_launcher) + '/' + resources.getResourceTypeName(
                R.mipmap.ic_launcher) + '/' + resources.getResourceEntryName(
                R.mipmap.ic_launcher));
        Intent resultData = new Intent();
        resultData.setData(imageUri);
        mActivityResult = new Instrumentation.ActivityResult(
                Activity.RESULT_OK, resultData);

        Log.d("tejavojjala","setupimageuriended");
    }

    public static BoundedMatcher<View, ImageView> hasDrawable() {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }

            @Override
            public boolean matchesSafely(ImageView imageView) {
                return imageView.getDrawable() != null;
            }
        };
    }

    private void addFlashCards() {
        String question = "Logo name?";
        String answer = "BuildmLearn";

        onView(withId(R.id.button_add_item)).perform(click());
        sleep();

        Intents.init();
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        Intent[] extraIntents = {galleryIntent};
        Matcher<Intent> expectedIntent = hasAction(Intent.ACTION_CHOOSER);
        intending(expectedIntent).respondWith(mActivityResult);

        onView(withId(R.id.flash_upload_image)).perform(click());
        intended(expectedIntent);
        Intents.release();

        onView(withId(R.id.banner_image)).check(matches(hasDrawable()));
        sleep();

        onView(withId(R.id.flash_question)).perform(typeText(question));
        closeSoftKeyboard();
        sleep();

        onView(withId(R.id.flash_answer)).perform(scrollTo());
        onView(withId(R.id.flash_answer)).perform(typeText(answer));
        closeSoftKeyboard();
        sleep();

        onView(withText(R.string.quiz_add)).perform(click());

    }

    private void editFlashCards() {
        String hint = "B__________";

        onView(withId(R.id.template_editor_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));
        sleep();

        onView(withId(R.id.action_edit)).perform(click());
        onView(withId(R.id.flash_hint)).perform(scrollTo()).perform(typeText(hint));
        closeSoftKeyboard();
        sleep();

        onView(withText(R.string.quiz_ok)).perform(click());

    }

    private void addTemplate() {

        onView(withId(R.id.author_name)).perform(replaceText("tejavojjala"));
        onView(withId(R.id.template_title)).perform(replaceText("Testing Flash Template"));
    }

    private void checkSimulator() {
        String question = "Logo name?";
        String answer = "BuildmLearn";
        String hint = "B__________";

        onView(withId(R.id.action_simulate)).perform(click());
        sleep();

        onView(withText("Testing Flash Template")).check(matches(isDisplayed()));
        sleep();

        onView(withText("tejavojjala")).check(matches(isDisplayed()));
        onView(withId(R.id.enter)).perform(click());
        sleep();

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar)), withText(R.string.main_title_flash)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.intro_number)).check(matches(withText("Card #1 of 1"))).check(matches(isDisplayed()));
        onView(withId(R.id.question)).check(matches(withText(question))).check(matches(isDisplayed()));
        onView(withId(R.id.hint)).perform(scrollTo()).check(matches(withText("Hint : "+ hint))).check(matches(isDisplayed()));
        onView(withId(R.id.flip)).perform(scrollTo()).perform(click());
        onView(withId(R.id.answertext)).check(matches(withText(answer))).check(matches(isDisplayed()));
        onView(withId(R.id.next)).perform(scrollTo()).perform(click());
        sleep();

        onView(withId(R.id.exit)).perform(click());
    }

    @Test
    public void testAllSerially() {
        allowPermissionsIfNeeded();
        toolbarTitle();
        addTemplate();
        addFlashCards();
        editFlashCards();
        checkSimulator();
        //    saveAPK();
    }

}
