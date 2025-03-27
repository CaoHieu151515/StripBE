package strip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to S Trip Be.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Trip trip = new Trip();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Trip getTrip() {
        return trip;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class Trip {

        private final Fee fee = new Fee();

        public Fee getFee() {
            return fee;
        }

        public static class Fee {

            private double driverCreate;
            private double driverDone;
            private double passengerApprove;

            public double getDriverCreate() {
                return driverCreate;
            }

            public void setDriverCreate(double driverCreate) {
                this.driverCreate = driverCreate;
            }

            public double getDriverDone() {
                return driverDone;
            }

            public void setDriverDone(double driverDone) {
                this.driverDone = driverDone;
            }

            public double getPassengerApprove() {
                return passengerApprove;
            }

            public void setPassengerApprove(double passengerApprove) {
                this.passengerApprove = passengerApprove;
            }
        }
    }
    // jhipster-needle-application-properties-property-class
}
