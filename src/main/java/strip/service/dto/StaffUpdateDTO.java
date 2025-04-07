package strip.service.dto;

import java.time.Instant;
import java.util.UUID;
import strip.domain.User;
import strip.domain.UserDetail;

public class StaffUpdateDTO {

    private UUID UserId;
    private String firstName;
    private String lastName;
    private String email;

    // Thông tin UserDetail
    private String phone;
    private String address;
    private Instant dob;
    private String gender;
    private Boolean status;

    public StaffUpdateDTO(User user, UserDetail detail) {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.status = user.isActivated();

        if (detail != null) {
            this.UserId = detail.getAppUserDetail();
            this.phone = detail.getPhone();
            this.address = detail.getAddress();
            this.dob = detail.getDob();
            this.gender = detail.getGender();
        }
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Instant getDob() {
        return dob;
    }

    public void setDob(Instant dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UUID getUserId() {
        return UserId;
    }

    public void setUserId(UUID userId) {
        UserId = userId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
