package com.wishadesign.app.dtrac.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.adapter.AllFixedAssignmentAdapter;
import com.wishadesign.app.dtrac.adapter.AllOutletsAdapter;
import com.wishadesign.app.dtrac.model.FixedAssignment;
import com.wishadesign.app.dtrac.model.Outlet;
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

public class FixedAssignmentFragment extends CustomFragment {

    private static FixedAssignmentFragment instance;

    private SessionManager mSessionManager;

    private ArrayList<FixedAssignment> mAllFixedAssignmentList;

    private ProgressDialog mProgress;
    private RecyclerView mAllFixedAssignmentRV;
    private AllFixedAssignmentAdapter mAdapter;

    private SearchView mSearchView;

    private ArrayList<FixedAssignment> mFilteredDataList;

    public FixedAssignmentFragment() {
    }

    public static CustomFragment newInstance() {
        if(instance == null) {
            instance = new FixedAssignmentFragment();
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
        View view = inflater.inflate(R.layout.fragment_fixed_assignments, container, false);

        mSessionManager = SessionManager.getInstance(getContext());

        mSearchView = (SearchView) view.findViewById(R.id.all_fixed_assignment_searchview);
        mSearchView.setIconified(false);

        mSearchView.clearFocus();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    mFilteredDataList = mAllFixedAssignmentList;
                } else {
                    ArrayList<FixedAssignment> filteredList = new ArrayList<>();
                    for (FixedAssignment request: mAllFixedAssignmentList) {
                        if (request.getBranch_name().toLowerCase().contains(query.toLowerCase()) || request.getAgentName().toLowerCase().contains(query.toLowerCase()) || request.getStatus().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(request);
                        }
                    }
                    mFilteredDataList = filteredList;
                }

                mAdapter.setData(mFilteredDataList);
                mAdapter.notifyDataSetChanged();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()) {
                    mFilteredDataList = mAllFixedAssignmentList;
                } else {
                    ArrayList<FixedAssignment> filteredList = new ArrayList<>();
                    for (FixedAssignment request: mAllFixedAssignmentList) {
                        if (request.getBranch_name().toLowerCase().contains(query.toLowerCase()) || request.getAgentName().toLowerCase().contains(query.toLowerCase()) || request.getStatus().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(request);
                        }
                    }
                    mFilteredDataList = filteredList;
                }

                mAdapter.setData(mFilteredDataList);
                mAdapter.notifyDataSetChanged();

                return true;
            }
        });

        mProgress = new ProgressDialog(getContext());
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        mAllFixedAssignmentRV = (RecyclerView) view.findViewById(R.id.all_fixed_assignment_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAllFixedAssignmentRV.setLayoutManager(mLayoutManager);
        mAllFixedAssignmentRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AllFixedAssignmentAdapter(getContext(), new ArrayList<FixedAssignment>());
        mAllFixedAssignmentRV.setAdapter(mAdapter);

        mAllFixedAssignmentList = new ArrayList<FixedAssignment>();

        getAllFixedAssignments();

        return view;
    }

    private void getAllFixedAssignments() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.GET_FIXED_ASSIGNMENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray fixed_assignment_array = resp.getJSONArray("assignments");
                    mAllFixedAssignmentList.clear();
                    for (int i = 0; i < fixed_assignment_array.length(); i++) {
                        mAllFixedAssignmentList.add(FixedAssignment.parse((JSONObject) fixed_assignment_array.get(i)));
                    }
                    mAdapter.setData(mAllFixedAssignmentList);
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
        APIRequest.getInstance(getContext()).addToRequestQueue(strRequest);
    }

    @Override
    public void refresh() {
        super.refresh();
        getAllFixedAssignments();
    }
}
