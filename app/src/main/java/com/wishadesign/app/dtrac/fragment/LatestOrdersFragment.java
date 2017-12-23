package com.wishadesign.app.dtrac.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.adapter.LatestAgentsAdapter;
import com.wishadesign.app.dtrac.model.Agent;

import java.util.ArrayList;

/**
 * Created by aksha on 12/19/2017.
 */

public class LatestOrdersFragment extends Fragment {

    private RecyclerView mLatestAgentsRV;
    private LatestAgentsAdapter mAdapter;

    public LatestOrdersFragment() {
    }

    public static LatestOrdersFragment newInstance() {
        LatestOrdersFragment fragment = new LatestOrdersFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_agents, container, false);

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
        mAdapter.notifyDataSetChanged();
    }

}
