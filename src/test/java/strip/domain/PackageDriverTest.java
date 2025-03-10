package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.PackageDriverTestSamples.*;
import static strip.domain.PaymentTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class PackageDriverTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PackageDriver.class);
        PackageDriver packageDriver1 = getPackageDriverSample1();
        PackageDriver packageDriver2 = new PackageDriver();
        assertThat(packageDriver1).isNotEqualTo(packageDriver2);

        packageDriver2.setId(packageDriver1.getId());
        assertThat(packageDriver1).isEqualTo(packageDriver2);

        packageDriver2 = getPackageDriverSample2();
        assertThat(packageDriver1).isNotEqualTo(packageDriver2);
    }

    @Test
    void paymentTest() {
        PackageDriver packageDriver = getPackageDriverRandomSampleGenerator();
        Payment paymentBack = getPaymentRandomSampleGenerator();

        packageDriver.addPayment(paymentBack);
        assertThat(packageDriver.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getPackageDriver()).isEqualTo(packageDriver);

        packageDriver.removePayment(paymentBack);
        assertThat(packageDriver.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getPackageDriver()).isNull();

        packageDriver.payments(new HashSet<>(Set.of(paymentBack)));
        assertThat(packageDriver.getPayments()).containsOnly(paymentBack);
        assertThat(paymentBack.getPackageDriver()).isEqualTo(packageDriver);

        packageDriver.setPayments(new HashSet<>());
        assertThat(packageDriver.getPayments()).doesNotContain(paymentBack);
        assertThat(paymentBack.getPackageDriver()).isNull();
    }
}
