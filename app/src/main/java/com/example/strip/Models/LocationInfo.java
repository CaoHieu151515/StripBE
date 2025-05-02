package com.example.strip.Models;

public class LocationInfo {
    private String startLocation;
    private String endLocation;
    private String distance;
    private String duration;

    public LocationInfo(String startLocation, String endLocation, String distance, String duration) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
        this.duration = duration;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
