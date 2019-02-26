package se.stagecast.stagecastmomentssdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.react.ReactInstanceManager;

import java.lang.ref.WeakReference;

public class StagecastMomentsApi {
    private static StagecastMomentsApi instance;
    private WeakReference<Activity> activityReference; // do not leak memory
    private Bundle bundleToPassToReact;

    private StagecastMomentsApi() {
    }

    public static StagecastMomentsApi getInstance() {
        if (instance == null) {
            instance = new StagecastMomentsApi();
        }
        return instance;
    }

    public static final String EVENT_ID_PROP = "event";

    public void setBundleToPassToReact(Bundle bundleToPassToReact) {
        this.bundleToPassToReact = bundleToPassToReact;
    }

    public Bundle getBundleToPassToReact() {
        return bundleToPassToReact;
    }

    public ReactInstanceManager getReactInstanceManager() {
        return reactInstanceManager;
    }

    public void setReactInstanceManager(ReactInstanceManager reactInstanceManager) {
        this.reactInstanceManager = reactInstanceManager;
    }

    private ReactInstanceManager reactInstanceManager;


    public void startReactApp(Activity activity, @NonNull ReactInstanceManager reactInstanceManager, Bundle bundle) {

        setReactInstanceManager(reactInstanceManager);

        if (activity == null) {
            Log.e("MySDK", "activity can't be null. returning.");
            return;
        }
        Intent intent = new Intent(activity, StagecastMomentsActivity.class);
        this.bundleToPassToReact = bundle;
        activity.startActivity(intent);

        // Send Events to React Native after 5 seconds of starting React app.
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // Send event to react native to enabled back button.
                        ReactBridge.sendEnableBackButton();
                    }
                },
                5000);
    }

    void dismissReactActivity() {
        if (this.activityReference.get() != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    activityReference.get().onBackPressed();
                }
            });
        }
    }

    void setMyActivityWeakReference(Activity myActivityReference) {
        this.activityReference = new WeakReference<>(myActivityReference);
    }
}

