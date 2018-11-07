package si.um.feri.guessemotion.util;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import si.um.feri.guessemotion.R;

/**
 * Created by:
 *
 * Diana Sellárová,
 * Marek Urban,
 * Stanislav Blaško
 *
 * as assignment for Mobile communication services subject on Erasmus+ study.
 */

public class AndroidUtils {

    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }

    public static int getRandomNumber(int min, int max) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // nextInt is normally exclusive of the top value - so keep it like this
            return ThreadLocalRandom.current().nextInt(min, max);
        } else {
            Random rand = new Random();
            // nextInt is normally exclusive of the top value - so keep it like this
            return rand.nextInt((max - min)) + min;
        }
    }

    private static void hideKeyboard(Activity activity) {
        if (activity == null) return;
        View view = activity.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyboardOnTouchOutside(View view, final Activity activity) {
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(activity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideKeyboardOnTouchOutside(innerView, activity);
            }
        }
    }

    public static int getCorrectDrawableByPosition(int position) {
        switch (position) {
            case 0:
                return R.drawable.mood_joy_correct;
            case 1:
                return R.drawable.mood_anger_correct;
            case 2:
                return R.drawable.mood_neutral_fast_correct;
            case 3:
                return R.drawable.mood_neutral_slow_correct;
            case 4:
                return R.drawable.mood_sadness_correct;
            case 5:
                return R.drawable.mood_surprise_correct;
            case 6:
                return R.drawable.mood_disgust_correct;
            case 7:
                return R.drawable.mood_fear_correct;
            default:
                return -1;
        }
    }

    public static int getWrongDrawableByPosition(int position) {
        switch (position) {
            case 0:
                return R.drawable.mood_joy_wrong;
            case 1:
                return R.drawable.mood_anger_wrong;
            case 2:
                return R.drawable.mood_neutral_fast_wrong;
            case 3:
                return R.drawable.mood_neutral_slow_wrong;
            case 4:
                return R.drawable.mood_sadness_wrong;
            case 5:
                return R.drawable.mood_surprise_wrong;
            case 6:
                return R.drawable.mood_disgust_wrong;
            case 7:
                return R.drawable.mood_fear_wrong;
            default:
                return -1;
        }
    }

    public static int getDrawableByPosition(int position) {
        switch (position) {
            case 0:
                return R.drawable.mood_joy;
            case 1:
                return R.drawable.mood_anger;
            case 2:
                return R.drawable.mood_neutral_fast;
            case 3:
                return R.drawable.mood_neutral_slow;
            case 4:
                return R.drawable.mood_sadness;
            case 5:
                return R.drawable.mood_surprise;
            case 6:
                return R.drawable.mood_disgust;
            case 7:
                return R.drawable.mood_fear;
            default:
                return -1;
        }
    }
}
