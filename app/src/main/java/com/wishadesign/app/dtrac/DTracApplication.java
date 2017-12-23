package com.wishadesign.app.dtrac;

import android.app.Application;

import com.wishadesign.app.dtrac.util.TypefaceUtil;

/**
 * Created by aksha on 12/15/2017.
 */

public class DTracApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(getApplicationContext(), "pt_sans_ttf", "pt_sans.ttf");
    }
}
