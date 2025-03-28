package strip.service.dto;

import java.util.List;
import java.util.Set;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.UserWallet;
import strip.domain.Vehicle;

public class UserProfileDTO {

    private User user;
    private UserDetail userDetail;
    private UserWallet userWallet;
    private Driver driver;
    private List<Vehicle> vehicles;
    private boolean isDriver;
    private boolean hasVehicle;
    private Set<String> roles;

    // Constructors
    public UserProfileDTO() {}

    public UserProfileDTO(User user, UserDetail userDetail, UserWallet userWallet, Driver driver, List<Vehicle> vehicles) {
        this.user = user;
        this.userDetail = userDetail;
        this.userWallet = userWallet;
        this.driver = driver;
        this.vehicles = vehicles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public UserWallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public boolean isHasVehicle() {
        return hasVehicle;
    }

    public void setHasVehicle(boolean hasVehicle) {
        this.hasVehicle = hasVehicle;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public void setDriver(boolean isDriver) {
        this.isDriver = isDriver;
    }
}
