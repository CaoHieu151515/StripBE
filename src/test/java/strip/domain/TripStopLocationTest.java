package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.TripStopLocationTestSamples.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class TripStopLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripStopLocation.class);
        TripStopLocation tripStopLocation1 = getTripStopLocationSample1();
        TripStopLocation tripStopLocation2 = new TripStopLocation();
        assertThat(tripStopLocation1).isNotEqualTo(tripStopLocation2);

        tripStopLocation2.setId(tripStopLocation1.getId());
        assertThat(tripStopLocation1).isEqualTo(tripStopLocation2);

        tripStopLocation2 = getTripStopLocationSample2();
        assertThat(tripStopLocation1).isNotEqualTo(tripStopLocation2);
    }

    @Test
    void tripTest() {
        TripStopLocation tripStopLocation = getTripStopLocationRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        tripStopLocation.setTrip(tripBack);
        assertThat(tripStopLocation.getTrip()).isEqualTo(tripBack);

        tripStopLocation.trip(null);
        assertThat(tripStopLocation.getTrip()).isNull();
    }
}
