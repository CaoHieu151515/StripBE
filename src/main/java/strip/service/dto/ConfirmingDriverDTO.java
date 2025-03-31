package strip.service.dto;

import java.io.Serializable;
import strip.domain.enumeration.VehicleType;

public class ConfirmingDriverDTO implements Serializable {

    // 🔹 Thông tin cá nhân
    private String firstName;
    private String lastName;
    private String phone;

    // 🔹 Giấy tờ Driver
    private byte[] driverLicense;
    private String driverLicenseContentType;

    private byte[] identityCardFaceUp;
    private String identityCardFaceUpContentType;

    private byte[] identityCardFacedown;
    private String identityCardFacedownContentType;

    // 🔹 Thông tin xe
    private String vehicleNumber;
    private VehicleType vehicleType;
    private Integer numberOfSeats;
    private String vehicleColor;
    private String vehicleBrand;

    // 🔹 Ảnh xe
    private byte[] vehicleImage;
    private String vehicleImageContentType;

    private byte[] carRegistration;
    private String carRegistrationContentType;

    private byte[] vehicleInspectionCertificate;
    private String vehicleInspectionCertificateContentType;

    private byte[] carInsurance;
    private String carInsuranceContentType;

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

    public byte[] getDriverLicense() {
        return driverLicense;
    }

    public void setDriverLicense(byte[] driverLicense) {
        this.driverLicense = driverLicense;
    }

    public String getDriverLicenseContentType() {
        return driverLicenseContentType;
    }

    public void setDriverLicenseContentType(String driverLicenseContentType) {
        this.driverLicenseContentType = driverLicenseContentType;
    }

    public byte[] getIdentityCardFaceUp() {
        return identityCardFaceUp;
    }

    public void setIdentityCardFaceUp(byte[] identityCardFaceUp) {
        this.identityCardFaceUp = identityCardFaceUp;
    }

    public String getIdentityCardFaceUpContentType() {
        return identityCardFaceUpContentType;
    }

    public void setIdentityCardFaceUpContentType(String identityCardFaceUpContentType) {
        this.identityCardFaceUpContentType = identityCardFaceUpContentType;
    }

    public byte[] getIdentityCardFacedown() {
        return identityCardFacedown;
    }

    public void setIdentityCardFacedown(byte[] identityCardFacedown) {
        this.identityCardFacedown = identityCardFacedown;
    }

    public String getIdentityCardFacedownContentType() {
        return identityCardFacedownContentType;
    }

    public void setIdentityCardFacedownContentType(String identityCardFacedownContentType) {
        this.identityCardFacedownContentType = identityCardFacedownContentType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public byte[] getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(byte[] vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleImageContentType() {
        return vehicleImageContentType;
    }

    public void setVehicleImageContentType(String vehicleImageContentType) {
        this.vehicleImageContentType = vehicleImageContentType;
    }

    public byte[] getCarRegistration() {
        return carRegistration;
    }

    public void setCarRegistration(byte[] carRegistration) {
        this.carRegistration = carRegistration;
    }

    public String getCarRegistrationContentType() {
        return carRegistrationContentType;
    }

    public void setCarRegistrationContentType(String carRegistrationContentType) {
        this.carRegistrationContentType = carRegistrationContentType;
    }

    public byte[] getVehicleInspectionCertificate() {
        return vehicleInspectionCertificate;
    }

    public void setVehicleInspectionCertificate(byte[] vehicleInspectionCertificate) {
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
    }

    public String getVehicleInspectionCertificateContentType() {
        return vehicleInspectionCertificateContentType;
    }

    public void setVehicleInspectionCertificateContentType(String vehicleInspectionCertificateContentType) {
        this.vehicleInspectionCertificateContentType = vehicleInspectionCertificateContentType;
    }

    public byte[] getCarInsurance() {
        return carInsurance;
    }

    public void setCarInsurance(byte[] carInsurance) {
        this.carInsurance = carInsurance;
    }

    public String getCarInsuranceContentType() {
        return carInsuranceContentType;
    }

    public void setCarInsuranceContentType(String carInsuranceContentType) {
        this.carInsuranceContentType = carInsuranceContentType;
    }
    // ✅ Tạo đầy đủ Getter & Setter cho tất cả các trường

}
