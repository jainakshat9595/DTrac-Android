package com.wishadesign.app.dtrac;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.wishadesign.app.dtrac.util.ConnectivityReceiver;
import com.wishadesign.app.dtrac.util.TypefaceUtil;

import io.fabric.sdk.android.Fabric;

/**
 * Created by aksha on 12/15/2017.
 */

public class DTracApplication extends Application {

    private static DTracApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(new ConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)           // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
        TypefaceUtil.overrideFont(getApplicationContext(), "pt_sans_ttf", "pt_sans.ttf");
    }

    public static synchronized DTracApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
