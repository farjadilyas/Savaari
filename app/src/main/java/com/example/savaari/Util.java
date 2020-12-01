package com.example.savaari;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

public class Util extends AppCompatActivity {

    public Util() {
    }

    public static void hideKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();

        //If no view currently has focus, create a view to grab a window token from it
        if (view == null)
            view = new View(activity);

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Animation inFromRightAnimation(int duration) {
        Animation inFromRight = new TranslateAnimation( Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(duration);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToRightAnimation(int duration) {
        Animation outToRight = new TranslateAnimation( Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, +1.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        outToRight.setDuration(duration);
        outToRight.setInterpolator(new AccelerateInterpolator());
        return outToRight;
    }

    public static Animation inFromBottomAnimation(int duration) {
        Animation inFromBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromBottom.setDuration(duration);
        inFromBottom.setInterpolator(new AccelerateInterpolator());
        return inFromBottom;
    }

    public static Animation outToBottomAnimation(int duration) {
        Animation outToBottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
        outToBottom.setDuration(duration);
        outToBottom.setInterpolator(new AccelerateInterpolator());
        return outToBottom;
    }

    public static void themeSelect(final Context context) {

        switch (ThemeVar.getData())
        {
            case(0):
                context.setTheme(R.style.BlackTheme);
                break;
            case(1):
                context.setTheme(R.style.RedTheme);
                break;
            case(2):
                context.setTheme(R.style.DimRedTheme);
                break;
            case(4):
                context.setTheme(R.style.DarkBlueTheme);
                break;
            case(5):
                context.setTheme(R.style.TodoTheme);
                break;
            default:
                context.setTheme(R.style.BlueTheme);
                break;
        }
    }
}
