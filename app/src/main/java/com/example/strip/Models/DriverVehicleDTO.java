package com.example.strip.Models;

public class DriverVehicleDTO {
    private String vehicleId;
    private String vehicleType;
    private String vehicleImageUrl;
    private String carRegistrationUrl;
    private String vehicleInspectionCertificateUrl;
    private String carInsuranceUrl;
    private String vehicleNumber;
    private int numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;
    private String status;

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getCarRegistrationUrl() {
        return carRegistrationUrl;
    }

    public void setCarRegistrationUrl(String carRegistrationUrl) {
        this.carRegistrationUrl = carRegistrationUrl;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public String getVehicleInspectionCertificateUrl() {
        return vehicleInspectionCertificateUrl;
    }

    public void setVehicleInspectionCertificateUrl(String vehicleInspectionCertificateUrl) {
        this.vehicleInspectionCertificateUrl = vehicleInspectionCertificateUrl;
    }

    public String getCarInsuranceUrl() {
        return carInsuranceUrl;
    }

    public void setCarInsuranceUrl(String carInsuranceUrl) {
        this.carInsuranceUrl = carInsuranceUrl;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
