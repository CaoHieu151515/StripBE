package strip.domain;

import java.util.UUID;

public class WalletDepositTestSamples {

    public static WalletDeposit getWalletDepositSample1() {
        return new WalletDeposit()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .bankNumber("bankNumber1")
            .nameOfBank("nameOfBank1")
            .bank("bank1");
    }

    public static WalletDeposit getWalletDepositSample2() {
        return new WalletDeposit()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .bankNumber("bankNumber2")
            .nameOfBank("nameOfBank2")
            .bank("bank2");
    }

    public static WalletDeposit getWalletDepositRandomSampleGenerator() {
        return new WalletDeposit()
            .id(UUID.randomUUID())
            .bankNumber(UUID.randomUUID().toString())
            .nameOfBank(UUID.randomUUID().toString())
            .bank(UUID.randomUUID().toString());
    }
}
