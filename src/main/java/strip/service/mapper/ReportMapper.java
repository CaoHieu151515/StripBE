package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.Report;
import strip.domain.User;
import strip.service.dto.ReportDTO;
import strip.service.dto.UserDTO;

/**
 * Mapper for the entity {@link Report} and its DTO {@link ReportDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportMapper extends EntityMapper<ReportDTO, Report> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    ReportDTO toDto(Report s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
