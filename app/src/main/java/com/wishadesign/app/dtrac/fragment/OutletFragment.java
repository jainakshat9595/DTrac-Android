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
import com.wishadesign.app.dtrac.adapter.AllAgentsAdapter;
import com.wishadesign.app.dtrac.adapter.AllOutletsAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.Outlet;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;
import com.wishadesign.app.dtrac.util.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OutletFragment extends Fragment {

    private static OutletFragment instance;

    private SessionManager mSessionManager;

    private ArrayList<Outlet> mAllOutletsList;

    private ProgressDialog mProgress;
    private RecyclerView mAllOutletRV;
    private AllOutletsAdapter mAdapter;

    private SearchView mSearchView;

    private ArrayList<Outlet> mFilteredDataList;

    public OutletFragment() {
    }

    public static Fragment newInstance() {
        if(instance == null) {
            instance = new OutletFragment();
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
        View view = inflater.inflate(R.layout.fragment_outlets, container, false);

        mSessionManager = SessionManager.getInstance(getContext());

        mSearchView = (SearchView) view.findViewById(R.id.all_outlet_searchview);
        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    mFilteredDataList = mAllOutletsList;
                } else {
                    ArrayList<Outlet> filteredList = new ArrayList<>();
                    for (Outlet outlet: mAllOutletsList) {
                        if (outlet.getCity().toLowerCase().contains(query.toLowerCase()) || outlet.getOutletName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(outlet);
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
                    mFilteredDataList = mAllOutletsList;
                } else {
                    ArrayList<Outlet> filteredList = new ArrayList<>();
                    for (Outlet outlet: mAllOutletsList) {
                        if (outlet.getCity().toLowerCase().contains(query.toLowerCase()) || outlet.getOutletName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(outlet);
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

        mAllOutletRV = (RecyclerView) view.findViewById(R.id.all_outlet_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAllOutletRV.setLayoutManager(mLayoutManager);
        mAllOutletRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AllOutletsAdapter(getContext(), new ArrayList<Outlet>());
        mAllOutletRV.setAdapter(mAdapter);

        mAllOutletsList = new ArrayList<Outlet>();

        getAllOutlets();

        return view;
    }

    private void getAllOutlets() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.GET_OUTLET_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray outlets_array = resp.getJSONArray("outletList");
                    mAllOutletsList.clear();
                    for (int i = 0; i < outlets_array.length(); i++) {
                        mAllOutletsList.add(Outlet.parse((JSONObject) outlets_array.get(i)));
                    }
                    mAdapter.setData(mAllOutletsList);
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
                        Log.d("OutletFragment", error.getMessage());
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
