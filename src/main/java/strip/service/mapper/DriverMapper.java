package strip.service.mapper;

import org.mapstruct.*;
import strip.config.IgnoreUnmappedConfig;
import strip.domain.Driver;
import strip.domain.User;
import strip.service.dto.DriverDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Driver} and its DTO {@link DriverDTO}.
 */
@Mapper(componentModel = "spring", config = IgnoreUnmappedConfig.class)
public interface DriverMapper extends EntityMapper<DriverDTO, Driver> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    DriverDTO toDto(Driver s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
