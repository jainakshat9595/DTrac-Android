package com.wishadesign.app.dtrac.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.wishadesign.app.dtrac.DTracApplication;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.fragment.AgentFragment;
import com.wishadesign.app.dtrac.fragment.DashboardFragment;
import com.wishadesign.app.dtrac.fragment.FixedAssignmentFragment;
import com.wishadesign.app.dtrac.fragment.OrderFragment;
import com.wishadesign.app.dtrac.fragment.OutletFragment;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.ConnectivityReceiver;
import com.wishadesign.app.dtrac.util.CustomFragment;
import com.wishadesign.app.dtrac.util.SessionManager;
import com.wishadesign.app.dtrac.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private TextView mNavUsername;
    private TextView mNavEmail;
    private TextView mWalletBalance;
    private TextView mUserFullName;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private View mNavHeader;

    ProgressDialog mProgress;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private SessionManager mSessionManager;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private CustomFragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();

        mSessionManager = SessionManager.getInstance(getBaseContext());
        mSessionManager.checkLogin();

        getUserDetails();

        mNavUsername.setText(mSessionManager.getToken());

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        ft = fm.beginTransaction();

        if (id == R.id.nav_dashboard) {
            mCurrentFragment = DashboardFragment.newInstance();
        } else if (id == R.id.nav_agents) {
            mCurrentFragment = AgentFragment.newInstance();
        } else if (id == R.id.nav_all_outlets) {
            mCurrentFragment = OutletFragment.newInstance();
        } else if (id == R.id.nav_fixed_assignment) {
            mCurrentFragment = FixedAssignmentFragment.newInstance();
        } else if (id == R.id.nav_orders) {
            mCurrentFragment = OrderFragment.newInstance();
        } else if (id == R.id.nav_logout) {
            mSessionManager.logoutUser();
            finish();
        }

        ft.replace(R.id.main_container, mCurrentFragment);
        ft.commit();

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new TypefaceSpan("pt_sans.ttf"), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    private void initViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        for (int i = 0; i < mToolbar.getChildCount(); i++) {
            View view = mToolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "pt_sans.ttf");
                textView.setTypeface(myCustomFont);
            }
        }

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavHeader = mNavigationView.getHeaderView(0);

        Menu m = mNavigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }

        mNavUsername = (TextView) mNavHeader.findViewById(R.id.nav_username);
        mNavEmail = (TextView) mNavHeader.findViewById(R.id.nav_email);

        mWalletBalance = (TextView) findViewById(R.id.wallet_balance);
        mUserFullName = (TextView) findViewById(R.id.user_fullname);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        fm = getSupportFragmentManager();

        ft = fm.beginTransaction();
        mCurrentFragment = DashboardFragment.newInstance();
        ft.add(R.id.main_container, mCurrentFragment);
        ft.commit();

    }

    private void getUserDetails() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.USER_DETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONObject userData = resp.getJSONObject("data");
                    mNavUsername.setText(userData.getString("username"));
                    mNavEmail.setText(userData.getString("email"));
                    mUserFullName.setText(Util.toTitleCase(userData.getString("fname")+" "+userData.getString("lname")));
                    mWalletBalance.setText(userData.getString("total")+"/-");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("userId", mSessionManager.getToken());
                return param;
            }
        };

        mProgress.show();
        APIRequest.getInstance(getBaseContext()).addToRequestQueue(strRequest);
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
