package com.marwadisachin78.mixvideospro.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.marwadisachin78.mixvideospro.R;

import java.net.InetAddress;

public class VideosPlayActivity extends AppCompatActivity {

    private String strVideoLink = null;
    LinearLayout layoutInternetConnection;
    Button btnTryAgain;
    private WebView wvVideoPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos_play);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                strVideoLink = null;
            } else {
                strVideoLink = extras.getString("link");
            }
        } else {
            strVideoLink = (String) savedInstanceState.getSerializable("link");
        }

        initView();
    }

    public void initView() {
        btnTryAgain = findViewById(R.id.btnTryAgain);
        layoutInternetConnection = findViewById(R.id.layoutInternetConnection);
        wvVideoPlay = findViewById(R.id.wvVideoPlay);
        btnTryAgain.setOnClickListener(v -> new CheckInternetConnection().execute());
        new CheckInternetConnection().execute();
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    public class CheckInternetConnection extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("EqualsBetweenInconvertibleTypes")
        @SuppressLint("MissingPermission")
        @Override
        protected Boolean doInBackground(Void... voids) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                try {
                    InetAddress inetAddress = InetAddress.getByName("google.com");
                    return !inetAddress.equals("");
                } catch (Exception e) {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean value) {
            super.onPostExecute(value);
            if (value) {
                wvVideoPlay.setVisibility(View.VISIBLE);
                layoutInternetConnection.setVisibility(View.GONE);
                loadVideoView();
            } else {
                wvVideoPlay.setVisibility(View.GONE);
                layoutInternetConnection.setVisibility(View.VISIBLE);
            }

        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void loadVideoView() {

        CookieManager.getInstance().setAcceptCookie(true);
        wvVideoPlay.getSettings().setJavaScriptEnabled(true);
        wvVideoPlay.getSettings().setUseWideViewPort(true);
        wvVideoPlay.getSettings().setLoadWithOverviewMode(true);
        wvVideoPlay.getSettings().setDomStorageEnabled(true);
        wvVideoPlay.getSettings().setPluginState(WebSettings.PluginState.ON);
        wvVideoPlay.setWebChromeClient(new WebChromeClient());

        wvVideoPlay.setVisibility(View.VISIBLE);


        wvVideoPlay.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {


                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        wvVideoPlay.loadUrl(strVideoLink);

    }

    @Override
    public void onResume() {
        super.onResume();
        wvVideoPlay.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        wvVideoPlay.onPause();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (wvVideoPlay.canGoBack()) {
                    wvVideoPlay.goBack();
                } else {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        wvVideoPlay.loadUrl("about:blank");
    }
}