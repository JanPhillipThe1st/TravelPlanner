package com.yamatoapps.travelplanner;

import java.util.Date;

public class TravelModel {
    public String destination;
    public Date departure;
    public String docId="";
    public String activity;

    public TravelModel(String destination, Date departure, String activity) {
        this.destination = destination;
        this.departure = departure;
        this.activity = activity;
    }
}
