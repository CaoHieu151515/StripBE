package strip.service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import strip.domain.Driver;
import strip.domain.Feedback;
import strip.domain.Trip;
import strip.domain.TripStopLocation;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.domain.WalletDeposit;
import strip.service.dto.FeedbackCusDTO;
import strip.service.dto.TripCusDTO;
import strip.service.dto.TripStopLocationDTO;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;
import strip.service.dto.WithdrawalRequestManageDTO;

@Mapper(componentModel = "spring")
public interface UsermanageMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "username")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.activated", target = "active") // Thêm ánh xạ active
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

    @Mapping(target = "tripId", source = "trip.tripID")
    @Mapping(target = "driverId", source = "driver.driverID")
    @Mapping(target = "username", source = "user.login")
    FeedbackCusDTO toFeedbackCusDTO(Feedback feedback);
}
