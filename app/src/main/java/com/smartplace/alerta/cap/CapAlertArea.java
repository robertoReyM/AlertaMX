package com.smartplace.alerta.cap;

import java.util.ArrayList;

/**
 * Created by Roberto on 12/07/2014.
 */
public class CapAlertArea {
    public String getAreaDesc() {
        return areaDesc;
    }

    public void setAreaDesc(String areaDesc) {
        this.areaDesc = areaDesc;
    }


    public ArrayList<String> getCircle() {
        return circle;
    }

    private ArrayList<String> circle;

    public void setCircle(ArrayList<String> circle) {
        this.circle = circle;
    }

    public ArrayList<String> getPolygon() {
        return polygon;
    }

    public void setPolygon(ArrayList<String> polygon) {
        this.polygon = polygon;
    }

    public ArrayList<String> getGeocode() {
        return geocode;
    }

    public void setGeocode(ArrayList<String> geocode) {
        this.geocode = geocode;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCeiling() {
        return ceiling;
    }

    public void setCeiling(String ceiling) {
        this.ceiling = ceiling;
    }
    private String areaDesc;
    private ArrayList<String> polygon;
    private ArrayList<String> geocode;
    private String altitude;
    private String ceiling;
}
