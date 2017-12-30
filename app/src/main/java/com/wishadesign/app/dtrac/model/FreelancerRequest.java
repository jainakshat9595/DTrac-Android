package com.wishadesign.app.dtrac.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aksha on 12/15/2017.
 */

public class FreelancerRequest {

    private String outletName;
    private String dateFrom;
    private String dateTo;
    private String slotAmount;
    private String status;

    public FreelancerRequest() {

    }

    public static FreelancerRequest parse(JSONObject obj) throws JSONException {
        FreelancerRequest freelancerRequest = new FreelancerRequest();
        freelancerRequest.setOutletName(obj.getString("branch_name"));
        freelancerRequest.setDateFrom(obj.getString("date_from"));
        freelancerRequest.setDateTo(obj.getString("date_to"));
        freelancerRequest.setSlotAmount(obj.getString("slot_amount"));
        freelancerRequest.setStatus(obj.getString("status"));

        return freelancerRequest;
    }


    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getSlotAmount() {
        return slotAmount;
    }

    public void setSlotAmount(String slotAmount) {
        this.slotAmount = slotAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
