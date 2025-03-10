package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SystemTempWalletTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SystemTempWallet getSystemTempWalletSample1() {
        return new SystemTempWallet().id(1L).systemWalletID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"));
    }

    public static SystemTempWallet getSystemTempWalletSample2() {
        return new SystemTempWallet().id(2L).systemWalletID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"));
    }

    public static SystemTempWallet getSystemTempWalletRandomSampleGenerator() {
        return new SystemTempWallet().id(longCount.incrementAndGet()).systemWalletID(UUID.randomUUID());
    }
}
