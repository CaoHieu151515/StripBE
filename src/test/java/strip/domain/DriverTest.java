package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverPackageSubscriptionTestSamples.*;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.FeedbackTestSamples.*;
import static strip.domain.RatingTestSamples.*;
import static strip.domain.TripTestSamples.*;
import static strip.domain.VehicleTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class DriverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Driver.class);
        Driver driver1 = getDriverSample1();
        Driver driver2 = new Driver();
        assertThat(driver1).isNotEqualTo(driver2);

        driver2.setId(driver1.getId());
        assertThat(driver1).isEqualTo(driver2);

        driver2 = getDriverSample2();
        assertThat(driver1).isNotEqualTo(driver2);
    }

    @Test
    void vehicleTest() {
        Driver driver = getDriverRandomSampleGenerator();
        Vehicle vehicleBack = getVehicleRandomSampleGenerator();

        driver.addVehicle(vehicleBack);
        assertThat(driver.getVehicles()).containsOnly(vehicleBack);
        assertThat(vehicleBack.getDriver()).isEqualTo(driver);

        driver.removeVehicle(vehicleBack);
        assertThat(driver.getVehicles()).doesNotContain(vehicleBack);
        assertThat(vehicleBack.getDriver()).isNull();

        driver.vehicles(new HashSet<>(Set.of(vehicleBack)));
        assertThat(driver.getVehicles()).containsOnly(vehicleBack);
        assertThat(vehicleBack.getDriver()).isEqualTo(driver);

        driver.setVehicles(new HashSet<>());
        assertThat(driver.getVehicles()).doesNotContain(vehicleBack);
        assertThat(vehicleBack.getDriver()).isNull();
    }

    @Test
    void tripTest() {
        Driver driver = getDriverRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        driver.addTrip(tripBack);
        assertThat(driver.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getDriver()).isEqualTo(driver);

        driver.removeTrip(tripBack);
        assertThat(driver.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getDriver()).isNull();

        driver.trips(new HashSet<>(Set.of(tripBack)));
        assertThat(driver.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getDriver()).isEqualTo(driver);

        driver.setTrips(new HashSet<>());
        assertThat(driver.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getDriver()).isNull();
    }

    @Test
    void feedbackTest() {
        Driver driver = getDriverRandomSampleGenerator();
        Feedback feedbackBack = getFeedbackRandomSampleGenerator();

        driver.addFeedback(feedbackBack);
        assertThat(driver.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getDriver()).isEqualTo(driver);

        driver.removeFeedback(feedbackBack);
        assertThat(driver.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getDriver()).isNull();

        driver.feedbacks(new HashSet<>(Set.of(feedbackBack)));
        assertThat(driver.getFeedbacks()).containsOnly(feedbackBack);
        assertThat(feedbackBack.getDriver()).isEqualTo(driver);

        driver.setFeedbacks(new HashSet<>());
        assertThat(driver.getFeedbacks()).doesNotContain(feedbackBack);
        assertThat(feedbackBack.getDriver()).isNull();
    }

    @Test
    void ratingTest() {
        Driver driver = getDriverRandomSampleGenerator();
        Rating ratingBack = getRatingRandomSampleGenerator();

        driver.addRating(ratingBack);
        assertThat(driver.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getDriver()).isEqualTo(driver);

        driver.removeRating(ratingBack);
        assertThat(driver.getRatings()).doesNotContain(ratingBack);
        assertThat(ratingBack.getDriver()).isNull();

        driver.ratings(new HashSet<>(Set.of(ratingBack)));
        assertThat(driver.getRatings()).containsOnly(ratingBack);
        assertThat(ratingBack.getDriver()).isEqualTo(driver);

        driver.setRatings(new HashSet<>());
        assertThat(driver.getRatings()).doesNotContain(ratingBack);
        assertThat(ratingBack.getDriver()).isNull();
    }

    @Test
    void driverPackageSubscriptionTest() {
        Driver driver = getDriverRandomSampleGenerator();
        DriverPackageSubscription driverPackageSubscriptionBack = getDriverPackageSubscriptionRandomSampleGenerator();

        driver.addDriverPackageSubscription(driverPackageSubscriptionBack);
        assertThat(driver.getDriverPackageSubscriptions()).containsOnly(driverPackageSubscriptionBack);
        assertThat(driverPackageSubscriptionBack.getDriver()).isEqualTo(driver);

        driver.removeDriverPackageSubscription(driverPackageSubscriptionBack);
        assertThat(driver.getDriverPackageSubscriptions()).doesNotContain(driverPackageSubscriptionBack);
        assertThat(driverPackageSubscriptionBack.getDriver()).isNull();

        driver.driverPackageSubscriptions(new HashSet<>(Set.of(driverPackageSubscriptionBack)));
        assertThat(driver.getDriverPackageSubscriptions()).containsOnly(driverPackageSubscriptionBack);
        assertThat(driverPackageSubscriptionBack.getDriver()).isEqualTo(driver);

        driver.setDriverPackageSubscriptions(new HashSet<>());
        assertThat(driver.getDriverPackageSubscriptions()).doesNotContain(driverPackageSubscriptionBack);
        assertThat(driverPackageSubscriptionBack.getDriver()).isNull();
    }
}
