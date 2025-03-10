package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WalletTransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WalletTransaction getWalletTransactionSample1() {
        return new WalletTransaction()
            .id(1L)
            .transID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .transactionThirdPartyID("transactionThirdPartyID1");
    }

    public static WalletTransaction getWalletTransactionSample2() {
        return new WalletTransaction()
            .id(2L)
            .transID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .transactionThirdPartyID("transactionThirdPartyID2");
    }

    public static WalletTransaction getWalletTransactionRandomSampleGenerator() {
        return new WalletTransaction()
            .id(longCount.incrementAndGet())
            .transID(UUID.randomUUID())
            .transactionThirdPartyID(UUID.randomUUID().toString());
    }
}
