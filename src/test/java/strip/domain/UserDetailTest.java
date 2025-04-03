package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverPointHistoryTestSamples.*;
import static strip.domain.UserDetailTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class UserDetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserDetail.class);
        UserDetail userDetail1 = getUserDetailSample1();
        UserDetail userDetail2 = new UserDetail();
        assertThat(userDetail1).isNotEqualTo(userDetail2);

        userDetail2.setId(userDetail1.getId());
        assertThat(userDetail1).isEqualTo(userDetail2);

        userDetail2 = getUserDetailSample2();
        assertThat(userDetail1).isNotEqualTo(userDetail2);
    }

    @Test
    void driverPointHistoryTest() {
        UserDetail userDetail = getUserDetailRandomSampleGenerator();
        DriverPointHistory driverPointHistoryBack = getDriverPointHistoryRandomSampleGenerator();

        userDetail.addDriverPointHistory(driverPointHistoryBack);
        assertThat(userDetail.getDriverPointHistories()).containsOnly(driverPointHistoryBack);
        assertThat(driverPointHistoryBack.getUserDetail()).isEqualTo(userDetail);

        userDetail.removeDriverPointHistory(driverPointHistoryBack);
        assertThat(userDetail.getDriverPointHistories()).doesNotContain(driverPointHistoryBack);
        assertThat(driverPointHistoryBack.getUserDetail()).isNull();

        userDetail.driverPointHistories(new HashSet<>(Set.of(driverPointHistoryBack)));
        assertThat(userDetail.getDriverPointHistories()).containsOnly(driverPointHistoryBack);
        assertThat(driverPointHistoryBack.getUserDetail()).isEqualTo(userDetail);

        userDetail.setDriverPointHistories(new HashSet<>());
        assertThat(userDetail.getDriverPointHistories()).doesNotContain(driverPointHistoryBack);
        assertThat(driverPointHistoryBack.getUserDetail()).isNull();
    }
}
