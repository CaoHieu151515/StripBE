package strip.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

public class DriverInfoDTO implements Serializable {

    private Long userId;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String gender;
    private Instant dob;
    private String email;
    private byte[] identityCardFaceUp;
    private byte[] identityCardFaceDown;
    private Set<DriverVehicleDTO> vehicles;

    public DriverInfoDTO() {
        // Default constructor
    }

    public DriverInfoDTO(
        Long userId,
        String firstName,
        String lastName,
        String phone,
        String address,
        String gender,
        Instant dob,
        String email,
        byte[] identityCardFaceUp,
        byte[] identityCardFaceDown,
        Set<DriverVehicleDTO> vehicles
    ) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.email = email;
        this.identityCardFaceUp = identityCardFaceUp;
        this.identityCardFaceDown = identityCardFaceDown;
        this.vehicles = vehicles;
    }

    // Getters & Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Set<DriverVehicleDTO> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<DriverVehicleDTO> vehicles) {
        this.vehicles = vehicles;
    }
}
