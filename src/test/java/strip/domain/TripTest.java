package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.FeedbackTestSamples.*;
import static strip.domain.PassengerTestSamples.*;
import static strip.domain.RatingTestSamples.*;
import static strip.domain.TripStopLocationTestSamples.*;
import static strip.domain.TripTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class TripTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trip.class);
        Trip trip1 = getTripSample1();
        Trip trip2 = new Trip();
        assertThat(trip1).isNotEqualTo(trip2);

        trip2.setId(trip1.getId());
        assertThat(trip1).isEqualTo(trip2);

        trip2 = getTripSample2();
        assertThat(trip1).isNotEqualTo(trip2);
    }

    @Test
    void driverTest() {
        Trip trip = getTripRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        trip.setDriver(driverBack);
        assertThat(trip.getDriver()).isEqualTo(driverBack);

        trip.driver(null);
        assertThat(trip.getDriver()).isNull();
    }

    @Test
    void passengerTest() {
        Trip trip = getTripRandomSampleGenerator();
        Passenger passengerBack = getPassengerRandomSampleGenerator();

        trip.addPassenger(passengerBack);
        assertThat(trip.getPassengers()).containsOnly(passengerBack);
        assertThat(passengerBack.getTrip()).isEqualTo(trip);

        trip.removePassenger(passengerBack);
        assertThat(trip.getPassengers()).doesNotContain(passengerBack);
        assertThat(passengerBack.getTrip()).isNull();

        trip.passengers(new HashSet<>(Set.of(passengerBack)));
        assertThat(trip.getPassengers()).containsOnly(passengerBack);
        assertThat(passengerBack.getTrip()).isEqualTo(trip);

        trip.setPassengers(new HashSet<>());
        assertThat(trip.getPassengers()).doesNotContain(passengerBack);
        assertThat(passengerBack.getTrip()).isNull();
    }

    @Test
    void tripStopLocationTest() {
        Trip trip = getTripRandomSampleGenerator();
        TripStopLocation tripStopLocationBack = getTripStopLocationRandomSampleGenerator();

        trip.addTripStopLocation(tripStopLocationBack);
        assertThat(trip.getTripStopLocations()).containsOnly(tripStopLocationBack);
        assertThat(tripStopLocationBack.getTrip()).isEqualTo(trip);

        trip.removeTripStopLocation(tripStopLocationBack);
        assertThat(trip.getTripStopLocations()).doesNotContain(tripStopLocationBack);
        assertThat(tripStopLocationBack.getTrip()).isNull();

        trip.tripStopLocations(new HashSet<>(Set.of(tripStopLocationBack)));
        assertThat(trip.getTripStopLocations()).containsOnly(tripStopLocationBack);
        assertThat(tripStopLocationBack.getTrip()).isEqualTo(trip);

        trip.setTripStopLocations(new HashSet<>());
        assertThat(trip.getTripStopLocations()).doesNotContain(tripStopLocationBack);
        assertThat(tripStopLocationBack.getTrip()).isNull();
    }

    @Test
    void feedbackTest() {
        Trip trip = getTripRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        trip.addFeedback(feedbackBack);
        assertThat(trip.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getTrip()).isEqualTo(trip);

        trip.removeFeedback(feedbackBack);
        assertThat(trip.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getTrip()).isNull();

        trip.feedbacks(new HashSet<>(Set.of(feedbackBack)));
        assertThat(trip.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getTrip()).isEqualTo(trip);

        trip.setFeedbacks(new HashSet<>());
        assertThat(trip.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getTrip()).isNull();
    }

    @Test
    void ratingTest() {
        Trip trip = getTripRandomSampleGenerator();
        Rating ratingBack = getRatingRandomSampleGenerator();

        trip.addRating(ratingBack);
        assertThat(trip.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getTrip()).isEqualTo(trip);

        trip.removeRating(ratingBack);
        assertThat(trip.getRatings()).doesNotContain(ratingBack);
        assertThat(ratingBack.getTrip()).isNull();

        trip.ratings(new HashSet<>(Set.of(ratingBack)));
        assertThat(trip.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getTrip()).isEqualTo(trip);

        trip.setRatings(new HashSet<>());
        assertThat(trip.getRatings()).doesNotContain(ratingBack);
        assertThat(ratingBack.getTrip()).isNull();
    }
}
