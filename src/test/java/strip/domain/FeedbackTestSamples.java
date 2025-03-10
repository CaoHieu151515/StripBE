package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FeedbackTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Feedback getFeedbackSample1() {
        return new Feedback()
            .id(1L)
            .feedbackID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .feedbackDescription("feedbackDescription1")
            .feedbackRating(1);
    }

    public static Feedback getFeedbackSample2() {
        return new Feedback()
            .id(2L)
            .feedbackID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .feedbackDescription("feedbackDescription2")
            .feedbackRating(2);
    }

    public static Feedback getFeedbackRandomSampleGenerator() {
        return new Feedback()
            .id(longCount.incrementAndGet())
            .feedbackID(UUID.randomUUID())
            .feedbackDescription(UUID.randomUUID().toString())
            .feedbackRating(intCount.incrementAndGet());
    }
}
