package com.wishadesign.app.dtrac.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aksha on 12/15/2017.
 */

public class FixedAssignment {

    private String id;
    private String addedById;
    private String slot_amount;
    private String total_amount_required;
    private String date_from;
    private String date_to;
    private String no_of_days;
    private String branch_id;
    private String branch_name;
    private String status;
    private String agentId;
    private String agentName;
    private String agentContact;
    private String createdAt;

    public FixedAssignment() {

    }

    public static FixedAssignment parse(JSONObject obj) throws JSONException {
        FixedAssignment freelancerRequest = new FixedAssignment();
        freelancerRequest.setId(obj.getString("id"));
        freelancerRequest.setAddedById(obj.getString("addedById"));
        freelancerRequest.setSlot_amount(obj.getString("slot_amount"));
        freelancerRequest.setTotal_amount_required(obj.getString("total_amount_required"));
        freelancerRequest.setDate_from(obj.getString("date_from"));
        freelancerRequest.setDate_to(obj.getString("date_to"));
        freelancerRequest.setNo_of_days(obj.getString("no_of_days"));
        freelancerRequest.setBranch_id(obj.getString("branch_id"));
        freelancerRequest.setBranch_name(obj.getString("branch_name"));
        freelancerRequest.setStatus(obj.getString("status"));
        freelancerRequest.setAgentId(obj.getString("agentId"));
        freelancerRequest.setAgentName(obj.getString("agentName"));
        freelancerRequest.setAgentContact(obj.getString("agentContact"));
        freelancerRequest.setCreatedAt(obj.getString("createdAt"));

        return freelancerRequest;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddedById() {
        return addedById;
    }

    public void setAddedById(String addedById) {
        this.addedById = addedById;
    }

    public String getSlot_amount() {
        return slot_amount;
    }

    public void setSlot_amount(String slot_amount) {
        this.slot_amount = slot_amount;
    }

    public String getTotal_amount_required() {
        return total_amount_required;
    }

    public void setTotal_amount_required(String total_amount_required) {
        this.total_amount_required = total_amount_required;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getNo_of_days() {
        return no_of_days;
    }

    public void setNo_of_days(String no_of_days) {
        this.no_of_days = no_of_days;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentContact() {
        return agentContact;
    }

    public void setAgentContact(String agentContact) {
        this.agentContact = agentContact;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
