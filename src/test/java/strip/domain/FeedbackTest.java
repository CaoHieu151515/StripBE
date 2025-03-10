package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.FeedbackTestSamples.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class FeedbackTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedback.class);
        Feedback feedback1 = getFeedbackSample1();
        Feedback feedback2 = new Feedback();
        assertThat(feedback1).isNotEqualTo(feedback2);

        feedback2.setId(feedback1.getId());
        assertThat(feedback1).isEqualTo(feedback2);

        feedback2 = getFeedbackSample2();
        assertThat(feedback1).isNotEqualTo(feedback2);
    }

    @Test
    void tripTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        feedback.setTrip(tripBack);
        assertThat(feedback.getTrip()).isEqualTo(tripBack);

        feedback.trip(null);
        assertThat(feedback.getTrip()).isNull();
    }

    @Test
    void driverTest() {
        Feedback feedback = getFeedbackRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        feedback.setDriver(driverBack);
        assertThat(feedback.getDriver()).isEqualTo(driverBack);

        feedback.driver(null);
        assertThat(feedback.getDriver()).isNull();
    }
}
