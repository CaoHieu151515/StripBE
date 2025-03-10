package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.TripTestSamples.*;
import static strip.domain.VehicleTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class VehicleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vehicle.class);
        Vehicle vehicle1 = getVehicleSample1();
        Vehicle vehicle2 = new Vehicle();
        assertThat(vehicle1).isNotEqualTo(vehicle2);

        vehicle2.setId(vehicle1.getId());
        assertThat(vehicle1).isEqualTo(vehicle2);

        vehicle2 = getVehicleSample2();
        assertThat(vehicle1).isNotEqualTo(vehicle2);
    }

    @Test
    void driverTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        vehicle.setDriver(driverBack);
        assertThat(vehicle.getDriver()).isEqualTo(driverBack);

        vehicle.driver(null);
        assertThat(vehicle.getDriver()).isNull();
    }

    @Test
    void tripTest() {
        Vehicle vehicle = getVehicleRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        vehicle.addTrip(tripBack);
        assertThat(vehicle.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getVehicle()).isEqualTo(vehicle);

        vehicle.removeTrip(tripBack);
        assertThat(vehicle.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getVehicle()).isNull();

        vehicle.trips(new HashSet<>(Set.of(tripBack)));
        assertThat(vehicle.getTrips()).containsOnly(tripBack);
        assertThat(tripBack.getVehicle()).isEqualTo(vehicle);

        vehicle.setTrips(new HashSet<>());
        assertThat(vehicle.getTrips()).doesNotContain(tripBack);
        assertThat(tripBack.getVehicle()).isNull();
    }
}
