package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.PassengerTestSamples.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class PassengerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Passenger.class);
        Passenger passenger1 = getPassengerSample1();
        Passenger passenger2 = new Passenger();
        assertThat(passenger1).isNotEqualTo(passenger2);

        passenger2.setId(passenger1.getId());
        assertThat(passenger1).isEqualTo(passenger2);

        passenger2 = getPassengerSample2();
        assertThat(passenger1).isNotEqualTo(passenger2);
    }

    @Test
    void tripTest() {
        Passenger passenger = getPassengerRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        passenger.setTrip(tripBack);
        assertThat(passenger.getTrip()).isEqualTo(tripBack);

        passenger.trip(null);
        assertThat(passenger.getTrip()).isNull();
    }
}
