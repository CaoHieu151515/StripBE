package strip.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import strip.domain.Driver;
import strip.domain.DriverPointHistory;
import strip.domain.Report;
import strip.domain.Trip;
import strip.domain.TripStopLocation;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.Vehicle;
import strip.domain.WalletDeposit;
import strip.service.dto.ConfirmingVehicleDTO;
import strip.service.dto.DriverDTO;
import strip.service.dto.DriverPointHistoryListDTO;
import strip.service.dto.DriverPointHistoryRefundDTO;
import strip.service.dto.DriverRawDTO;
import strip.service.dto.ReportCusDTO;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripDTO;
import strip.service.dto.TripDetailDTO;
import strip.service.dto.TripListDTO;
import strip.service.dto.TripStopLocationDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;
import strip.service.dto.VehicleRawDTO;
import strip.service.dto.WithdrawalRequestManageDTO;

@Mapper(componentModel = "spring")
public interface UsermanageMapper {
    @Mapping(source = "user.login", target = "username")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.activated", target = "active")
    @Mapping(source = "userDetail.appUserDetail", target = "userId")
    @Mapping(source = "userDetail.gender", target = "gender")
    @Mapping(source = "userDetail.phone", target = "phoneNumber")
    @Mapping(target = "roles", ignore = true)
    UsermanageDTO toDto(User user, UserDetail userDetail);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "username")
    @Mapping(source = "userDetail.userimage", target = "userImage")
    @Mapping(source = "driver.driverStatus", target = "driverStatus")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "userDetail.gender", target = "gender")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "userDetail.dob", target = "dob")
    @Mapping(source = "userDetail.address", target = "address")
    @Mapping(source = "driver.identityCardFaceUp", target = "identityCardFaceUp")
    @Mapping(source = "driver.identityCardFacedown", target = "identityCardFaceDown")
    UsermanageDetailsDTO toDto(User user, UserDetail userDetail, Driver driver);

    @Mapping(source = "userWallet.user.firstName", target = "firstName")
    @Mapping(source = "userWallet.user.lastName", target = "lastName")
    @Mapping(source = "userWallet.user.email", target = "email")
    @Mapping(source = "id", target = "depositId")
    WithdrawalRequestManageDTO toDtoWithdrawalRequestManageDTO(WalletDeposit deposit);

    List<WithdrawalRequestManageDTO> toWithdrawalRequestManageDTOs(List<WalletDeposit> deposits); // 👈 Quan trọng!

    @Mapping(target = "tripID", source = "tripID")
    @Mapping(target = "tripImgUrl", ignore = true)
    @Mapping(target = "driverName", ignore = true)
    @Mapping(target = "driverPhone", ignore = true)
    @Mapping(target = "totalTime", source = "totalTime")
    @Mapping(target = "totalDistance", source = "totalDistance")
    @Mapping(target = "vehicleID", source = "vehicle.vehicleID")
    @Mapping(target = "vehicleType", source = "vehicle.vehicleType")
    @Mapping(target = "vehicleNumber", source = "vehicle.vehicleNumber")
    @Mapping(target = "numberOfSeats", source = "vehicle.numberOfSeats")
    @Mapping(target = "vehicleColor", source = "vehicle.vehicleColor")
    @Mapping(target = "vehicleBrand", source = "vehicle.vehicleBrand")
    @Mapping(target = "vehicleImageUrl", ignore = true)
    @Mapping(target = "stopLocations", ignore = true)
    TripCusDTO toTripCusDTO(Trip trip);

    @Mapping(target = "trip", ignore = true) // chỉ map tripID nếu cần
    TripStopLocationDTO toTripStopLocationDTO(TripStopLocation stopLocation);

    DriverPointHistoryRefundDTO toDto(DriverPointHistory entity);

    @Mapping(source = "trip.tripID", target = "tripId")
    @Mapping(target = "userId", ignore = true)
    @Mapping(source = "driver.driverID", target = "driverId")
    ReportCusDTO toReportCusDTO(Report report);

    @Mapping(source = "tripID", target = "stripID")
    @Mapping(source = "startDate", target = "startDay")
    @Mapping(source = "endDate", target = "endDay")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endlocation")
    @Mapping(source = "pricePerSeat", target = "price")
    @Mapping(source = "tripStatus", target = "status")
    @Mapping(source = "totalTime", target = "totalTime")
    TripListDTO toTripListDTO(Trip trip);

    TripDTO toTripDTO(Trip trip);

    DriverDTO toDriverDTO(Driver driver);

    @Mapping(target = "carInsuranceUrl", ignore = true)
    @Mapping(target = "carregistrationUrl", ignore = true)
    @Mapping(target = "vehicleImageUrl", ignore = true)
    @Mapping(target = "vehicleInspectionCertificateUrl", ignore = true)
    ConfirmingVehicleDTO toConfirmingVehicleDTO(Vehicle vehicle);

    @Mapping(source = "tripID", target = "tripID")
    @Mapping(source = "startLocation", target = "startLocation")
    @Mapping(source = "endLocation", target = "endLocation")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "condition", target = "condition")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "pricePerSeat", target = "pricePerSeat")
    @Mapping(source = "maxSeat", target = "maxSeat")
    @Mapping(source = "currentSeat", target = "currentSeat")
    @Mapping(source = "tripStatus", target = "tripStatus")
    @Mapping(source = "cancelReason", target = "cancelReason")
    @Mapping(source = "totalTime", target = "totalTime")
    @Mapping(source = "totalDistance", target = "totalDistance")
    @Mapping(target = "tripImgUrl", ignore = true)
    @Mapping(target = "driver", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "stoplocation", ignore = true)
    TripDetailDTO toTripDetailDTO(Trip trip);

    @Mapping(target = "vehicleImageUrl", ignore = true)
    @Mapping(target = "carregistrationUrl", ignore = true)
    @Mapping(target = "vehicleInspectionCertificateUrl", ignore = true)
    @Mapping(target = "carInsuranceUrl", ignore = true)
    VehicleRawDTO toRawDTO(Vehicle vehicle);

    @Mapping(source = "driver.driverID", target = "driverId")
    @Mapping(source = "driver.user.firstName", target = "firstName")
    @Mapping(source = "driver.user.lastName", target = "lastName")
    @Mapping(source = "driver.user.email", target = "email")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "userDetail.address", target = "address")
    @Mapping(source = "userDetail.gender", target = "gender")
    @Mapping(source = "userDetail.dob", target = "dob")
    @Mapping(target = "driverLicenseUrl", ignore = true)
    @Mapping(target = "identityCardFaceUpUrl", ignore = true)
    @Mapping(target = "identityCardFaceDownUrl", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "rating", ignore = true)
    DriverRawDTO toRawDTO(Driver driver, UserDetail userDetail);

    @Mapping(target = "userName", ignore = true)
    DriverPointHistoryListDTO toListDtoBase(DriverPointHistory entity);
}
