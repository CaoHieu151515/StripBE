package strip.service.dto;

import java.io.Serializable;
import java.util.UUID;
import strip.domain.enumeration.VehicleStatus;
import strip.domain.enumeration.VehicleType;

public class ConfirmingVehicleDTO implements Serializable {

    private Long id;

    private UUID vehicleID;

    private VehicleType vehicleType;

    // ✅ URL thay cho byte[]
    private String vehicleImageUrl;
    private String carregistrationUrl;
    private String vehicleInspectionCertificateUrl;
    private String carInsuranceUrl;

    private String vehicleNumber;

    private Integer numberOfSeats;

    private String vehicleColor;

    private String vehicleBrand;

    private VehicleStatus status;

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(UUID vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleImageUrl() {
        return vehicleImageUrl;
    }

    public void setVehicleImageUrl(String vehicleImageUrl) {
        this.vehicleImageUrl = vehicleImageUrl;
    }

    public String getCarregistrationUrl() {
        return carregistrationUrl;
    }

    public void setCarregistrationUrl(String carregistrationUrl) {
        this.carregistrationUrl = carregistrationUrl;
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

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }
}
