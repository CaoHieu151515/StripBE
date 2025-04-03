package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TripTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Trip getTripSample1() {
        return new Trip()
            .id(1L)
            .tripID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .maxSeat(1)
            .totalTime(1)
            .currentSeat(1)
            .startLocation("startLocation1")
            .endLocation("endLocation1")
            .description("description1")
            .condition("condition1")
            .cancelReason("cancelReason1");
    }

    public static Trip getTripSample2() {
        return new Trip()
            .id(2L)
            .tripID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .maxSeat(2)
            .totalTime(2)
            .currentSeat(2)
            .startLocation("startLocation2")
            .endLocation("endLocation2")
            .description("description2")
            .condition("condition2")
            .cancelReason("cancelReason2");
    }

    public static Trip getTripRandomSampleGenerator() {
        return new Trip()
            .id(longCount.incrementAndGet())
            .tripID(UUID.randomUUID())
            .maxSeat(intCount.incrementAndGet())
            .totalTime(intCount.incrementAndGet())
            .currentSeat(intCount.incrementAndGet())
            .startLocation(UUID.randomUUID().toString())
            .endLocation(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .condition(UUID.randomUUID().toString())
            .cancelReason(UUID.randomUUID().toString());
    }
}
