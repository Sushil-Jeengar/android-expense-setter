package com.sushil.Splid;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;

public class MyApplication extends Application {

    private static final int RATING_POPUP_DELAY = 10000; // 10 sec
    private static boolean ratingShown = false;
    private Activity currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle bundle) {}
            @Override public void onActivityStarted(Activity activity) {}
            @Override public void onActivityResumed(Activity activity) {
                currentActivity = activity;
            }
            @Override public void onActivityPaused(Activity activity) {
                if (currentActivity == activity) currentActivity = null;
            }
            @Override public void onActivityStopped(Activity activity) {}
            @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {}
            @Override public void onActivityDestroyed(Activity activity) {}
        });

        new Handler().postDelayed(() -> {
            if (!ratingShown && currentActivity != null) {
                DialogUtils.showRatingDialog(currentActivity);
                ratingShown = true;
            }
        }, RATING_POPUP_DELAY);
    }
}
