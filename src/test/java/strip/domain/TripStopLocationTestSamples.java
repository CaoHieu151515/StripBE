package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TripStopLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TripStopLocation getTripStopLocationSample1() {
        return new TripStopLocation()
            .id(1L)
            .stopLocaID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .stopLoca("stopLoca1")
            .stopLocaStatus("stopLocaStatus1");
    }

    public static TripStopLocation getTripStopLocationSample2() {
        return new TripStopLocation()
            .id(2L)
            .stopLocaID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .stopLoca("stopLoca2")
            .stopLocaStatus("stopLocaStatus2");
    }

    public static TripStopLocation getTripStopLocationRandomSampleGenerator() {
        return new TripStopLocation()
            .id(longCount.incrementAndGet())
            .stopLocaID(UUID.randomUUID())
            .stopLoca(UUID.randomUUID().toString())
            .stopLocaStatus(UUID.randomUUID().toString());
    }
}
