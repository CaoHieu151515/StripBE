package com.example.strip.Models.Request;

public class AddVehicleRequest {
    private String vehicleNumber;
    private Integer numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;
    private String vehicleType;
    private byte[] vehicleImage;
    private String vehicleImageContentType;
    private byte[] carregistration;
    private String carregistrationContentType;
    private byte[] vehicleInspectionCertificate;
    private String vehicleInspectionCertificateContentType;
    private byte[] carInsurance;
    private String carInsuranceContentType;

    public AddVehicleRequest(String vehicleNumber, Integer numberOfSeats, String vehicleColor, String vehicleBrand, String vehicleType, byte[] vehicleImage, String vehicleImageContentType, byte[] carregistration, String carregistrationContentType, byte[] vehicleInspectionCertificate, String vehicleInspectionCertificateContentType, byte[] carInsurance, String carInsuranceContentType) {
        this.vehicleNumber = vehicleNumber;
        this.numberOfSeats = numberOfSeats;
        this.vehicleColor = vehicleColor;
        this.vehicleBrand = vehicleBrand;
        this.vehicleType = vehicleType;

        this.vehicleImage = vehicleImage;
        this.vehicleImageContentType = vehicleImageContentType;
        this.carregistration = carregistration;
        this.carregistrationContentType = carregistrationContentType;
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
        this.vehicleInspectionCertificateContentType = vehicleInspectionCertificateContentType;
        this.carInsurance = carInsurance;
        this.carInsuranceContentType = carInsuranceContentType;
    }
}
