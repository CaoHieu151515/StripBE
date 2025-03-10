package strip.service.dto;

import java.io.Serializable;
import java.time.Instant;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;

public class UsermanageDetailsDTO implements Serializable {

    private byte[] userImage;
    private String driverStatus;
    private String firstName;
    private String lastName;
    private String gender;
    private String phone;
    private Instant dob;
    private String address;
    private String email;
    private byte[] identityCardFaceUp;
    private byte[] identityCardFaceDown;

    public UsermanageDetailsDTO() {}

    public UsermanageDetailsDTO(User user, UserDetail userDetail, Driver driver) {
        if (user != null) {
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.email = user.getEmail();
        }

        if (userDetail != null) {
            this.userImage = userDetail.getUserimage();
            this.gender = userDetail.getGender();
            this.phone = userDetail.getPhone();
            this.dob = userDetail.getDob();
            this.address = userDetail.getAddress();
        }

        if (driver != null) {
            this.driverStatus = driver.getDriverStatus().name();
            this.identityCardFaceUp = driver.getIdentityCardFaceUp();
            this.identityCardFaceDown = driver.getIdentityCardFacedown();
        }
    }

    // Getters và Setters
    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public String getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(String driverStatus) {
        this.driverStatus = driverStatus;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
