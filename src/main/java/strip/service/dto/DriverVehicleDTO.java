package strip.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class DriverVehicleDTO implements Serializable {

    private UUID vehicleId;
    private String vehicleType;
    private byte[] vehicleImage;
    private byte[] carRegistration;
    private byte[] vehicleInspectionCertificate;
    private byte[] carInsurance;
    private String vehicleNumber;
    private Integer numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;

    public DriverVehicleDTO() {
        // Default constructor
    }

    public DriverVehicleDTO(
        UUID vehicleId,
        String vehicleType,
        byte[] vehicleImage,
        byte[] carRegistration,
        byte[] vehicleInspectionCertificate,
        byte[] carInsurance,
        String vehicleNumber,
        Integer numberOfSeats,
        String vehicleColor,
        String vehicleBrand
    ) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.vehicleImage = vehicleImage;
        this.carRegistration = carRegistration;
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
        this.carInsurance = carInsurance;
        this.vehicleNumber = vehicleNumber;
        this.numberOfSeats = numberOfSeats;
        this.vehicleColor = vehicleColor;
        this.vehicleBrand = vehicleBrand;
    }

    // Getters & Setters
    public UUID getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(UUID vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public byte[] getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(byte[] vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public byte[] getCarRegistration() {
        return carRegistration;
    }

    public void setCarRegistration(byte[] carRegistration) {
        this.carRegistration = carRegistration;
    }

    public byte[] getVehicleInspectionCertificate() {
        return vehicleInspectionCertificate;
    }

    public void setVehicleInspectionCertificate(byte[] vehicleInspectionCertificate) {
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
    }

    public byte[] getCarInsurance() {
        return carInsurance;
    }

    public void setCarInsurance(byte[] carInsurance) {
        this.carInsurance = carInsurance;
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
}
