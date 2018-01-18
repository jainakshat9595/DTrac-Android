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
import com.wishadesign.app.dtrac.adapter.LatestAgentsAdapter;
import com.wishadesign.app.dtrac.adapter.LatestFreelancerRequestAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.FreelancerRequest;
import com.wishadesign.app.dtrac.util.CustomFragment;

import java.util.ArrayList;

/**
 * Created by aksha on 12/19/2017.
 */

public class LatestFreelancerRequestFragment extends CustomFragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mLatestFreelancerRequestRV;
    private LatestFreelancerRequestAdapter mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CustomFragment mParentDashboardFragment;

    public LatestFreelancerRequestFragment() {
    }

    @SuppressLint("ValidFragment")
    public LatestFreelancerRequestFragment(CustomFragment DashboardFragment) {
        this.mParentDashboardFragment = DashboardFragment;
    }

    public static LatestFreelancerRequestFragment newInstance(CustomFragment DashboardFragment) {
        LatestFreelancerRequestFragment fragment = new LatestFreelancerRequestFragment(DashboardFragment);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_freelancer_request, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mLatestFreelancerRequestRV = (RecyclerView) view.findViewById(R.id.latest_ten_freelancer_request);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLatestFreelancerRequestRV.setLayoutManager(mLayoutManager);
        mLatestFreelancerRequestRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new LatestFreelancerRequestAdapter(getContext(), new ArrayList<FreelancerRequest>());
        mLatestFreelancerRequestRV.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setData(ArrayList<FreelancerRequest> mLatestAgentsList) {
        mAdapter.setData(mLatestAgentsList);
        mLatestFreelancerRequestRV.getRecycledViewPool().clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        Log.d("LatestFrelancerFragment", "Refresh Called");
        mParentDashboardFragment.refresh();
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
