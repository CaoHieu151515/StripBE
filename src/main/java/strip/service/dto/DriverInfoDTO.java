package strip.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class DriverInfoDTO implements Serializable {

    private Long userId;
    private UUID driverId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String gender;
    private Instant dob;
    private String email;

    // ✅ Đổi byte[] -> URL
    private String driverLicenseUrl;
    private String identityCardFaceUpUrl;
    private String identityCardFaceDownUrl;

    // ✅ Danh sách phương tiện
    private Set<DriverVehicleDTO> vehicles;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDriverLicenseUrl() {
        return driverLicenseUrl;
    }

    public void setDriverLicenseUrl(String driverLicenseUrl) {
        this.driverLicenseUrl = driverLicenseUrl;
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

    public Set<DriverVehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<DriverVehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }
    // Getters & Setters

}
