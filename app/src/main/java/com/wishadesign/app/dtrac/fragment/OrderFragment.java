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
import com.wishadesign.app.dtrac.adapter.AllOrderAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.Order;
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

public class OrderFragment extends Fragment {

    private static OrderFragment instance;

    private SessionManager mSessionManager;

    private ArrayList<Order> mAllOrdersList;
    private ArrayList<Agent> mAllOrdersAgentList;

    private ProgressDialog mProgress;
    private RecyclerView mAllOrderRV;
    private AllOrderAdapter mAdapter;

    private SearchView mSearchView;

    private ArrayList<Order> mFilteredDataList;

    public OrderFragment() {
    }

    public static Fragment newInstance() {
        if(instance == null) {
            instance = new OrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        mSessionManager = SessionManager.getInstance(getContext());

        mSearchView = (SearchView) view.findViewById(R.id.all_order_searchview);
        mSearchView.setIconified(false);

        mSearchView.clearFocus();

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()) {
                    mFilteredDataList = mAllOrdersList;
                } else {
                    ArrayList<Order> filteredList = new ArrayList<>();
                    for (Order order: mAllOrdersList) {
                        if (order.getStatus().toLowerCase().contains(query.toLowerCase()) || order.getOutletName().toLowerCase().contains(query.toLowerCase()) || order.getAgentName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(order);
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
                    mFilteredDataList = mAllOrdersList;
                } else {
                    ArrayList<Order> filteredList = new ArrayList<>();
                    for (Order order: mAllOrdersList) {
                        if (order.getStatus().toLowerCase().contains(query.toLowerCase()) || order.getOutletName().toLowerCase().contains(query.toLowerCase()) || order.getAgentName().toLowerCase().contains(query.toLowerCase())) {
                            filteredList.add(order);
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

        mAllOrderRV = (RecyclerView) view.findViewById(R.id.all_order_rv);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mAllOrderRV.setLayoutManager(mLayoutManager);
        mAllOrderRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AllOrderAdapter(OrderFragment.this, getContext(), new ArrayList<Order>(), new ArrayList<Agent>());
        mAllOrderRV.setAdapter(mAdapter);

        mAllOrdersList = new ArrayList<Order>();
        mAllOrdersAgentList = new ArrayList<Agent>();

        getAllOrders();

        return view;
    }

    public void getAllOrders() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.GET_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray outlets_array = resp.getJSONArray("orders");
                    mAllOrdersList.clear();
                    for (int i = 0; i < outlets_array.length(); i++) {
                        Order order = Order.parse((JSONObject) outlets_array.get(i));
                        mAllOrdersList.add(order);
                    }
                    mAdapter.setData(mAllOrdersList);
                    mAdapter.notifyDataSetChanged();
                    mProgress.show();
                    getAllAgents();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Log.d("OrderFragment", error.getMessage());
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

    private void getAllAgents() {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.GET_AGENT_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    JSONArray agents_array = resp.getJSONArray("agentList");
                    mAllOrdersAgentList.clear();
                    for (int i = 0; i < agents_array.length(); i++) {
                        Agent agent = Agent.parse((JSONObject) agents_array.get(i));
                        if(agent.getUserStatus().equals("Active")) {
                            if(!mAllOrdersAgentList.contains(agent) && agent!=null) {
                                mAllOrdersAgentList.add(agent);
                            }
                        }
                    }
                    Log.d("OrderFragment", response);
                    mAdapter.setAgentData(mAllOrdersAgentList);
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
