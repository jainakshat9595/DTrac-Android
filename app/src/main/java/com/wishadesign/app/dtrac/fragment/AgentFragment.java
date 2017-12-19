package com.wishadesign.app.dtrac.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.adapter.AllAgentsAdapter;
import com.wishadesign.app.dtrac.adapter.LatestAgentsAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AgentFragment extends Fragment {

    private static AgentFragment instance;

    private SessionManager mSessionManager;

    private ArrayList<Agent> mAllAgentsList;

    private ProgressDialog mProgress;
    private RecyclerView mAllAgentRV;
    private AllAgentsAdapter mAdapter;

    public AgentFragment() {
    }

    public static Fragment newInstance() {
        if(instance == null) {
            instance = new AgentFragment();
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
        View view = inflater.inflate(R.layout.fragment_agents, container, false);

        mSessionManager = SessionManager.getInstance(getContext());

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        mAllAgentRV = (RecyclerView) view.findViewById(R.id.all_agent_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAllAgentRV.setLayoutManager(mLayoutManager);
        mAllAgentRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AllAgentsAdapter(getContext(), new ArrayList<Agent>());
        mAllAgentRV.setAdapter(mAdapter);

        mAllAgentsList = new ArrayList<Agent>();

        getAllAgents();

        return view;
    }


    private void getAllAgents() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.GET_AGENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray agents_array = resp.getJSONArray("agentList");
                    mAllAgentsList.clear();
                    for (int i = 0; i < agents_array.length(); i++) {
                        mAllAgentsList.add(Agent.parse((JSONObject) agents_array.get(i)));
                    }
                    Log.d("AgentFragment", response);
                    mAdapter.setData(mAllAgentsList);
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Log.d("AgentFragment", error.getMessage());
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
        APIRequest.getInstance(getContext()).addToRequestQueue(strRequest);
    }

}