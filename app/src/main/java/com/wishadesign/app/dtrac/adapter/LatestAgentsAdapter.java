package com.wishadesign.app.dtrac.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.model.Agent;

import java.util.ArrayList;

/**
 * Created by aksha on 12/15/2017.
 */

public class LatestAgentsAdapter extends RecyclerView.Adapter<LatestAgentsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Agent> mDataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView phone;
        private TextView city;
        private TextView amount;
        private View loginStatus;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.agent_name);
            phone = (TextView) view.findViewById(R.id.agent_phone);
            city = (TextView) view.findViewById(R.id.agent_city);
            amount = (TextView) view.findViewById(R.id.agent_amount);
            loginStatus = (View) view.findViewById(R.id.agent_login_status);

        }
    }

    public LatestAgentsAdapter(Context context, ArrayList<Agent> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.latest_agent_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Agent data = mDataList.get(position);

        holder.name.setText(data.getUserFullName());
        holder.amount.setText(data.getTotal()+"/-");
        holder.phone.setText(data.getUsername());
        holder.city.setText(data.getCity());

        if(data.getLoggedIn().equals("1")) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.loginStatus.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.circle_green) );
            } else {
                holder.loginStatus.setBackground( mContext.getResources().getDrawable(R.drawable.circle_green) );
            }
        }
    }

    public void setData(ArrayList<Agent> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}