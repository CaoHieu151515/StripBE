package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RatingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Rating getRatingSample1() {
        return new Rating().id(1L).ratingID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).ratingDriver(1);
    }

    public static Rating getRatingSample2() {
        return new Rating().id(2L).ratingID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).ratingDriver(2);
    }

    public static Rating getRatingRandomSampleGenerator() {
        return new Rating().id(longCount.incrementAndGet()).ratingID(UUID.randomUUID()).ratingDriver(intCount.incrementAndGet());
    }
}
