package strip.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.Vehicle;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.DriverInfoDTO;
import strip.service.dto.DriverVehicleDTO;

@Mapper(componentModel = "spring")
public interface DriverInfoMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "userDetail.gender", target = "gender")
    @Mapping(source = "userDetail.address", target = "address")
    @Mapping(source = "userDetail.dob", target = "dob")
    @Mapping(source = "driver.driverLicense", target = "driverLicense")
    @Mapping(source = "driver.identityCardFaceUp", target = "identityCardFaceUp")
    @Mapping(source = "driver.identityCardFacedown", target = "identityCardFaceDown")
    @Mapping(target = "vehicles", ignore = true) // Vehicles sẽ được set thủ công sau
    DriverInfoDTO toDriverInfoDTO(User user, UserDetail userDetail, Driver driver);

    @Mapping(source = "vehicle.vehicleID", target = "vehicleId")
    @Mapping(source = "vehicle.vehicleType", target = "vehicleType")
    @Mapping(source = "vehicle.vehicleImage", target = "vehicleImage")
    @Mapping(source = "vehicle.carregistration", target = "carRegistration")
    @Mapping(source = "vehicle.vehicleInspectionCertificate", target = "vehicleInspectionCertificate")
    @Mapping(source = "vehicle.carInsurance", target = "carInsurance")
    @Mapping(source = "vehicle.vehicleNumber", target = "vehicleNumber")
    @Mapping(source = "vehicle.numberOfSeats", target = "numberOfSeats")
    @Mapping(source = "vehicle.vehicleColor", target = "vehicleColor")
    @Mapping(source = "vehicle.vehicleBrand", target = "vehicleBrand")
    @Mapping(source = "vehicle.status", target = "status")
    DriverVehicleDTO toDriverVehicleDTO(Vehicle vehicle);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "driver.identityCardFaceUp", target = "identityCardFaceUp")
    @Mapping(source = "driver.identityCardFacedown", target = "identityCardFaceDown")
    @Mapping(source = "driver.driverLicense", target = "driverLicense")
    @Mapping(source = "vehicle", target = "vehicle")
    ConfirmingVehicleDTO toConfirmingVehicleDTO(User user, UserDetail userDetail, Driver driver, Vehicle vehicle);
}
