package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverPointHistoryTestSamples.*;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.UserDetailTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class DriverPointHistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DriverPointHistory.class);
        DriverPointHistory driverPointHistory1 = getDriverPointHistorySample1();
        DriverPointHistory driverPointHistory2 = new DriverPointHistory();
        assertThat(driverPointHistory1).isNotEqualTo(driverPointHistory2);

        driverPointHistory2.setId(driverPointHistory1.getId());
        assertThat(driverPointHistory1).isEqualTo(driverPointHistory2);

        driverPointHistory2 = getDriverPointHistorySample2();
        assertThat(driverPointHistory1).isNotEqualTo(driverPointHistory2);
    }

    @Test
    void driverTest() {
        DriverPointHistory driverPointHistory = getDriverPointHistoryRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        driverPointHistory.setDriver(driverBack);
        assertThat(driverPointHistory.getDriver()).isEqualTo(driverBack);

        driverPointHistory.driver(null);
        assertThat(driverPointHistory.getDriver()).isNull();
    }

    @Test
    void userDetailTest() {
        DriverPointHistory driverPointHistory = getDriverPointHistoryRandomSampleGenerator();
        UserDetail userDetailBack = getUserDetailRandomSampleGenerator();

        driverPointHistory.setUserDetail(userDetailBack);
        assertThat(driverPointHistory.getUserDetail()).isEqualTo(userDetailBack);

        driverPointHistory.userDetail(null);
        assertThat(driverPointHistory.getUserDetail()).isNull();
    }
}
