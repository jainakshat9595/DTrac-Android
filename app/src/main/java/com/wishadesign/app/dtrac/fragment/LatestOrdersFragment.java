package com.wishadesign.app.dtrac.fragment;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.adapter.LatestOrdersAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.Order;
import com.wishadesign.app.dtrac.util.CustomFragment;

import java.util.ArrayList;

/**
 * Created by aksha on 12/19/2017.
 */

public class LatestOrdersFragment extends CustomFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mLatestOrdersRV;
    private LatestOrdersAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CustomFragment mParentDashboardFragment;
    private ArrayList<Agent> mAgentDataList;

    public LatestOrdersFragment() {
    }

    @SuppressLint("ValidFragment")
    public LatestOrdersFragment(CustomFragment DashboardFragment) {
        this.mParentDashboardFragment = DashboardFragment;
    }

    public static LatestOrdersFragment newInstance(CustomFragment DashboardFragment) {
        LatestOrdersFragment fragment = new LatestOrdersFragment(DashboardFragment);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_orders, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLatestOrdersRV = (RecyclerView) view.findViewById(R.id.latest_ten_orders);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLatestOrdersRV.setLayoutManager(mLayoutManager);
        mLatestOrdersRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LatestOrdersAdapter(getContext(), new ArrayList<Order>(), new ArrayList<Agent>(), this);
        mLatestOrdersRV.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setData(ArrayList<Order> mLatestOrdersList) {
        mAdapter.setData(mLatestOrdersList);
        mLatestOrdersRV.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
    }

    public void setAgentData(ArrayList<Agent> mAgentDataList) {
        this.mAgentDataList = mAgentDataList;
        mAdapter.setAgentData(mAgentDataList);
    }

    @Override
    public void onRefresh() {
        Log.d("DashboardFragment", "Refresh Called");
        mParentDashboardFragment.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void refresh() {
        mParentDashboardFragment.refresh();
    }
}
