package com.example.strip.Models.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteResponse {
    @SerializedName("routes")
    public List<Route> routes;

    public static class Route {
        @SerializedName("legs")
        public List<Leg> legs;

        @SerializedName("overview_polyline")
        public Polyline overviewPolyline;
    }

    public static class Leg {
        @SerializedName("distance")
        public TextValue distance;

        @SerializedName("duration")
        public TextValue duration;
    }

    public static class TextValue {
        @SerializedName("text")
        public String text;
    }

    public static class Polyline {
        @SerializedName("points")
        public String points;
    }
}
