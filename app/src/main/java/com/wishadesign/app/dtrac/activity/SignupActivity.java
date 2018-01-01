package com.wishadesign.app.dtrac.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.wishadesign.app.dtrac.DTracApplication;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.ConnectivityReceiver;

public class SignupActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().startSync();
        CookieManager.getInstance().setAcceptCookie(true);

        WebView webContainer = (WebView) findViewById(R.id.signup_webview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webContainer, true);
        }

        webContainer.getSettings().setJavaScriptEnabled(true);
        webContainer.setWebChromeClient(new WebChromeClient());

        WebSettings settings = webContainer.getSettings();
        settings.setDomStorageEnabled(true);
        webContainer.getSettings().setAppCacheEnabled(true);
        webContainer.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webContainer.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                CookieSyncManager.getInstance().sync();
                String javascript="javascript: $(document).ready(function() { $('#open_reg_modal').click(); });";
                view.loadUrl(javascript);
            }
        });
        webContainer.loadUrl(Config.SIGNUP_CP_LINK);
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webContainer, true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().stopSync();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().sync();
        DTracApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        String message;
        if (isConnected) {
            message = "Connected to Internet";
        } else {
            APIRequest.getInstance(getBaseContext()).cancelAll();
            message = "Sorry! Not connected to internet";
        }

        Toast snackbar = Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG);

        snackbar.show();
    }

}

