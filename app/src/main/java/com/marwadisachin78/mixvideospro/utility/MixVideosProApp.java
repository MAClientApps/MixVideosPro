package com.marwadisachin78.mixvideospro.utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

public class MixVideosProApp extends Application {

    Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            builder.detectFileUriExposure();
            StrictMode.setVmPolicy(builder.build());
        }

        try {
            mContext = this;
        } catch (Exception e) {
            e.printStackTrace();
        }

        AdjustConfig adjustConfig = new AdjustConfig(this, Utils.ADJUST_TOKEN_VALUE,
                AdjustConfig.ENVIRONMENT_PRODUCTION);
        Adjust.addSessionCallbackParameter(Utils.PREF_KEY_USER_UUID,
                Utils.generateClickID(getApplicationContext()));

        adjustConfig.setOnAttributionChangedListener(attribution -> {
            Log.e("App", "attribution: " + attribution.toString());
            Utils.setReceivedAttribution(getApplicationContext(),
                    attribution.toString());
            Utils.setCampaign(mContext, attribution.campaign);
        });

        adjustConfig.setOnDeeplinkResponseListener(deeplink -> {
            Log.e("App", "deeplink: " + deeplink.toString());
            Utils.setDeepLink(getApplicationContext(), deeplink.toString());
            return false;
        });
        try {
            FirebaseAnalytics.getInstance(mContext)
                    .getAppInstanceId()
                    .addOnCompleteListener(task -> {
                        AdjustEvent adjustEvent_InternalFlow = new AdjustEvent(Utils.ADJUST_FIREBASE_INSTANCE_ID);
                        adjustEvent_InternalFlow.addCallbackParameter(Utils.PARAM_KEY_EVENT_VALUE,
                                task.getResult());
                        adjustEvent_InternalFlow.addCallbackParameter(Utils.PREF_KEY_USER_UUID,
                                Utils.generateClickID(getApplicationContext()));
                        Adjust.addSessionCallbackParameter(Utils.PARAM_KEY_FIREBASE_INSTANCE_ID,
                                task.getResult());
                        Adjust.trackEvent(adjustEvent_InternalFlow);
                        Adjust.sendFirstPackages();
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        adjustConfig.setDelayStart(1);
        Adjust.onCreate(adjustConfig);
        registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    private static final class AdjustLifecycleCallbacks implements ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }

}
