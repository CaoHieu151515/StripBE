package strip.service.dto;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;
import strip.domain.Authority;
import strip.domain.User;
import strip.domain.UserDetail;

public class UsermanageDTO implements Serializable {

    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String phoneNumber;
    private boolean active; // Thêm trạng thái kích hoạt
    private Set<String> roles;

    public UsermanageDTO() {}

    // Constructor nhận dữ liệu từ User và UserDetail
    public UsermanageDTO(User user, UserDetail userDetail) {
        if (user != null) {
            this.userId = user.getId();
            this.username = user.getLogin();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
            this.email = user.getEmail();
            this.active = user.isActivated(); // Lấy trạng thái kích hoạt
            this.roles = (user.getAuthorities() != null)
                ? user.getAuthorities().stream().map(Authority::getName).collect(Collectors.toSet())
                : null;
        }

        if (userDetail != null) {
            this.gender = userDetail.getGender();
            this.phoneNumber = userDetail.getPhone();
        }
    }

    // Getters và Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
