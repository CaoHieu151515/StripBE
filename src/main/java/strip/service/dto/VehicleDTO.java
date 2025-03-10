package strip.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import strip.domain.enumeration.VehicleType;

/**
 * A DTO for the {@link strip.domain.Vehicle} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class VehicleDTO implements Serializable {

    private Long id;

    private UUID vehicleID;

    private VehicleType vehicleType;

    @Lob
    private byte[] vehicleImage;

    private String vehicleImageContentType;

    @Lob
    private byte[] carregistration;

    private String carregistrationContentType;

    @Lob
    private byte[] vehicleInspectionCertificate;

    private String vehicleInspectionCertificateContentType;

    private String vehicleNumber;

    private Integer numberOfSeats;

    private String vehicleColor;

    private String vehicleBrand;

    private DriverDTO driver;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(UUID vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
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

    public byte[] getCarregistration() {
        return carregistration;
    }

    public void setCarregistration(byte[] carregistration) {
        this.carregistration = carregistration;
    }

    public String getCarregistrationContentType() {
        return carregistrationContentType;
    }

    public void setCarregistrationContentType(String carregistrationContentType) {
        this.carregistrationContentType = carregistrationContentType;
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

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
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

    public DriverDTO getDriver() {
        return driver;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VehicleDTO)) {
            return false;
        }

        VehicleDTO vehicleDTO = (VehicleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, vehicleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VehicleDTO{" +
            "id=" + getId() +
            ", vehicleID='" + getVehicleID() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", vehicleImage='" + getVehicleImage() + "'" +
            ", carregistration='" + getCarregistration() + "'" +
            ", vehicleInspectionCertificate='" + getVehicleInspectionCertificate() + "'" +
            ", vehicleNumber='" + getVehicleNumber() + "'" +
            ", numberOfSeats=" + getNumberOfSeats() +
            ", vehicleColor='" + getVehicleColor() + "'" +
            ", vehicleBrand='" + getVehicleBrand() + "'" +
            ", driver=" + getDriver() +
            "}";
    }
}
