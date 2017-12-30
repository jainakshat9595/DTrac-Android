package com.wishadesign.app.dtrac.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.model.FixedAssignment;

import java.util.ArrayList;

/**
 * Created by aksha on 12/15/2017.
 */

public class AllFixedAssignmentAdapter extends RecyclerView.Adapter<AllFixedAssignmentAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<FixedAssignment> mDataList;

    private ProgressDialog mProgress;

    private View parentView;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView dateFrom;
        private TextView dateTo;
        private TextView amount;
        private View status;
        private TextView agent;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.fixed_assign_outlet_name);
            dateFrom = (TextView) view.findViewById(R.id.fixed_assign_date_from);
            dateTo = (TextView) view.findViewById(R.id.fixed_assign_date_to);
            amount = (TextView) view.findViewById(R.id.fixed_assign_slot_amount);
            status = (View) view.findViewById(R.id.fixed_assign_status);
            agent = (TextView) view.findViewById(R.id.fixed_assign_agent);

        }
    }

    public AllFixedAssignmentAdapter(Context context, ArrayList<FixedAssignment> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_fixed_assignment_list_item, parent, false);

        parentView = itemView;

        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        FixedAssignment data = mDataList.get(position);

        holder.name.setText(data.getBranch_name());
        holder.dateFrom.setText(data.getDate_from());
        holder.dateTo.setText(data.getDate_to());
        holder.amount.setText(data.getSlot_amount()+"/-");
        holder.agent.setText(data.getAgentName()+" - "+data.getAgentContact());

        if(data.getStatus().equals("Accepted")) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.status.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.circle_green) );
            } else {
                holder.status.setBackground( mContext.getResources().getDrawable(R.drawable.circle_green) );
            }
        }

    }

    public void setData(ArrayList<FixedAssignment> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

}