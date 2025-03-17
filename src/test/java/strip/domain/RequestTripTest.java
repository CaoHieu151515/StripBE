package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.RequestTripTestSamples.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class RequestTripTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestTrip.class);
        RequestTrip requestTrip1 = getRequestTripSample1();
        RequestTrip requestTrip2 = new RequestTrip();
        assertThat(requestTrip1).isNotEqualTo(requestTrip2);

        requestTrip2.setId(requestTrip1.getId());
        assertThat(requestTrip1).isEqualTo(requestTrip2);

        requestTrip2 = getRequestTripSample2();
        assertThat(requestTrip1).isNotEqualTo(requestTrip2);
    }

    @Test
    void tripTest() {
        RequestTrip requestTrip = getRequestTripRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        requestTrip.setTrip(tripBack);
        assertThat(requestTrip.getTrip()).isEqualTo(tripBack);

        requestTrip.trip(null);
        assertThat(requestTrip.getTrip()).isNull();
    }
}
