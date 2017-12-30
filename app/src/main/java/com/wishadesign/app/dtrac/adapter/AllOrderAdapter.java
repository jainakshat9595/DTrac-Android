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
import com.wishadesign.app.dtrac.fragment.OrderFragment;
import com.wishadesign.app.dtrac.model.Agent;
import com.wishadesign.app.dtrac.model.Order;
import com.wishadesign.app.dtrac.model.Outlet;
import com.wishadesign.app.dtrac.util.APIRequest;
import com.wishadesign.app.dtrac.util.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aksha on 12/15/2017.
 */

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.MyViewHolder> {

    private OrderFragment mParentFragment;
    private Context mContext;
    private ArrayList<Order> mDataList;
    private ArrayList<Agent> mAgentDataList;

    private ProgressDialog mProgress;

    private View parentView;


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

    public AllOrderAdapter(OrderFragment orderFragment, Context context, ArrayList<Order> mDataList, ArrayList<Agent> mAgentDataList) {
        this.mContext = context;
        this.mDataList = mDataList;
        this.mAgentDataList = mAgentDataList;
        this.mParentFragment = orderFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_order_list_item, parent, false);

        parentView = itemView;

        mProgress = new ProgressDialog(mContext);
        mProgress.setTitle("Loading");
        mProgress.setMessage("Wait while loading...");
        mProgress.setCancelable(false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
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
        if(status.equals("Arrived At Location")||status.equals("UnAttended")) {
            holder.orderReassignButtonEnable.setVisibility(View.VISIBLE);
            holder.orderReassignButtonDisable.setVisibility(View.GONE);
        } else {
            holder.orderReassignButtonEnable.setVisibility(View.GONE);
            holder.orderReassignButtonDisable.setVisibility(View.VISIBLE);
        }

        holder.orderReassignButtonEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(mContext);
                final View reassignDialogView = factory.inflate(R.layout.reassign_dialog_view, null);
                final AlertDialog reassignDialog = new AlertDialog.Builder(mContext).create();
                reassignDialog.setView(reassignDialogView);
                final Spinner agentListSpinner = (Spinner) reassignDialogView.findViewById(R.id.reassign_agent_list);
                ArrayAdapter<Agent> dataAdapter = new ArrayAdapter<Agent>(mContext, android.R.layout.simple_spinner_item, mAgentDataList);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                agentListSpinner.setAdapter(dataAdapter);
                reassignDialogView.findViewById(R.id.reassign_confirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reassignOrder(((Agent)agentListSpinner.getSelectedItem()).getUserId(), data.getOrderId());
                        reassignDialog.dismiss();
                    }
                });
                reassignDialogView.findViewById(R.id.reassign_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reassignDialog.dismiss();
                    }
                });
                reassignDialog.show();
            }
        });

    }

    public void setData(ArrayList<Order> dataList) {
        this.mDataList = dataList;
    }

    public void setAgentData(ArrayList<Agent> mAgentDataList) {
        this.mAgentDataList = mAgentDataList;
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
                    mParentFragment.getAllOrders();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgress.dismiss();
                        Log.d("AgentFragment", error.getMessage());
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

}