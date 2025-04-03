package strip.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static strip.domain.DriverTestSamples.*;
import static strip.domain.ReportTestSamples.*;
import static strip.domain.TripTestSamples.*;

import org.junit.jupiter.api.Test;
import strip.web.rest.TestUtil;

class ReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Report.class);
        Report report1 = getReportSample1();
        Report report2 = new Report();
        assertThat(report1).isNotEqualTo(report2);

        report2.setId(report1.getId());
        assertThat(report1).isEqualTo(report2);

        report2 = getReportSample2();
        assertThat(report1).isNotEqualTo(report2);
    }

    @Test
    void tripTest() {
        Report report = getReportRandomSampleGenerator();
        Trip tripBack = getTripRandomSampleGenerator();

        report.setTrip(tripBack);
        assertThat(report.getTrip()).isEqualTo(tripBack);

        report.trip(null);
        assertThat(report.getTrip()).isNull();
    }

    @Test
    void driverTest() {
        Report report = getReportRandomSampleGenerator();
        Driver driverBack = getDriverRandomSampleGenerator();

        report.setDriver(driverBack);
        assertThat(report.getDriver()).isEqualTo(driverBack);

        report.driver(null);
        assertThat(report.getDriver()).isNull();
    }
}
