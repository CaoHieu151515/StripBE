package com.example.strip.Models.Request;

public class ConfirmDriverRequest {
    // 🔹 Thông tin cá nhân
    private String firstName;
    private String lastName;
    private String phone;

    // 🔹 Giấy tờ Driver
    private byte[] driverLicense;
    private String driverLicenseContentType;

    private byte[] identityCardFaceUp;
    private String identityCardFaceUpContentType;

    private byte[] identityCardFacedown;
    private String identityCardFacedownContentType;

    // 🔹 Thông tin xe
    private String vehicleNumber;
    private String vehicleType;
    private Integer numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;

    // 🔹 Ảnh xe
    private byte[] vehicleImage;
    private String vehicleImageContentType;

    private byte[] carRegistration;
    private String carRegistrationContentType;

    private byte[] vehicleInspectionCertificate;
    private String vehicleInspectionCertificateContentType;

    private byte[] carInsurance;
    private String carInsuranceContentType;

    public ConfirmDriverRequest(String firstName, String lastName, String phone, byte[] driverLicense, byte[] identityCardFaceUp, String driverLicenseContentType, String identityCardFaceUpContentType, byte[] identityCardFacedown, String identityCardFacedownContentType, Integer numberOfSeats, String vehicleType, String vehicleNumber, String vehicleColor, byte[] vehicleImage, String vehicleBrand, String vehicleImageContentType, byte[] carRegistration, String carRegistrationContentType, byte[] vehicleInspectionCertificate, String vehicleInspectionCertificateContentType, byte[] carInsurance, String carInsuranceContentType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.driverLicense = driverLicense;
        this.identityCardFaceUp = identityCardFaceUp;
        this.driverLicenseContentType = driverLicenseContentType;
        this.identityCardFaceUpContentType = identityCardFaceUpContentType;
        this.identityCardFacedown = identityCardFacedown;
        this.identityCardFacedownContentType = identityCardFacedownContentType;
        this.numberOfSeats = numberOfSeats;
        this.vehicleType = vehicleType;
        this.vehicleNumber = vehicleNumber;
        this.vehicleColor = vehicleColor;
        this.vehicleImage = vehicleImage;
        this.vehicleBrand = vehicleBrand;
        this.vehicleImageContentType = vehicleImageContentType;
        this.carRegistration = carRegistration;
        this.carRegistrationContentType = carRegistrationContentType;
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
        this.vehicleInspectionCertificateContentType = vehicleInspectionCertificateContentType;
        this.carInsurance = carInsurance;
        this.carInsuranceContentType = carInsuranceContentType;
    }
}
