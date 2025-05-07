package com.example.strip.Models.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DriverInfoResponse {
    public String driverId;
    public String firstName;
    public String lastName;
    public String phone;
    public String address;
    public String gender;
    public String dob;
    public String email;
    public double averageRating;
    public String avatar;
    public String driverLicenseUrl;
    @SerializedName("ratings")
    public List<RatingOfDriverInfoResponse> ratingOfDriverInfoResponseList;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDriverLicenseUrl() {
        return driverLicenseUrl;
    }

    public void setDriverLicenseUrl(String driverLicenseUrl) {
        this.driverLicenseUrl = driverLicenseUrl;
    }

    public List<RatingOfDriverInfoResponse> getRatingOfDriverInfoResponseList() {
        return ratingOfDriverInfoResponseList;
    }

    public void setRatingOfDriverInfoResponseList(List<RatingOfDriverInfoResponse> ratingOfDriverInfoResponseList) {
        this.ratingOfDriverInfoResponseList = ratingOfDriverInfoResponseList;
    }
}
