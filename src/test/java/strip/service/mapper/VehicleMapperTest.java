package strip.service.mapper;

import static strip.domain.VehicleAsserts.*;
import static strip.domain.VehicleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VehicleMapperTest {

    private VehicleMapper vehicleMapper;

    @BeforeEach
    void setUp() {
        vehicleMapper = new VehicleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getVehicleSample1();
        var actual = vehicleMapper.toEntity(vehicleMapper.toDto(expected));
        assertVehicleAllPropertiesEquals(expected, actual);
    }
}
