package com.wishadesign.app.dtrac.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.model.Outlet;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aksha on 12/15/2017.
 */

public class AllOutletsAdapter extends RecyclerView.Adapter<AllOutletsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Outlet> mDataList;

    private ProgressDialog mProgress;

    private View parentView;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView outletName;
        private TextView manager;
        private TextView city;
        private TextView assignedTo;

        public MyViewHolder(View view) {
            super(view);

            outletName = (TextView) view.findViewById(R.id.outlet_name);
            city = (TextView) view.findViewById(R.id.outlet_city);
            manager = (TextView) view.findViewById(R.id.outlet_manager);
            assignedTo = (TextView) view.findViewById(R.id.outlet_assigned_to);

        }
    }

    public AllOutletsAdapter(Context context, ArrayList<Outlet> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_outlet_list_item, parent, false);

        parentView = itemView;

        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Outlet data = mDataList.get(position);

        holder.outletName.setText(data.getOutletName());
        holder.manager.setText(data.getManagerName()+" - "+data.getMobileNumber());
        holder.city.setText(data.getCity());
        holder.assignedTo.setText(data.getCPFname()+" "+data.getCPLname());

    }

    public void setData(ArrayList<Outlet> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}