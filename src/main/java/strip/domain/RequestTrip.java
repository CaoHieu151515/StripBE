package strip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import strip.domain.enumeration.PassengerStatus;
import strip.domain.enumeration.PassengerType;

/**
 * A RequestTrip.
 */
@Entity
@Table(name = "request_trip")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RequestTrip implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "request_trip_id", length = 36)
    private UUID requestTripID;

    @Column(name = "start_loca")
    private String startLoca;

    @Column(name = "end_loca")
    private String endLoca;

    @Column(name = "amount_approve_fee")
    private Double amountApproveFee;

    @Lob
    @Column(name = "luggage_img")
    private byte[] luggageImg;

    @Column(name = "luggage_img_content_type")
    private String luggageImgContentType;

    @Column(name = "luggage_description")
    private String luggageDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PassengerType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PassengerStatus status;

    @Column(name = "pick_up_time")
    private Instant pickUpTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Column(name = "check_in")
    private Boolean checkIn;

    @Column(name = "check_in_time")
    private Instant checkInTime;

    @Column(name = "check_out")
    private Boolean checkOut;

    @Column(name = "check_out_t_ime")
    private Instant checkOutTIme;

    @Column(name = "applied_at")
    private Instant appliedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "vehicle", "driver", "requestTrips", "tripStopLocations", "feedbacks", "ratings", "passengers" },
        allowSetters = true
    )
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RequestTrip id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getRequestTripID() {
        return this.requestTripID;
    }

    public RequestTrip requestTripID(UUID requestTripID) {
        this.setRequestTripID(requestTripID);
        return this;
    }

    public void setRequestTripID(UUID requestTripID) {
        this.requestTripID = requestTripID;
    }

    public String getStartLoca() {
        return this.startLoca;
    }

    public RequestTrip startLoca(String startLoca) {
        this.setStartLoca(startLoca);
        return this;
    }

    public void setStartLoca(String startLoca) {
        this.startLoca = startLoca;
    }

    public String getEndLoca() {
        return this.endLoca;
    }

    public RequestTrip endLoca(String endLoca) {
        this.setEndLoca(endLoca);
        return this;
    }

    public void setEndLoca(String endLoca) {
        this.endLoca = endLoca;
    }

    public Double getAmountApproveFee() {
        return this.amountApproveFee;
    }

    public RequestTrip amountApproveFee(Double amountApproveFee) {
        this.setAmountApproveFee(amountApproveFee);
        return this;
    }

    public void setAmountApproveFee(Double amountApproveFee) {
        this.amountApproveFee = amountApproveFee;
    }

    public byte[] getLuggageImg() {
        return this.luggageImg;
    }

    public RequestTrip luggageImg(byte[] luggageImg) {
        this.setLuggageImg(luggageImg);
        return this;
    }

    public void setLuggageImg(byte[] luggageImg) {
        this.luggageImg = luggageImg;
    }

    public String getLuggageImgContentType() {
        return this.luggageImgContentType;
    }

    public RequestTrip luggageImgContentType(String luggageImgContentType) {
        this.luggageImgContentType = luggageImgContentType;
        return this;
    }

    public void setLuggageImgContentType(String luggageImgContentType) {
        this.luggageImgContentType = luggageImgContentType;
    }

    public String getLuggageDescription() {
        return this.luggageDescription;
    }

    public RequestTrip luggageDescription(String luggageDescription) {
        this.setLuggageDescription(luggageDescription);
        return this;
    }

    public void setLuggageDescription(String luggageDescription) {
        this.luggageDescription = luggageDescription;
    }

    public PassengerType getType() {
        return this.type;
    }

    public RequestTrip type(PassengerType type) {
        this.setType(type);
        return this;
    }

    public void setType(PassengerType type) {
        this.type = type;
    }

    public PassengerStatus getStatus() {
        return this.status;
    }

    public RequestTrip status(PassengerStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(PassengerStatus status) {
        this.status = status;
    }

    public Instant getPickUpTime() {
        return this.pickUpTime;
    }

    public RequestTrip pickUpTime(Instant pickUpTime) {
        this.setPickUpTime(pickUpTime);
        return this;
    }

    public void setPickUpTime(Instant pickUpTime) {
        this.pickUpTime = pickUpTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public RequestTrip endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Boolean getCheckIn() {
        return this.checkIn;
    }

    public RequestTrip checkIn(Boolean checkIn) {
        this.setCheckIn(checkIn);
        return this;
    }

    public void setCheckIn(Boolean checkIn) {
        this.checkIn = checkIn;
    }

    public Instant getCheckInTime() {
        return this.checkInTime;
    }

    public RequestTrip checkInTime(Instant checkInTime) {
        this.setCheckInTime(checkInTime);
        return this;
    }

    public void setCheckInTime(Instant checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Boolean getCheckOut() {
        return this.checkOut;
    }

    public RequestTrip checkOut(Boolean checkOut) {
        this.setCheckOut(checkOut);
        return this;
    }

    public void setCheckOut(Boolean checkOut) {
        this.checkOut = checkOut;
    }

    public Instant getCheckOutTIme() {
        return this.checkOutTIme;
    }

    public RequestTrip checkOutTIme(Instant checkOutTIme) {
        this.setCheckOutTIme(checkOutTIme);
        return this;
    }

    public void setCheckOutTIme(Instant checkOutTIme) {
        this.checkOutTIme = checkOutTIme;
    }

    public Instant getAppliedAt() {
        return this.appliedAt;
    }

    public RequestTrip appliedAt(Instant appliedAt) {
        this.setAppliedAt(appliedAt);
        return this;
    }

    public void setAppliedAt(Instant appliedAt) {
        this.appliedAt = appliedAt;
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public RequestTrip trip(Trip trip) {
        this.setTrip(trip);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RequestTrip user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequestTrip)) {
            return false;
        }
        return getId() != null && getId().equals(((RequestTrip) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RequestTrip{" +
            "id=" + getId() +
            ", requestTripID='" + getRequestTripID() + "'" +
            ", startLoca='" + getStartLoca() + "'" +
            ", endLoca='" + getEndLoca() + "'" +
            ", amountApproveFee=" + getAmountApproveFee() +
            ", luggageImg='" + getLuggageImg() + "'" +
            ", luggageImgContentType='" + getLuggageImgContentType() + "'" +
            ", luggageDescription='" + getLuggageDescription() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", pickUpTime='" + getPickUpTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", checkIn='" + getCheckIn() + "'" +
            ", checkInTime='" + getCheckInTime() + "'" +
            ", checkOut='" + getCheckOut() + "'" +
            ", checkOutTIme='" + getCheckOutTIme() + "'" +
            ", appliedAt='" + getAppliedAt() + "'" +
            "}";
    }
}
