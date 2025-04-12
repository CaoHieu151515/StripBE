package strip.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.Vehicle;
import strip.service.dto.ConfirmingVehicleDriverDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverVehicleDTO;

@Mapper(componentModel = "spring")
public interface DriverInfoMapper {
    @Mapping(source = "driver.driverID", target = "driverId")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "userDetail.gender", target = "gender")
    @Mapping(source = "userDetail.address", target = "address")
    @Mapping(source = "userDetail.dob", target = "dob")
    @Mapping(source = "userDetail.appUserDetail", target = "userId")
    @Mapping(target = "driverLicenseUrl", ignore = true)
    @Mapping(target = "identityCardFaceUpUrl", ignore = true)
    @Mapping(target = "identityCardFaceDownUrl", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    DriverInfoDTO toDriverInfoDTO(User user, UserDetail userDetail, Driver driver);

    @Mapping(source = "vehicle.vehicleID", target = "vehicleId")
    @Mapping(source = "vehicle.vehicleType", target = "vehicleType")
    @Mapping(source = "vehicle.vehicleNumber", target = "vehicleNumber")
    @Mapping(source = "vehicle.numberOfSeats", target = "numberOfSeats")
    @Mapping(source = "vehicle.vehicleColor", target = "vehicleColor")
    @Mapping(source = "vehicle.vehicleBrand", target = "vehicleBrand")
    @Mapping(source = "vehicle.status", target = "status")
    @Mapping(target = "vehicleImageUrl", ignore = true)
    @Mapping(target = "carRegistrationUrl", ignore = true)
    @Mapping(target = "vehicleInspectionCertificateUrl", ignore = true)
    @Mapping(target = "carInsuranceUrl", ignore = true)
    DriverVehicleDTO toDriverVehicleDTO(Vehicle vehicle);

    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "userDetail.appUserDetail", target = "userId")
    @Mapping(source = "driver.driverID", target = "driverId")
    @Mapping(source = "vehicle", target = "vehicle")
    @Mapping(target = "identityCardFaceUpUrl", ignore = true)
    @Mapping(target = "identityCardFaceDownUrl", ignore = true)
    @Mapping(target = "driverLicenseUrl", ignore = true)
    @Mapping(target = "vehicle.vehicleImageUrl", ignore = true)
    @Mapping(target = "vehicle.carregistrationUrl", ignore = true)
    @Mapping(target = "vehicle.vehicleInspectionCertificateUrl", ignore = true)
    @Mapping(target = "vehicle.carInsuranceUrl", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    ConfirmingVehicleDriverDTO toConfirmingVehicleDTO(User user, UserDetail userDetail, Driver driver, Vehicle vehicle);
}
