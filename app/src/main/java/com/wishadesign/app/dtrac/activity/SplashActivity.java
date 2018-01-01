package com.wishadesign.app.dtrac.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wishadesign.app.dtrac.DTracApplication;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.ConnectivityReceiver;
import com.wishadesign.app.dtrac.util.SessionManager;

/**
 * Created by aksha on 12/9/2017.
 */

public class SplashActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private final int SPLASH_DISPLAY_LENGTH = 1000;

    private SessionManager mSessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                mSessionManager = SessionManager.getInstance(getBaseContext());
                if(mSessionManager.isLoggedIn()) {
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Intent mainIntent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
