package strip.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import strip.domain.enumeration.DriverStatus;

/**
 * A DTO for the {@link strip.domain.Driver} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DriverDTO implements Serializable {

    private Long id;

    private UUID driverID;

    private Boolean usedtoDriver;

    private Instant expirationDate;

    private DriverStatus driverStatus;

    private Integer driverPoint;

    private Instant bannedDay;

    @Lob
    private byte[] driverLicense;

    private String driverLicenseContentType;

    @Lob
    private byte[] identityCardFaceUp;

    private String identityCardFaceUpContentType;

    @Lob
    private byte[] identityCardFacedown;

    private String identityCardFacedownContentType;

    private UserDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
    }

    public Boolean getUsedtoDriver() {
        return usedtoDriver;
    }

    public void setUsedtoDriver(Boolean usedtoDriver) {
        this.usedtoDriver = usedtoDriver;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public DriverStatus getDriverStatus() {
        return driverStatus;
    }

    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }

    public Integer getDriverPoint() {
        return driverPoint;
    }

    public void setDriverPoint(Integer driverPoint) {
        this.driverPoint = driverPoint;
    }

    public Instant getBannedDay() {
        return bannedDay;
    }

    public void setBannedDay(Instant bannedDay) {
        this.bannedDay = bannedDay;
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

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DriverDTO)) {
            return false;
        }

        DriverDTO driverDTO = (DriverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, driverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DriverDTO{" +
            "id=" + getId() +
            ", driverID='" + getDriverID() + "'" +
            ", usedtoDriver='" + getUsedtoDriver() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", driverStatus='" + getDriverStatus() + "'" +
            ", driverPoint=" + getDriverPoint() +
            ", bannedDay='" + getBannedDay() + "'" +
            ", driverLicense='" + getDriverLicense() + "'" +
            ", identityCardFaceUp='" + getIdentityCardFaceUp() + "'" +
            ", identityCardFacedown='" + getIdentityCardFacedown() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
