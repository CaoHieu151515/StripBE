package strip.service.mapper;

import static strip.domain.PackageDriverAsserts.*;
import static strip.domain.PackageDriverTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PackageDriverMapperTest {

    private PackageDriverMapper packageDriverMapper;

    @BeforeEach
    void setUp() {
        packageDriverMapper = new PackageDriverMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPackageDriverSample1();
        var actual = packageDriverMapper.toEntity(packageDriverMapper.toDto(expected));
        assertPackageDriverAllPropertiesEquals(expected, actual);
    }
}
