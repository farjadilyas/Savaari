package com.farjad.savaari.utility;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;

import androidx.room.Room;

import com.farjad.savaari.R;
import com.farjad.savaari.services.location.LocationUpdateUtil;
import com.farjad.savaari.services.network.RequestController;
import com.farjad.savaari.services.persistence.Repository;
import com.farjad.savaari.services.persistence.AppDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Util {

    private static Util instance = null;
    private ExecutorService executorService;
    private Repository repository;
    private ScheduledThreadPoolExecutor scheduledExecutor;
    private int themeID = 4;

    /* Initialize network and local storage vars */
    private Util(Context context) {
        scheduledExecutor = new ScheduledThreadPoolExecutor(6);
        executorService = Executors.newFixedThreadPool(4);
        AppDatabase db = Room.databaseBuilder(context,
                AppDatabase.class, "database-name").build();

        repository = new Repository(executorService, RequestController.INSTANCE.getWebService(), db.userDao());
        LocationUpdateUtil.setRepository(repository);
    }

    // Called by first activity - init instance with context
    public static synchronized Util getInstance(Context context) {
        if (instance == null) {
            instance = new Util(context);
        }
        return instance;
    }

    /*
    * Instance should eb initialized at this point, else, let exception rise
    * TODO: Handle potential NullPointerException
    */
    public static synchronized Util getInstance() {
        return instance;
    }

    public Repository getRepository() {
        return repository;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public ScheduledThreadPoolExecutor getScheduledExecutor() {
        return scheduledExecutor;
    }

    public int getThemeID() {
        return themeID;
    }

    public void setThemeID(int themeID) {
        this.themeID = themeID;
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

    public void themeSelect(final Context context) {

        switch (themeID)
        {
            case(0):
                context.setTheme(R.style.BlackTheme);
                break;
            case(1):
                context.setTheme(R.style.DarkBlueTheme);
                break;
            case(2):
                context.setTheme(R.style.DimRedTheme);
                break;
            case(4):
                context.setTheme(R.style.RedTheme);
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
