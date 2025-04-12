package com.example.strip.Models.Response;

public class VehicleResponse {
    private int id;
    private String vehicleID;
    private String vehicleType;
    private String vehicleImageUrl;
    private String carregistrationUrl;
    private String vehicleInspectionCertificateUrl;
    private String carInsuranceUrl;
    private String vehicleNumber;
    private int numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;
    private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCarregistrationUrl() {
        return carregistrationUrl;
    }

    public void setCarregistrationUrl(String carregistrationUrl) {
        this.carregistrationUrl = carregistrationUrl;
    }

    public String getCarInsuranceUrl() {
        return carInsuranceUrl;
    }

    public void setCarInsuranceUrl(String carInsuranceUrl) {
        this.carInsuranceUrl = carInsuranceUrl;
    }

    public String getVehicleInspectionCertificateUrl() {
        return vehicleInspectionCertificateUrl;
    }

    public void setVehicleInspectionCertificateUrl(String vehicleInspectionCertificateUrl) {
        this.vehicleInspectionCertificateUrl = vehicleInspectionCertificateUrl;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
