package com.example.strip.Models;

public class UserDetailsCusDTO {
    private String appUserDetail;
    private String phone;
    private String gender;
    private String address;
    private String dob;
    private String imageUrl;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAppUserDetail() {
        return appUserDetail;
    }

    public void setAppUserDetail(String appUserDetail) {
        this.appUserDetail = appUserDetail;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
