package com.example.strip.Models.Response;

import com.example.strip.Models.Driver;
import com.example.strip.Models.User;
import com.example.strip.Models.UserDetailsCusDTO;
import com.example.strip.Models.UserWallet;
import com.example.strip.Models.DriverVehicleDTO;

import java.util.List;

public class UserMoreResponse {
    private User user;
    private UserDetailsCusDTO userDetailsCusDTO;
    private UserWallet userWallet;
    private Driver driver;
    private List<DriverVehicleDTO> driverVehicleDTO;
    private boolean hasVehicle;
    private List<String> roles;

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

    public List<DriverVehicleDTO> getDriverVehicleDTO() {
        return driverVehicleDTO;
    }

    public void setDriverVehicleDTO(List<DriverVehicleDTO> driverVehicleDTO) {
        this.driverVehicleDTO = driverVehicleDTO;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public boolean isHasVehicle() {
        return hasVehicle;
    }

    public void setHasVehicle(boolean hasVehicle) {
        this.hasVehicle = hasVehicle;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
