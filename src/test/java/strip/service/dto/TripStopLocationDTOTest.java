package strip.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class TripStopLocationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripStopLocationDTO.class);
        TripStopLocationDTO tripStopLocationDTO1 = new TripStopLocationDTO();
        tripStopLocationDTO1.setId(1L);
        TripStopLocationDTO tripStopLocationDTO2 = new TripStopLocationDTO();
        assertThat(tripStopLocationDTO1).isNotEqualTo(tripStopLocationDTO2);
        tripStopLocationDTO2.setId(tripStopLocationDTO1.getId());
        assertThat(tripStopLocationDTO1).isEqualTo(tripStopLocationDTO2);
        tripStopLocationDTO2.setId(2L);
        assertThat(tripStopLocationDTO1).isNotEqualTo(tripStopLocationDTO2);
        tripStopLocationDTO1.setId(null);
        assertThat(tripStopLocationDTO1).isNotEqualTo(tripStopLocationDTO2);
    }
}
