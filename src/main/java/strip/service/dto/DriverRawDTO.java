package strip.service.dto;

import java.time.Instant;
import java.util.UUID;

public class DriverRawDTO {

    private UUID driverId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String gender;
    private Instant dob;
    private String email;
    private Double rating;

    // ✅ Đổi byte[] -> URL
    private String avatarUrl;
    private String driverLicenseUrl;
    private String identityCardFaceUpUrl;
    private String identityCardFaceDownUrl;

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

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
