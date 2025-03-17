package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.VehicleType;

/**
 * A Vehicle.
 */
@Entity
@Table(name = "vehicle")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Vehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "vehicle_id", length = 36)
    private UUID vehicleID;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType vehicleType;

    @Lob
    @Column(name = "vehicle_image")
    private byte[] vehicleImage;

    @Column(name = "vehicle_image_content_type")
    private String vehicleImageContentType;

    @Lob
    @Column(name = "carregistration")
    private byte[] carregistration;

    @Column(name = "carregistration_content_type")
    private String carregistrationContentType;

    @Lob
    @Column(name = "vehicle_inspection_certificate")
    private byte[] vehicleInspectionCertificate;

    @Column(name = "vehicle_inspection_certificate_content_type")
    private String vehicleInspectionCertificateContentType;

    @Lob
    @Column(name = "car_insurance")
    private byte[] carInsurance;

    @Column(name = "car_insurance_content_type")
    private String carInsuranceContentType;

    @Column(name = "vehicle_number")
    private String vehicleNumber;

    @Column(name = "number_of_seats")
    private Integer numberOfSeats;

    @Column(name = "vehicle_color")
    private String vehicleColor;

    @Column(name = "vehicle_brand")
    private String vehicleBrand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "user", "vehicles", "trips", "feedbacks", "ratings", "driverPackageSubscriptions" },
        allowSetters = true
    )
    private Driver driver;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vehicle")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "vehicle", "driver", "requestTrips", "tripStopLocations", "feedbacks", "ratings", "passengers" },
        allowSetters = true
    )
    private Set<Trip> trips = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vehicle id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getVehicleID() {
        return this.vehicleID;
    }

    public Vehicle vehicleID(UUID vehicleID) {
        this.setVehicleID(vehicleID);
        return this;
    }

    public void setVehicleID(UUID vehicleID) {
        this.vehicleID = vehicleID;
    }

    public VehicleType getVehicleType() {
        return this.vehicleType;
    }

    public Vehicle vehicleType(VehicleType vehicleType) {
        this.setVehicleType(vehicleType);
        return this;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public byte[] getVehicleImage() {
        return this.vehicleImage;
    }

    public Vehicle vehicleImage(byte[] vehicleImage) {
        this.setVehicleImage(vehicleImage);
        return this;
    }

    public void setVehicleImage(byte[] vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public String getVehicleImageContentType() {
        return this.vehicleImageContentType;
    }

    public Vehicle vehicleImageContentType(String vehicleImageContentType) {
        this.vehicleImageContentType = vehicleImageContentType;
        return this;
    }

    public void setVehicleImageContentType(String vehicleImageContentType) {
        this.vehicleImageContentType = vehicleImageContentType;
    }

    public byte[] getCarregistration() {
        return this.carregistration;
    }

    public Vehicle carregistration(byte[] carregistration) {
        this.setCarregistration(carregistration);
        return this;
    }

    public void setCarregistration(byte[] carregistration) {
        this.carregistration = carregistration;
    }

    public String getCarregistrationContentType() {
        return this.carregistrationContentType;
    }

    public Vehicle carregistrationContentType(String carregistrationContentType) {
        this.carregistrationContentType = carregistrationContentType;
        return this;
    }

    public void setCarregistrationContentType(String carregistrationContentType) {
        this.carregistrationContentType = carregistrationContentType;
    }

    public byte[] getVehicleInspectionCertificate() {
        return this.vehicleInspectionCertificate;
    }

    public Vehicle vehicleInspectionCertificate(byte[] vehicleInspectionCertificate) {
        this.setVehicleInspectionCertificate(vehicleInspectionCertificate);
        return this;
    }

    public void setVehicleInspectionCertificate(byte[] vehicleInspectionCertificate) {
        this.vehicleInspectionCertificate = vehicleInspectionCertificate;
    }

    public String getVehicleInspectionCertificateContentType() {
        return this.vehicleInspectionCertificateContentType;
    }

    public Vehicle vehicleInspectionCertificateContentType(String vehicleInspectionCertificateContentType) {
        this.vehicleInspectionCertificateContentType = vehicleInspectionCertificateContentType;
        return this;
    }

    public void setVehicleInspectionCertificateContentType(String vehicleInspectionCertificateContentType) {
        this.vehicleInspectionCertificateContentType = vehicleInspectionCertificateContentType;
    }

    public byte[] getCarInsurance() {
        return this.carInsurance;
    }

    public Vehicle carInsurance(byte[] carInsurance) {
        this.setCarInsurance(carInsurance);
        return this;
    }

    public void setCarInsurance(byte[] carInsurance) {
        this.carInsurance = carInsurance;
    }

    public String getCarInsuranceContentType() {
        return this.carInsuranceContentType;
    }

    public Vehicle carInsuranceContentType(String carInsuranceContentType) {
        this.carInsuranceContentType = carInsuranceContentType;
        return this;
    }

    public void setCarInsuranceContentType(String carInsuranceContentType) {
        this.carInsuranceContentType = carInsuranceContentType;
    }

    public String getVehicleNumber() {
        return this.vehicleNumber;
    }

    public Vehicle vehicleNumber(String vehicleNumber) {
        this.setVehicleNumber(vehicleNumber);
        return this;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public Integer getNumberOfSeats() {
        return this.numberOfSeats;
    }

    public Vehicle numberOfSeats(Integer numberOfSeats) {
        this.setNumberOfSeats(numberOfSeats);
        return this;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getVehicleColor() {
        return this.vehicleColor;
    }

    public Vehicle vehicleColor(String vehicleColor) {
        this.setVehicleColor(vehicleColor);
        return this;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleBrand() {
        return this.vehicleBrand;
    }

    public Vehicle vehicleBrand(String vehicleBrand) {
        this.setVehicleBrand(vehicleBrand);
        return this;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public Driver getDriver() {
        return this.driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public Vehicle driver(Driver driver) {
        this.setDriver(driver);
        return this;
    }

    public Set<Trip> getTrips() {
        return this.trips;
    }

    public void setTrips(Set<Trip> trips) {
        if (this.trips != null) {
            this.trips.forEach(i -> i.setVehicle(null));
        }
        if (trips != null) {
            trips.forEach(i -> i.setVehicle(this));
        }
        this.trips = trips;
    }

    public Vehicle trips(Set<Trip> trips) {
        this.setTrips(trips);
        return this;
    }

    public Vehicle addTrip(Trip trip) {
        this.trips.add(trip);
        trip.setVehicle(this);
        return this;
    }

    public Vehicle removeTrip(Trip trip) {
        this.trips.remove(trip);
        trip.setVehicle(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicle)) {
            return false;
        }
        return getId() != null && getId().equals(((Vehicle) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicle{" +
            "id=" + getId() +
            ", vehicleID='" + getVehicleID() + "'" +
            ", vehicleType='" + getVehicleType() + "'" +
            ", vehicleImage='" + getVehicleImage() + "'" +
            ", vehicleImageContentType='" + getVehicleImageContentType() + "'" +
            ", carregistration='" + getCarregistration() + "'" +
            ", carregistrationContentType='" + getCarregistrationContentType() + "'" +
            ", vehicleInspectionCertificate='" + getVehicleInspectionCertificate() + "'" +
            ", vehicleInspectionCertificateContentType='" + getVehicleInspectionCertificateContentType() + "'" +
            ", carInsurance='" + getCarInsurance() + "'" +
            ", carInsuranceContentType='" + getCarInsuranceContentType() + "'" +
            ", vehicleNumber='" + getVehicleNumber() + "'" +
            ", numberOfSeats=" + getNumberOfSeats() +
            ", vehicleColor='" + getVehicleColor() + "'" +
            ", vehicleBrand='" + getVehicleBrand() + "'" +
            "}";
    }
}
