package com.wishadesign.app.dtrac.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.model.FreelancerRequest;

import java.util.ArrayList;

/**
 * Created by aksha on 12/15/2017.
 */

public class LatestFreelancerRequestAdapter extends RecyclerView.Adapter<LatestFreelancerRequestAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<FreelancerRequest> mDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView dateFrom;
        private TextView dateTo;
        private TextView amount;
        private View status;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.ff_request_outlet_name);
            dateFrom = (TextView) view.findViewById(R.id.ff_request_date_from);
            dateTo = (TextView) view.findViewById(R.id.ff_request_date_to);
            amount = (TextView) view.findViewById(R.id.ff_request_slot_amount);
            status = (View) view.findViewById(R.id.ff_request_status);

        }
    }

    public LatestFreelancerRequestAdapter(Context context, ArrayList<FreelancerRequest> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_freelancer_request_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FreelancerRequest data = mDataList.get(position);

        holder.name.setText(data.getOutletName());
        holder.amount.setText(data.getSlotAmount()+"/-");
        holder.dateFrom.setText("From: "+data.getDateFrom());
        holder.dateTo.setText("To: "+data.getDateTo());

        if(data.getStatus().equals("Accepted")) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.status.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.circle_green) );
            } else {
                holder.status.setBackground( mContext.getResources().getDrawable(R.drawable.circle_green) );
            }
        }
    }

    public void setData(ArrayList<FreelancerRequest> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}