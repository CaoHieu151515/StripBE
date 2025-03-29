package strip.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

public class UserDetailsCusDTO implements Serializable {

    private UUID appUserDetail;

    private String phone;

    private String gender;

    private String address;

    private Instant dob;

    private String imageUrl; // ✅ Thay vì byte[]

    // Getters & Setters

    public UUID getAppUserDetail() {
        return appUserDetail;
    }

    public void setAppUserDetail(UUID appUserDetail) {
        this.appUserDetail = appUserDetail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
