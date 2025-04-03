package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class DriverPointHistoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static DriverPointHistory getDriverPointHistorySample1() {
        return new DriverPointHistory().id(1L).pointId(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).point(1).reason("reason1");
    }

    public static DriverPointHistory getDriverPointHistorySample2() {
        return new DriverPointHistory().id(2L).pointId(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).point(2).reason("reason2");
    }

    public static DriverPointHistory getDriverPointHistoryRandomSampleGenerator() {
        return new DriverPointHistory()
            .id(longCount.incrementAndGet())
            .pointId(UUID.randomUUID())
            .point(intCount.incrementAndGet())
            .reason(UUID.randomUUID().toString());
    }
}
