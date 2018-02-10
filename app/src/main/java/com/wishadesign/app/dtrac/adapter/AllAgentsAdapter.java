package com.wishadesign.app.dtrac.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aksha on 12/15/2017.
 */

public class AllAgentsAdapter extends RecyclerView.Adapter<AllAgentsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Agent> mDataList;

    private ProgressDialog mProgress;

    private View parentView;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView phone;
        private TextView city;
        private TextView amount;
        private View loginStatus;
        private Button statusButtonEnable;
        private Button statusButtonDisable;
        private TextView lastlocation;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.agent_name);
            phone = (TextView) view.findViewById(R.id.agent_phone);
            city = (TextView) view.findViewById(R.id.agent_city);
            amount = (TextView) view.findViewById(R.id.agent_amount);
            loginStatus = (View) view.findViewById(R.id.agent_login_status);
            statusButtonEnable = (Button) view.findViewById(R.id.agent_status_switch_enable);
            statusButtonDisable = (Button) view.findViewById(R.id.agent_status_switch_disable);
            lastlocation = (TextView) view.findViewById(R.id.agent_last_location);
        }
    }

    public AllAgentsAdapter(Context context, ArrayList<Agent> mDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_agent_list_item, parent, false);

        parentView = itemView;

        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Agent data = mDataList.get(position);

        holder.name.setText(data.getUserFullName());
        holder.amount.setText(data.getTotal()+"/-");
        holder.phone.setText(data.getUsername());
        holder.city.setText(data.getCity());
        holder.lastlocation.setText(data.getLastLocation());

        if(data.getLoggedIn().equals("1")) {
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.loginStatus.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.circle_green) );
            } else {
                holder.loginStatus.setBackground( mContext.getResources().getDrawable(R.drawable.circle_green) );
            }
        }

        Log.d("UserStatus", data.getUserStatus());

        holder.statusButtonDisable.setVisibility(View.VISIBLE);
        holder.statusButtonEnable.setVisibility(View.VISIBLE);

        if(data.getUserStatus().toLowerCase().equals("inactive")) {
            holder.statusButtonDisable.setVisibility(View.GONE);
        } else if(data.getUserStatus().toLowerCase().equals("active")) {
            holder.statusButtonEnable.setVisibility(View.GONE);
        }

        holder.statusButtonDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAgentStatus(mDataList.get(position).getUserId(), false);
            }
        });

        holder.statusButtonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeAgentStatus(mDataList.get(position).getUserId(), true);
            }
        });

    }

    public void setData(ArrayList<Agent> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private void changeAgentStatus(final String agentID, Boolean currStatus) {
        String action;

        if(!currStatus) {
            action = "deactivate_agent";
        } else {
            action = "activate_agent";
        }

        final String finalAction = action;
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.PROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    Toast.makeText(mContext, "Agent status successfully updated.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        error.printStackTrace();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> param = new HashMap<String, String>();
                param.put("fire", finalAction);
                param.put("agentId", agentID);
                return param;
            }
        };

        mProgress.show();
        APIRequest.getInstance(mContext).addToRequestQueue(strRequest);
    }
}