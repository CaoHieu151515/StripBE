package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RequestTripTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static RequestTrip getRequestTripSample1() {
        return new RequestTrip()
            .id(1L)
            .requestTripID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .startLoca("startLoca1")
            .endLoca("endLoca1")
            .luggageDescription("luggageDescription1");
    }

    public static RequestTrip getRequestTripSample2() {
        return new RequestTrip()
            .id(2L)
            .requestTripID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .startLoca("startLoca2")
            .endLoca("endLoca2")
            .luggageDescription("luggageDescription2");
    }

    public static RequestTrip getRequestTripRandomSampleGenerator() {
        return new RequestTrip()
            .id(longCount.incrementAndGet())
            .requestTripID(UUID.randomUUID())
            .startLoca(UUID.randomUUID().toString())
            .endLoca(UUID.randomUUID().toString())
            .luggageDescription(UUID.randomUUID().toString());
    }
}
