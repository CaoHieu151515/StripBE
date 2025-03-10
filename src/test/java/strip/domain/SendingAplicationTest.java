package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.SendingAplicationTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class SendingAplicationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SendingAplication.class);
        SendingAplication sendingAplication1 = getSendingAplicationSample1();
        SendingAplication sendingAplication2 = new SendingAplication();
        assertThat(sendingAplication1).isNotEqualTo(sendingAplication2);

        sendingAplication2.setId(sendingAplication1.getId());
        assertThat(sendingAplication1).isEqualTo(sendingAplication2);

        sendingAplication2 = getSendingAplicationSample2();
        assertThat(sendingAplication1).isNotEqualTo(sendingAplication2);
    }
}
