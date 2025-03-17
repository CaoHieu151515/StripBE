package strip.service.dto;

import java.io.Serializable;
import java.util.UUID;

public class ConfirmingVehicleDriverDTO implements Serializable {

    private Long userId;
    private UUID driverId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private byte[] identityCardFaceUp;
    private byte[] identityCardFaceDown;
    private byte[] driverLicense;
    private DriverVehicleDTO vehicle; // Chỉ chứa một phương tiện

    public ConfirmingVehicleDriverDTO() {
        // Default constructor
    }

    public ConfirmingVehicleDriverDTO(
        Long userId,
        UUID driverId,
        String firstName,
        String lastName,
        String phone,
        String email,
        byte[] identityCardFaceUp,
        byte[] identityCardFaceDown,
        byte[] driverLicense,
        DriverVehicleDTO vehicle
    ) {
        this.userId = userId;
        this.driverId = driverId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.identityCardFaceUp = identityCardFaceUp;
        this.identityCardFaceDown = identityCardFaceDown;
        this.driverLicense = driverLicense;
        this.vehicle = vehicle;
    }

    // Getters & Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getIdentityCardFaceUp() {
        return identityCardFaceUp;
    }

    public void setIdentityCardFaceUp(byte[] identityCardFaceUp) {
        this.identityCardFaceUp = identityCardFaceUp;
    }

    public byte[] getIdentityCardFaceDown() {
        return identityCardFaceDown;
    }

    public void setIdentityCardFaceDown(byte[] identityCardFaceDown) {
        this.identityCardFaceDown = identityCardFaceDown;
    }

    public byte[] getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(byte[] driverLicense) {
        this.driverLicense = driverLicense;
    }

    public DriverVehicleDTO getVehicle() {
        return vehicle;
    }

    public void setVehicle(DriverVehicleDTO vehicle) {
        this.vehicle = vehicle;
    }
}
