package strip.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import strip.domain.Driver;
import strip.domain.User;
import strip.domain.UserDetail;
import strip.service.dto.UsermanageDTO;
import strip.service.dto.UsermanageDetailsDTO;

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
    UsermanageDTO toDto(User user, UserDetail userDetail);

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
}
