package com.wishadesign.app.dtrac.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by aksha on 12/15/2017.
 */

public class Agent {

    private String userId;
    private String username;
    private String userFullName;
    private String city;
    private String total;
    private String lat;
    private String lon;
    private String loggedIn;
    private String userStatus;
    private String CPFname;
    private String CPLname;
    private String lastLocation;

    public Agent() {

    }

    public static Agent parse(JSONObject obj) throws JSONException {
        Agent agent = new Agent();
        agent.setUserId(obj.getString("userId"));
        agent.setUsername(obj.getString("username"));
        agent.setUserFullName(obj.getString("userFullName"));
        agent.setCity(obj.getString("city"));
        agent.setTotal(obj.getString("total"));
        agent.setLat(obj.getString("lat"));
        agent.setLon(obj.getString("lon"));
        agent.setLoggedIn(obj.getString("loggedIn"));
        agent.setUserStatus(obj.getString("userStatus"));
        agent.setCPFname(obj.getString("CPFname"));
        agent.setCPLname(obj.getString("CPLname"));

        if(obj.has("lastLocation")) {
            agent.setLastLocation(obj.getString("lastLocation"));
        }
        return agent;
    }

    @Override
    public String toString() {
        return this.getUserFullName();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(String loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
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

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }
}
