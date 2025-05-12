package strip.service.dto;

import strip.domain.enumeration.VehicleType;

public class VehicleCreateDTO {

    private VehicleType vehicleType;

    private byte[] vehicleImage;
    private String vehicleImageContentType;

    private byte[] carregistration;
    private String carregistrationContentType;

    private byte[] vehicleInspectionCertificate;
    private String vehicleInspectionCertificateContentType;

    private byte[] carInsurance;
    private String carInsuranceContentType;

    private String vehicleNumber;
    private Integer numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public byte[] getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(byte[] vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleImageContentType() {
        return vehicleImageContentType;
    }

    public void setVehicleImageContentType(String vehicleImageContentType) {
        this.vehicleImageContentType = vehicleImageContentType;
    }

    public byte[] getCarregistration() {
        return carregistration;
    }

    public void setCarregistration(byte[] carregistration) {
        this.carregistration = carregistration;
    }

    public String getCarregistrationContentType() {
        return carregistrationContentType;
    }

    public void setCarregistrationContentType(String carregistrationContentType) {
        this.carregistrationContentType = carregistrationContentType;
    }

    public byte[] getVehicleInspectionCertificate() {
        return vehicleInspectionCertificate;
    }

    public void setVehicleInspectionCertificate(byte[] vehicleInspectionCertificate) {
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
    }

    public String getVehicleInspectionCertificateContentType() {
        return vehicleInspectionCertificateContentType;
    }

    public void setVehicleInspectionCertificateContentType(String vehicleInspectionCertificateContentType) {
        this.vehicleInspectionCertificateContentType = vehicleInspectionCertificateContentType;
    }

    public byte[] getCarInsurance() {
        return carInsurance;
    }

    public void setCarInsurance(byte[] carInsurance) {
        this.carInsurance = carInsurance;
    }

    public String getCarInsuranceContentType() {
        return carInsuranceContentType;
    }

    public void setCarInsuranceContentType(String carInsuranceContentType) {
        this.carInsuranceContentType = carInsuranceContentType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
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
    // Getters & setters

}
