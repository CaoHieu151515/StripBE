package com.example.strip.Models.Response;

import com.google.gson.annotations.SerializedName;

public class ConfirmDriverResponse {
    private String userId;
    private String driverId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String identityCardFaceUpUrl;
    private String identityCardFaceDownUrl;
    private String driverLicenseUrl;
    @SerializedName("vehicle")

    private VehicleResponse vehicleResponse;
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentityCardFaceUpUrl() {
        return identityCardFaceUpUrl;
    }

    public void setIdentityCardFaceUpUrl(String identityCardFaceUpUrl) {
        this.identityCardFaceUpUrl = identityCardFaceUpUrl;
    }

    public String getIdentityCardFaceDownUrl() {
        return identityCardFaceDownUrl;
    }

    public void setIdentityCardFaceDownUrl(String identityCardFaceDownUrl) {
        this.identityCardFaceDownUrl = identityCardFaceDownUrl;
    }

    public String getDriverLicenseUrl() {
        return driverLicenseUrl;
    }

    public void setDriverLicenseUrl(String driverLicenseUrl) {
        this.driverLicenseUrl = driverLicenseUrl;
    }

    public VehicleResponse getVehicleResponse() {
        return vehicleResponse;
    }

    public void setVehicleResponse(VehicleResponse vehicleResponse) {
        this.vehicleResponse = vehicleResponse;
    }
}
