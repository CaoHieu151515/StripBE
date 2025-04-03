package strip.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class ConfirmingVehicleDriverDTO implements Serializable {

    private UUID userId;
    private UUID driverId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String identityCardFaceUpUrl;
    private String identityCardFaceDownUrl;
    private String driverLicenseUrl;
    private ConfirmingVehicleDTO vehicle; // Chỉ chứa một phương tiện

    // Getters & Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDriverId() {
        return driverId;
    }

    public void setDriverId(UUID driverId) {
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

    public ConfirmingVehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(ConfirmingVehicleDTO vehicle) {
        this.vehicle = vehicle;
    }
}
