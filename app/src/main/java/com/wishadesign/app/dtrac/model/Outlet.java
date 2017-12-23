package com.wishadesign.app.dtrac.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aksha on 12/23/2017.
 */

public class Outlet {

    private String outletId;
    private String outletName;
    private String managerName;
    private String mobileNumber;
    private String city;
    private String outletAddress;
    private String CPFname;
    private String CPLname;

    public Outlet() {

    }

    public static Outlet parse(JSONObject obj) throws JSONException {
        Outlet outlet = new Outlet();
        outlet.setOutletId(obj.getString("outletId"));
        outlet.setOutletName(obj.getString("outletName"));
        outlet.setManagerName(obj.getString("managerName"));
        outlet.setMobileNumber(obj.getString("mobileNumber"));
        outlet.setCity(obj.getString("city"));
        outlet.setOutletAddress(obj.getString("outletAddress"));
        outlet.setCPFname(obj.getString("CPFname"));
        outlet.setCPLname(obj.getString("CPLname"));

        return outlet;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getOutletName() {
        return outletName;
    }

    public void setOutletName(String outletName) {
        this.outletName = outletName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getCPFname() {
        return CPFname;
    }

    public void setCPFname(String CPFname) {
        this.CPFname = CPFname;
    }

    public String getCPLname() {
        return CPLname;
    }

    public void setCPLname(String CPLname) {
        this.CPLname = CPLname;
    }
}
