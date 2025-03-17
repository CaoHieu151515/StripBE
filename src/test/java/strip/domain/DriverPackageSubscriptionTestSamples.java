package strip.domain;

import java.util.UUID;

public class DriverPackageSubscriptionTestSamples {

    public static DriverPackageSubscription getDriverPackageSubscriptionSample1() {
        return new DriverPackageSubscription().id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static DriverPackageSubscription getDriverPackageSubscriptionSample2() {
        return new DriverPackageSubscription().id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static DriverPackageSubscription getDriverPackageSubscriptionRandomSampleGenerator() {
        return new DriverPackageSubscription().id(UUID.randomUUID());
    }
}
