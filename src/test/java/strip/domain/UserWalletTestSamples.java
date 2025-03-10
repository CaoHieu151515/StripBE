package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserWalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserWallet getUserWalletSample1() {
        return new UserWallet().id(1L).userWallet(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static UserWallet getUserWalletSample2() {
        return new UserWallet().id(2L).userWallet(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static UserWallet getUserWalletRandomSampleGenerator() {
        return new UserWallet().id(longCount.incrementAndGet()).userWallet(UUID.randomUUID());
    }
}
