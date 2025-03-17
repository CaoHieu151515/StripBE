package strip.service.mapper;

import static strip.domain.RequestTripAsserts.*;
import static strip.domain.RequestTripTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RequestTripMapperTest {

    private RequestTripMapper requestTripMapper;

    @BeforeEach
    void setUp() {
        requestTripMapper = new RequestTripMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getRequestTripSample1();
        var actual = requestTripMapper.toEntity(requestTripMapper.toDto(expected));
        assertRequestTripAllPropertiesEquals(expected, actual);
    }
}
