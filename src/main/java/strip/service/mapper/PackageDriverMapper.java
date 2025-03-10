package strip.service.mapper;

import org.mapstruct.*;
import strip.domain.PackageDriver;
import strip.service.dto.PackageDriverDTO;

/**
 * Mapper for the entity {@link PackageDriver} and its DTO {@link PackageDriverDTO}.
 */
@Mapper(componentModel = "spring")
public interface PackageDriverMapper extends EntityMapper<PackageDriverDTO, PackageDriver> {}
