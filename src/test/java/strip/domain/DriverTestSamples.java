package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DriverTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Driver getDriverSample1() {
        return new Driver().id(1L).driverID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).driverPoint(1);
    }

    public static Driver getDriverSample2() {
        return new Driver().id(2L).driverID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).driverPoint(2);
    }

    public static Driver getDriverRandomSampleGenerator() {
        return new Driver().id(longCount.incrementAndGet()).driverID(UUID.randomUUID()).driverPoint(intCount.incrementAndGet());
    }
}
