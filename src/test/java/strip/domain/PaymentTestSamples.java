package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PaymentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Payment getPaymentSample1() {
        return new Payment().id(1L).paymentID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa")).transactionId("transactionId1");
    }

    public static Payment getPaymentSample2() {
        return new Payment().id(2L).paymentID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367")).transactionId("transactionId2");
    }

    public static Payment getPaymentRandomSampleGenerator() {
        return new Payment().id(longCount.incrementAndGet()).paymentID(UUID.randomUUID()).transactionId(UUID.randomUUID().toString());
    }
}
