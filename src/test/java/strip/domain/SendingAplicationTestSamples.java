package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SendingAplicationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SendingAplication getSendingAplicationSample1() {
        return new SendingAplication().id(1L).apliID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).content("content1");
    }

    public static SendingAplication getSendingAplicationSample2() {
        return new SendingAplication().id(2L).apliID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).content("content2");
    }

    public static SendingAplication getSendingAplicationRandomSampleGenerator() {
        return new SendingAplication().id(longCount.incrementAndGet()).apliID(UUID.randomUUID()).content(UUID.randomUUID().toString());
    }
}
