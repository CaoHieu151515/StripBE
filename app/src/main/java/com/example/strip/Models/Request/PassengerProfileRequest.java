package com.example.strip.Models.Request;

public class PassengerProfileRequest {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String dob;
    private String gender;
    private String userImageContentType;
    private byte[] userImage;

    public PassengerProfileRequest(String firstName, String lastName, String phone, String dob, String address, String gender, String userImageContentType, byte[] userImage) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dob = dob;
        this.address = address;
        this.gender = gender;
        this.userImageContentType = userImageContentType;
        this.userImage = userImage;
    }
}
