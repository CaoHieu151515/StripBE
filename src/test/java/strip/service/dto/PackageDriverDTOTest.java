package strip.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class PackageDriverDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageDriverDTO.class);
        PackageDriverDTO packageDriverDTO1 = new PackageDriverDTO();
        packageDriverDTO1.setId(1L);
        PackageDriverDTO packageDriverDTO2 = new PackageDriverDTO();
        assertThat(packageDriverDTO1).isNotEqualTo(packageDriverDTO2);
        packageDriverDTO2.setId(packageDriverDTO1.getId());
        assertThat(packageDriverDTO1).isEqualTo(packageDriverDTO2);
        packageDriverDTO2.setId(2L);
        assertThat(packageDriverDTO1).isNotEqualTo(packageDriverDTO2);
        packageDriverDTO1.setId(null);
        assertThat(packageDriverDTO1).isNotEqualTo(packageDriverDTO2);
    }
}
