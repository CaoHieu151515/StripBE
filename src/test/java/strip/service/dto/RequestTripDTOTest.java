package strip.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class RequestTripDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RequestTripDTO.class);
        RequestTripDTO requestTripDTO1 = new RequestTripDTO();
        requestTripDTO1.setId(1L);
        RequestTripDTO requestTripDTO2 = new RequestTripDTO();
        assertThat(requestTripDTO1).isNotEqualTo(requestTripDTO2);
        requestTripDTO2.setId(requestTripDTO1.getId());
        assertThat(requestTripDTO1).isEqualTo(requestTripDTO2);
        requestTripDTO2.setId(2L);
        assertThat(requestTripDTO1).isNotEqualTo(requestTripDTO2);
        requestTripDTO1.setId(null);
        assertThat(requestTripDTO1).isNotEqualTo(requestTripDTO2);
    }
}
