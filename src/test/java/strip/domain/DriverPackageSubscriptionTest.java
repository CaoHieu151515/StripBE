package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverPackageSubscriptionTestSamples.*;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.PackageDriverTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class DriverPackageSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverPackageSubscription.class);
        DriverPackageSubscription driverPackageSubscription1 = getDriverPackageSubscriptionSample1();
        DriverPackageSubscription driverPackageSubscription2 = new DriverPackageSubscription();
        assertThat(driverPackageSubscription1).isNotEqualTo(driverPackageSubscription2);

        driverPackageSubscription2.setId(driverPackageSubscription1.getId());
        assertThat(driverPackageSubscription1).isEqualTo(driverPackageSubscription2);

        driverPackageSubscription2 = getDriverPackageSubscriptionSample2();
        assertThat(driverPackageSubscription1).isNotEqualTo(driverPackageSubscription2);
    }

    @Test
    void driverTest() {
        DriverPackageSubscription driverPackageSubscription = getDriverPackageSubscriptionRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        driverPackageSubscription.setDriver(driverBack);
        assertThat(driverPackageSubscription.getDriver()).isEqualTo(driverBack);

        driverPackageSubscription.driver(null);
        assertThat(driverPackageSubscription.getDriver()).isNull();
    }

    @Test
    void packageDriverTest() {
        DriverPackageSubscription driverPackageSubscription = getDriverPackageSubscriptionRandomSampleGenerator();
        PackageDriver packageDriverBack = getPackageDriverRandomSampleGenerator();

        driverPackageSubscription.setPackageDriver(packageDriverBack);
        assertThat(driverPackageSubscription.getPackageDriver()).isEqualTo(packageDriverBack);

        driverPackageSubscription.packageDriver(null);
        assertThat(driverPackageSubscription.getPackageDriver()).isNull();
    }
}
