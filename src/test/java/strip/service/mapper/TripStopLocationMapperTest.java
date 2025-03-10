package strip.service.mapper;

import static strip.domain.TripStopLocationAsserts.*;
import static strip.domain.TripStopLocationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TripStopLocationMapperTest {

    private TripStopLocationMapper tripStopLocationMapper;

    @BeforeEach
    void setUp() {
        tripStopLocationMapper = new TripStopLocationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTripStopLocationSample1();
        var actual = tripStopLocationMapper.toEntity(tripStopLocationMapper.toDto(expected));
        assertTripStopLocationAllPropertiesEquals(expected, actual);
    }
}
