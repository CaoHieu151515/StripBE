package strip.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import strip.domain.enumeration.PackageDriverStatus;

/**
 * A DTO for the {@link strip.domain.PackageDriver} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PackageDriverDTO implements Serializable {

    private Long id;

    private UUID packageID;

    private Double price;

    private String name;

    private String description;

    private Integer time;

    private Integer bonus;

    private PackageDriverStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPackageID() {
        return packageID;
    }

    public void setPackageID(UUID packageID) {
        this.packageID = packageID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getBonus() {
        return bonus;
    }

    public void setBonus(Integer bonus) {
        this.bonus = bonus;
    }

    public PackageDriverStatus getStatus() {
        return status;
    }

    public void setStatus(PackageDriverStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PackageDriverDTO)) {
            return false;
        }

        PackageDriverDTO packageDriverDTO = (PackageDriverDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, packageDriverDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PackageDriverDTO{" +
            "id=" + getId() +
            ", packageID='" + getPackageID() + "'" +
            ", price=" + getPrice() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", time=" + getTime() +
            ", bonus=" + getBonus() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
