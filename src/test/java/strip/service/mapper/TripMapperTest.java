package strip.service.mapper;

import static strip.domain.TripAsserts.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripMapperTest {

    private TripMapper tripMapper;

    @BeforeEach
    void setUp() {
        tripMapper = new TripMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTripSample1();
        var actual = tripMapper.toEntity(tripMapper.toDto(expected));
        assertTripAllPropertiesEquals(expected, actual);
    }
}
