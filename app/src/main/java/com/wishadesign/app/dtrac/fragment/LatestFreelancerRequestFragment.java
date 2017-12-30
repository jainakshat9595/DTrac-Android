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
import com.wishadesign.app.dtrac.adapter.LatestFreelancerRequestAdapter;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.FreelancerRequest;

import java.util.ArrayList;

/**
 * Created by aksha on 12/19/2017.
 */

public class LatestFreelancerRequestFragment extends Fragment {

    private RecyclerView mLatestFreelancerRequestRV;
    private LatestFreelancerRequestAdapter mAdapter;

    public LatestFreelancerRequestFragment() {
    }

    public static LatestFreelancerRequestFragment newInstance() {
        LatestFreelancerRequestFragment fragment = new LatestFreelancerRequestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest_freelancer_request, container, false);

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
        mAdapter.notifyDataSetChanged();
    }

}
