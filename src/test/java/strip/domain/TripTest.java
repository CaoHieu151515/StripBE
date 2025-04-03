package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.FeedbackTestSamples.*;
import static strip.domain.RatingTestSamples.*;
import static strip.domain.ReportTestSamples.*;
import static strip.domain.RequestTripTestSamples.*;
import static strip.domain.TripStopLocationTestSamples.*;
import static strip.domain.TripTestSamples.*;
import static strip.domain.VehicleTestSamples.*;

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
    void vehicleTest() {
        Trip trip = getTripRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        trip.setVehicle(vehicleBack);
        assertThat(trip.getVehicle()).isEqualTo(vehicleBack);

        trip.vehicle(null);
        assertThat(trip.getVehicle()).isNull();
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
    void requestTripTest() {
        Trip trip = getTripRandomSampleGenerator();
        RequestTrip requestTripBack = getRequestTripRandomSampleGenerator();

        trip.addRequestTrip(requestTripBack);
        assertThat(trip.getRequestTrips()).containsOnly(requestTripBack);
        assertThat(requestTripBack.getTrip()).isEqualTo(trip);

        trip.removeRequestTrip(requestTripBack);
        assertThat(trip.getRequestTrips()).doesNotContain(requestTripBack);
        assertThat(requestTripBack.getTrip()).isNull();

        trip.requestTrips(new HashSet<>(Set.of(requestTripBack)));
        assertThat(trip.getRequestTrips()).containsOnly(requestTripBack);
        assertThat(requestTripBack.getTrip()).isEqualTo(trip);

        trip.setRequestTrips(new HashSet<>());
        assertThat(trip.getRequestTrips()).doesNotContain(requestTripBack);
        assertThat(requestTripBack.getTrip()).isNull();
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
    void reportTest() {
        Trip trip = getTripRandomSampleGenerator();
        Report reportBack = getReportRandomSampleGenerator();

        trip.addReport(reportBack);
        assertThat(trip.getReports()).containsOnly(reportBack);
        assertThat(reportBack.getTrip()).isEqualTo(trip);

        trip.removeReport(reportBack);
        assertThat(trip.getReports()).doesNotContain(reportBack);
        assertThat(reportBack.getTrip()).isNull();

        trip.reports(new HashSet<>(Set.of(reportBack)));
        assertThat(trip.getReports()).containsOnly(reportBack);
        assertThat(reportBack.getTrip()).isEqualTo(trip);

        trip.setReports(new HashSet<>());
        assertThat(trip.getReports()).doesNotContain(reportBack);
        assertThat(reportBack.getTrip()).isNull();
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
