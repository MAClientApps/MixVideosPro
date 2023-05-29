package com.marwadisachin78.mixvideospro.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.adjust.sdk.Adjust;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.marwadisachin78.mixvideospro.R;
import com.marwadisachin78.mixvideospro.utility.Utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SplashActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    long SPLASH_TIME = 0;
    long REF_TIMER = 10;
    long APP_TIMER = 16;
    ScheduledExecutorService mScheduledExecutorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        try {
            Adjust.getGoogleAdId(this, googleAdId -> {
                try {
                    Utils.setGPSADID(SplashActivity.this, googleAdId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
        runScheduledExecutorService();
    }

    public void initView() {
        if (!Utils.isNetworkAvailable(SplashActivity.this)) {
            checkInternetConnectionDialog(SplashActivity.this);
        } else {
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(1)
                    .build();
            mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
            mFirebaseRemoteConfig.reset();
            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCanceledListener(() -> {
                       /* try {
                            Log.e("mFirebaseRemoteConfig=","addOnCanceledListener");
                            gotoHome();
                        } catch (Exception e) {
                            gotoHome();
                        }*/
                    })
                    .addOnFailureListener(this, task -> {
                       /* try {
                            Log.e("mFirebaseRemoteConfig=","addOnFailureListener");
                            gotoHome();
                        } catch (Exception e) {
                            gotoHome();
                        }*/
                    })
                    .addOnCompleteListener(this, task -> {
                        try {
                            Log.e("mFirebaseRemoteConfig=","addOnCompleteListener");
                            if (!mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU)
                                    .equalsIgnoreCase("")) {
                                Log.e("EndPoint=", mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU));
                                if (mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU)
                                        .startsWith("http")) {
                                    Utils.setEndPointValue(SplashActivity.this,
                                            mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU));
                                } else {
                                    Utils.setEndPointValue(SplashActivity.this,
                                            "https://" + mFirebaseRemoteConfig.getString(Utils.PARAM_KEY_REMOTE_CONFIG_SUB_ENDU));
                                }
/*
                                if (!Utils.getEndPointValue(SplashActivity.this).isEmpty() ||
                                        !Utils.getEndPointValue(SplashActivity.this).equalsIgnoreCase("")) {
                                    Log.e("getEndPointValue =", Utils.getEndPointValue(SplashActivity.this));
                                    runScheduledExecutorService();
                                } else {
                                    gotoHome();
                                }*/
                            }/* else {
                                gotoHome();
                            }*/
                        } catch (Exception e) {
                            //gotoHome();
                        }
                    });
        }
    }

    public void gotoNext() {

        if (!Utils.getEndPointValue(SplashActivity.this).isEmpty() ||
                !Utils.getEndPointValue(SplashActivity.this).equalsIgnoreCase("")) {
            Log.e("getEndPointValue =", Utils.getEndPointValue(SplashActivity.this));
            startActivity(new Intent(SplashActivity.this, MixVideosProActivity.class));
            finish();
        } else {
            gotoHome();
        }

    }

    public void gotoHome() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }

    public void checkInternetConnectionDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.no_internet_connection);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.btn_try_again, (dialog, which) -> retryInternetConnection());
        builder.show();
    }

    private void retryInternetConnection() {
        new Handler(Looper.getMainLooper()).postDelayed(this::initView, 1000);
    }

    public void runScheduledExecutorService() {
        try {
            if (!Utils.isSecondOpen(SplashActivity.this)) {
                Utils.setSecondOpen(SplashActivity.this, true);
                mScheduledExecutorService = Executors.newScheduledThreadPool(5);
                mScheduledExecutorService.scheduleAtFixedRate(() -> {
                    SPLASH_TIME = SPLASH_TIME + 1;
                    Log.e("InternalFlow_timer", "InternalFlow_timer: " + SPLASH_TIME);
                    if (!Utils.getDeepLink(SplashActivity.this).isEmpty()) {
                        try {
                            mScheduledExecutorService.shutdown();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        gotoNext();
                    } else if (SPLASH_TIME >= REF_TIMER) {
                        if (!Utils.getReceivedAttribution(SplashActivity.this).isEmpty()) {

                            if (!Utils.getCampaign(SplashActivity.this).isEmpty()) {
                                try {
                                    mScheduledExecutorService.shutdown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                gotoNext();
                                return;
                            }
                            if (SPLASH_TIME >= APP_TIMER) {
                                try {
                                    mScheduledExecutorService.shutdown();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                gotoHome();
                            }
                        } else if (SPLASH_TIME >= APP_TIMER) {
                            try {
                                mScheduledExecutorService.shutdown();
                            } catch (Exception InternalFlow_exception) {
                                InternalFlow_exception.printStackTrace();
                            }
                            gotoHome();
                        }
                    }

                }, 0, 500, TimeUnit.MILLISECONDS);
            } else {
                if (!Utils.getDeepLink(SplashActivity.this).isEmpty()) {
                    gotoNext();
                    return;
                }
                if (!Utils.getReceivedAttribution(SplashActivity.this).isEmpty()
                        && !Utils.getCampaign(SplashActivity.this).isEmpty()) {
                    gotoNext();
                    return;
                }
                gotoHome();
            }
        } catch (Exception InternalFlow_exception) {
            gotoHome();
        }
    }
}