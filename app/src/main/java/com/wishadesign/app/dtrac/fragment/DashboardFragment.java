package com.wishadesign.app.dtrac.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.adapter.DashboardPagerAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.FreelancerRequest;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.CustomFragment;
import com.wishadesign.app.dtrac.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends CustomFragment {

    private static DashboardFragment instance;

    private SessionManager mSessionManager;

    private ArrayList<FreelancerRequest> mLatestFreelancerRequestList;
    private ArrayList<Agent> mLatestAgentsList;

    private ProgressDialog mProgress;

    private ViewPager mDashboardPager;
    private TabLayout mDashboardTabs;
    private DashboardPagerAdapter mDashboardPagerAdapter;

    private LatestFreelancerRequestFragment mLatestFreelancerRequestFragment;
    private LatestAgentsFragment mLatestAgentsFragment;

    public DashboardFragment() {
    }

    public static CustomFragment newInstance() {
        if(instance == null) {
            instance = new DashboardFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mSessionManager = SessionManager.getInstance(getContext());

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        mDashboardPager = (ViewPager) view.findViewById(R.id.dashboard_pager);
        mDashboardTabs = (TabLayout) view.findViewById(R.id.dashboard_tabs);
        mDashboardPager.setOffscreenPageLimit(3);
        mDashboardTabs.setupWithViewPager(mDashboardPager);

        mDashboardPagerAdapter = new DashboardPagerAdapter(getChildFragmentManager());

        mLatestFreelancerRequestFragment = LatestFreelancerRequestFragment.newInstance();
        mLatestAgentsFragment = LatestAgentsFragment.newInstance();

        mDashboardPagerAdapter.addFragment(LatestOrdersFragment.newInstance(),"Latest Orders");
        mDashboardPagerAdapter.addFragment(mLatestFreelancerRequestFragment,"Latest Freelancers Request");
        mDashboardPagerAdapter.addFragment(mLatestAgentsFragment,"Latest Agents");
        mDashboardPager.setAdapter(mDashboardPagerAdapter);

        mLatestFreelancerRequestList = new ArrayList<FreelancerRequest>();
        mLatestAgentsList = new ArrayList<Agent>();

        getLatestFreelancerRequest();
        getLatestAgents();

        return view;
    }

    private void getLatestFreelancerRequest() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.PROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray request_array = resp.getJSONArray("ff_requests");
                    mLatestAgentsList.clear();
                    for (int i = 0; i < request_array.length(); i++) {
                        mLatestFreelancerRequestList.add(FreelancerRequest.parse((JSONObject) request_array.get(i)));
                    }
                    mLatestFreelancerRequestFragment.setData(mLatestFreelancerRequestList);
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
                param.put("fire", "get_ff_requests");
                param.put("partnerId", mSessionManager.getToken());
                return param;
            }
        };

        mProgress.show();
        APIRequest.getInstance(getContext()).addToRequestQueue(strRequest);
    }

    private void getLatestAgents() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.PROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray agents_array = resp.getJSONArray("agents");
                    mLatestAgentsList.clear();
                    for (int i = 0; i < agents_array.length(); i++) {
                        mLatestAgentsList.add(Agent.parse((JSONObject) agents_array.get(i)));
                    }
                    mLatestAgentsFragment.setData(mLatestAgentsList);
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
                param.put("fire", "get_agent_list");
                param.put("partnerId", mSessionManager.getToken());
                return param;
            }
        };

        mProgress.show();
        APIRequest.getInstance(getContext()).addToRequestQueue(strRequest);
    }

    @Override
    public void refresh() {
        super.refresh();
        getLatestFreelancerRequest();
        getLatestAgents();
    }
}
