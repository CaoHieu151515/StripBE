package strip.service.dto;

import java.util.List;
import java.util.Set;
import strip.domain.User;
import strip.domain.UserWallet;

public class UserProfileDTO {

    private User user;
    private UserDetailsCusDTO userDetailsCusDTO;
    private UserWallet userWallet;
    private DriverInfoDTO driver;
    private List<DriverVehicleDTO> driverVehicleDTO;
    private boolean isDriver;
    private boolean hasVehicle;
    private Set<String> roles;

    public UserProfileDTO() {}

    public UserProfileDTO(
        User user,
        UserDetailsCusDTO userDetailsCusDTO,
        UserWallet userWallet,
        DriverInfoDTO driver,
        List<DriverVehicleDTO> driverVehicleDTO,
        boolean isDriver,
        boolean hasVehicle,
        Set<String> roles
    ) {
        this.user = user;
        this.userDetailsCusDTO = userDetailsCusDTO;
        this.userWallet = userWallet;
        this.driver = driver;
        this.driverVehicleDTO = driverVehicleDTO;
        this.isDriver = isDriver;
        this.hasVehicle = hasVehicle;
        this.roles = roles;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserDetailsCusDTO getUserDetailsCusDTO() {
        return userDetailsCusDTO;
    }

    public void setUserDetailsCusDTO(UserDetailsCusDTO userDetailsCusDTO) {
        this.userDetailsCusDTO = userDetailsCusDTO;
    }

    public UserWallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public DriverInfoDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverInfoDTO driver) {
        this.driver = driver;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public void setDriver(boolean isDriver) {
        this.isDriver = isDriver;
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

    public List<DriverVehicleDTO> getDriverVehicleDTO() {
        return driverVehicleDTO;
    }

    public void setDriverVehicleDTO(List<DriverVehicleDTO> driverVehicleDTO) {
        this.driverVehicleDTO = driverVehicleDTO;
    }
}
