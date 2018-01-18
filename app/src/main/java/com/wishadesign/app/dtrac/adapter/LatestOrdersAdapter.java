package com.wishadesign.app.dtrac.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wishadesign.app.dtrac.R;
import com.wishadesign.app.dtrac.fragment.LatestAgentsFragment;
import com.wishadesign.app.dtrac.fragment.LatestOrdersFragment;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.Order;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aksha on 12/15/2017.
 */

public class LatestOrdersAdapter extends RecyclerView.Adapter<LatestOrdersAdapter.MyViewHolder> {

    private ArrayList<Agent> mAgentDataList;
    private final LatestOrdersFragment mParentFragment;
    private Context mContext;
    private ArrayList<Order> mDataList;

    private ProgressDialog mProgress;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView orderName;
        private TextView orderAddress;
        private TextView orderAmount;
        private TextView orderStatus;
        private TextView orderPaymentMode;
        private TextView orderOutletName;
        private TextView orderAgentName;
        private TextView orderTime;
        private Button orderReassignButtonEnable;
        private Button orderReassignButtonDisable;

        public MyViewHolder(View view) {
            super(view);
            orderName = (TextView) view.findViewById(R.id.order_name);
            orderAddress = (TextView) view.findViewById(R.id.order_address);
            orderAmount = (TextView) view.findViewById(R.id.order_amount);
            orderStatus = (TextView) view.findViewById(R.id.order_status);
            orderPaymentMode = (TextView) view.findViewById(R.id.order_pay_mode);
            orderOutletName = (TextView) view.findViewById(R.id.order_outlet_name);
            orderAgentName = (TextView) view.findViewById(R.id.order_agent_name);
            orderTime = (TextView) view.findViewById(R.id.order_time);
            orderReassignButtonDisable = (Button) view.findViewById(R.id.order_reassign_disable);
            orderReassignButtonEnable = (Button) view.findViewById(R.id.order_reassign_enable);

        }
    }

    public LatestOrdersAdapter(Context context, ArrayList<Order> mDataList, ArrayList<Agent> mAgentDataList, LatestOrdersFragment mParentFragment) {
        this.mContext = context;
        this.mDataList = mDataList;
        this.mAgentDataList = mAgentDataList;
        this.mParentFragment = mParentFragment;

        mProgress = new ProgressDialog(context);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_order_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Order data = mDataList.get(position);

        String status = data.getStatus();
        holder.orderName.setText(data.getOrderName());
        holder.orderAddress.setText(data.getAddress());
        holder.orderAmount.setText(data.getAmount()+"/-");
        holder.orderStatus.setText(status);
        holder.orderPaymentMode.setText(data.getPaymentMode());
        holder.orderOutletName.setText(data.getOutletName());
        holder.orderAgentName.setText("Agent: "+data.getAgentName());
        holder.orderTime.setText(data.getCreatedAt());
        holder.orderReassignButtonDisable.setVisibility(View.GONE);
        holder.orderReassignButtonEnable.setVisibility(View.GONE);
    }

    public void setData(ArrayList<Order> dataList) {
        this.mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private void reassignOrder(final String agentID, final String orderID) {
        StringRequest strRequest = new StringRequest(Request.Method.POST, Config.BASE_URL+Config.PROCESS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mProgress.dismiss();
                try {
                    JSONObject resp = new JSONObject(response);
                    Log.d("OrderFragment", response);
                    Toast.makeText(mContext, resp.getString("message"), Toast.LENGTH_SHORT).show();
                    mParentFragment.refresh();
                } catch (JSONException e) {
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
                param.put("fire", "reassign_order");
                param.put("orderId", orderID);
                param.put("agentId", agentID);
                return param;
            }
        };

        mProgress.show();
        APIRequest.getInstance(mContext).addToRequestQueue(strRequest);
    }

    public void setAgentData(ArrayList<Agent> mAgentDataList) {
        this.mAgentDataList = mAgentDataList;
        notifyDataSetChanged();
    }
}