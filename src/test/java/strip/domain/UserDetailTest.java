package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.UserDetailTestSamples.*;

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
}
