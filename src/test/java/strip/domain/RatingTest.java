package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.RatingTestSamples.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class RatingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Rating.class);
        Rating rating1 = getRatingSample1();
        Rating rating2 = new Rating();
        assertThat(rating1).isNotEqualTo(rating2);

        rating2.setId(rating1.getId());
        assertThat(rating1).isEqualTo(rating2);

        rating2 = getRatingSample2();
        assertThat(rating1).isNotEqualTo(rating2);
    }

    @Test
    void tripTest() {
        Rating rating = getRatingRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        rating.setTrip(tripBack);
        assertThat(rating.getTrip()).isEqualTo(tripBack);

        rating.trip(null);
        assertThat(rating.getTrip()).isNull();
    }

    @Test
    void driverTest() {
        Rating rating = getRatingRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        rating.setDriver(driverBack);
        assertThat(rating.getDriver()).isEqualTo(driverBack);

        rating.driver(null);
        assertThat(rating.getDriver()).isNull();
    }
}
