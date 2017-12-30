package com.wishadesign.app.dtrac.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aksha on 12/15/2017.
 */

public class Order {

    private String orderId;
    private String orderName;
    private String address;
    private String amount;
    private String status;
    private String paymentMode;
    private String outletName;
    private String agentName;
    private String createdAt;

    public Order() {

    }

    public static Order parse(JSONObject obj) throws JSONException {
        Order order = new Order();
        order.setOrderId(obj.getString("orderId"));
        order.setOrderName(obj.getString("orderName"));
        order.setAddress(obj.getString("address"));
        order.setAmount(obj.getString("amount"));
        order.setStatus(obj.getString("status"));
        order.setPaymentMode(obj.getString("paymentMode"));
        order.setOutletName(obj.getString("outletName"));
        order.setAgentName(obj.getString("agentName"));
        order.setCreatedAt(obj.getString("createdAt"));

        return order;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
