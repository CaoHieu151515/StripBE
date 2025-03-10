package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.PackageDriver;
import strip.domain.Payment;
import strip.domain.User;
import strip.service.dto.PackageDriverDTO;
import strip.service.dto.PaymentDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "packageDriver", source = "packageDriver", qualifiedByName = "packageDriverId")
    PaymentDTO toDto(Payment s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("packageDriverId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PackageDriverDTO toDtoPackageDriverId(PackageDriver packageDriver);
}
