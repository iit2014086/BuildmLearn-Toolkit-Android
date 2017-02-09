package org.buildmlearn.toolkit.espresso;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.test.annotation.UiThreadTest;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Espresso test designed to test all the functionalities of Comprehension template
 * Created by anupam (opticod) on 7/6/16.
 */
@Ignore
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MatchTemplateTest {

    @Rule
    public final ActivityTestRule<TemplateEditor> mActivityRule =
            new ActivityTestRule<TemplateEditor>(TemplateEditor.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = getInstrumentation()
                            .getTargetContext();
                    Intent result = new Intent(targetContext, TemplateEditor.class);
                    result.putExtra(Constants.TEMPLATE_ID, 7);
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
        String title = "Match Template";
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText(title)));
    }

    private void addMetaDetails() {
        String title = "Match Title";
        String first_list_title = "First List Title";
        String second_list_title = "Second List Title";

        onView(withId(R.id.button_add_item)).perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.meta_title)).perform(typeText(title));
        closeSoftKeyboard();
        onView(withId(R.id.meta_first_list_title)).perform(typeText(first_list_title), ViewActions.closeSoftKeyboard());
        sleep();
        onView(withId(R.id.meta_second_list_title)).perform(typeText(second_list_title), ViewActions.closeSoftKeyboard());;
        sleep();
        onView(withText("Add")).perform(click());

    }

    private void editMetaDetails() {
        String title = "Edited Match Title";
        String first_list_title = "Edited First List Title";
        String second_list_title = "Edited Second List Title";

        onView(withId(R.id.template_meta_listview)).perform(longClick());
        onView(withId(R.id.action_edit)).perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.meta_title)).perform(replaceText(title));
        closeSoftKeyboard();
        onView(withId(R.id.meta_first_list_title)).perform(replaceText(first_list_title), ViewActions.closeSoftKeyboard());
        sleep();
        onView(withId(R.id.meta_second_list_title)).perform(replaceText(second_list_title), ViewActions.closeSoftKeyboard());;
        sleep();
        onView(withText(R.string.quiz_ok)).perform(click());

    }

    private void addMatchItems() {

        onView(withId(R.id.button_add_item)).perform(click());
        onView(withId(R.id.first_list_item)).perform(typeText("a"));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.second_list_item)).perform(typeText("A"));
        closeSoftKeyboard();
        sleep();
        onView(withText("Add")).perform(click());
        sleep();

        onView(withId(R.id.button_add_item)).perform(click());
        onView(withId(R.id.first_list_item)).perform(typeText("b"));
        closeSoftKeyboard();
        sleep();
        onView(withId(R.id.second_list_item)).perform(typeText("B"));
        closeSoftKeyboard();
        sleep();
        onView(withText("Add")).perform(click());
        sleep();

    }

    private void editMatchItems() {

        onView(withId(R.id.template_editor_recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(1, longClick()));
        sleep();
        onView(withId(R.id.action_edit)).perform(click());
        sleep();
        onView(withId(R.id.first_list_item)).perform(replaceText("c"));
        closeSoftKeyboard();
        onView(withId(R.id.second_list_item)).perform(replaceText("C"));
        closeSoftKeyboard();
        onView(withText(R.string.quiz_ok)).perform(click());

    }

    private void addTemplate() {

        onView(withId(R.id.author_name)).perform(replaceText("tejavojjala"));
        onView(withId(R.id.template_title)).perform(replaceText("Testing Match template"));
    }

    private void saveAPK() {

        onView(withId(R.id.action_save)).perform(click());
        onView(withText("Save APK")).perform(click());
        //sleep(15000);

        onView(withText("install")).perform(click());
    }

    int num;
    int num1,num2,num3,num4;
    private int getPosition(final String required){

        num=0;
        onView(new Matcher<View>() {
            @Override
            public boolean matches(Object item) {
                boolean matches = withId(R.id.text).matches(item);
                if(matches){
                    num++;
                    View view = (View) item;
                    String matchedText = ((TextView)view).getText().toString();
                    if(matchedText.equals(required))
                    {
                        if(required.equals("c"))num1=num;
                        else if(required.equals("b"))num2=num;
                        else if(required.equals("C"))num3=num;
                        else if(required.equals("B"))num4=num;
                        Toast.makeText(mActivityRule.getActivity(),String.valueOf(num),Toast.LENGTH_LONG).show();
                        return true;
                    }
                    return false;
                }
                return false;
            }

            @Override
            public void describeMismatch(Object item, Description mismatchDescription) {

            }

            @Override
            public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

            }

            @Override
            public void describeTo(Description description) {

            }
        }).check(matches(isDisplayed()));
        return num;
    }

    private void checkSimulator() {
        onView(withId(R.id.action_simulate)).perform(click());
        onView(withText("Testing Match template")).check(matches(isDisplayed()));
        onView(withText("tejavojjala")).check(matches(isDisplayed()));
        onView(withId(R.id.enter)).perform(click());
        String title = "Edited Match Title";
        String first_list_title = "Edited First List Title";
        String second_list_title = "Edited Second List Title";

        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar_main)), withText(title)))
                .check(matches(isDisplayed()));
        onView(withId(R.id.first_list_title)).check(matches(withText(first_list_title))).check(matches(isDisplayed()));
        onView(withId(R.id.second_list_title)).check(matches(withText(second_list_title))).check(matches(isDisplayed()));

        num1=getPosition("c");
        num2=getPosition("b");
        num3=getPosition("C");
        num4=getPosition("B");
        sleep();

        if(!(((num1<num2)&&(num3<num4))||(((num1>num2)&&(num3>num4)))))
        {
            onData(anything()).inAdapterView(withId(R.id.list_view_match_A)).atPosition(1).perform(click());
            onView(withId(R.id.first_list_down)).perform(click());
        }

        onView(withId(R.id.check_answer)).perform(click());
        sleep();

        onView(withId(R.id.score)).check(matches(withText("Score : 2 of 2")));
        sleep();

        onView(withId(R.id.try_again)).perform(click());
    }

    @Test
    public void testAllSerially() {
        allowPermissionsIfNeeded();
        toolbarTitle();
        addTemplate();
        addMetaDetails();
        editMetaDetails();
        addMatchItems();
        editMatchItems();
        checkSimulator();
        //saveAPK();
    }
}