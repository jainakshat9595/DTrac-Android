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
import com.wishadesign.app.dtrac.adapter.AllAgentsAdapter;
import com.wishadesign.app.dtrac.adapter.LatestAgentsAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.util.CustomFragment;

import java.util.ArrayList;

/**
 * Created by aksha on 12/19/2017.
 */

public class LatestAgentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mLatestAgentsRV;
    private LatestAgentsAdapter mAdapter;

    private CustomFragment mParentDashboardFragment;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public LatestAgentsFragment() {
    }

    @SuppressLint("ValidFragment")
    public LatestAgentsFragment(CustomFragment DashboardFragment) {
        this.mParentDashboardFragment = DashboardFragment;
    }

    public static LatestAgentsFragment newInstance(CustomFragment DashboardFragment) {
        LatestAgentsFragment fragment = new LatestAgentsFragment(DashboardFragment);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_agents, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLatestAgentsRV = (RecyclerView) view.findViewById(R.id.latest_ten_agents);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLatestAgentsRV.setLayoutManager(mLayoutManager);
        mLatestAgentsRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LatestAgentsAdapter(getContext(), new ArrayList<Agent>());
        mLatestAgentsRV.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setData(ArrayList<Agent> mLatestAgentsList) {
        mAdapter.setData(mLatestAgentsList);
        mLatestAgentsRV.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Log.d("LatestAgentsFragment", "Refresh Called");
        mParentDashboardFragment.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
