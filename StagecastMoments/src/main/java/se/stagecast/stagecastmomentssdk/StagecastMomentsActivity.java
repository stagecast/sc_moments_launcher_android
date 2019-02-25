package se.stagecast.stagecastmomentssdk;

import android.app.Activity;
import android.os.Bundle;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler;

public class StagecastMomentsActivity  extends Activity implements DefaultHardwareBackBtnHandler {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReactRootView = new ReactRootView(this);

        mReactInstanceManager = StagecastMomentsApi.getInstance().getReactInstanceManager();

        if(mReactInstanceManager == null)
            throw new IllegalStateException(getString(R.string.exception_react_instance_manager_null));

        Bundle bundle = new Bundle();

        String eventID = getIntent().getStringExtra(StagecastMomentsApi.EVENT_ID_KEY);

        if(eventID!=null && !"".equals(eventID))
            bundle.putString(StagecastMomentsApi.EVENT_ID_KEY,eventID);

        // The string here (e.g. "StagecastMoments") has to match
        // the string in AppRegistry.registerComponent() in index.js

        mReactRootView.startReactApplication(mReactInstanceManager, "StagecastMoments", bundle);

        setContentView(mReactRootView);

        StagecastMomentsApi.getInstance().setMyActivityWeakReference(this);
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostDestroy(this);
        }
/*        if (mReactRootView != null) {
            mReactRootView.unmountReactApplication();
        }*/
    }
}