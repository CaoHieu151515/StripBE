package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserDetailTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserDetail getUserDetailSample1() {
        return new UserDetail()
            .id(1L)
            .appUserDetail(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .phone("phone1")
            .gender("gender1");
    }

    public static UserDetail getUserDetailSample2() {
        return new UserDetail()
            .id(2L)
            .appUserDetail(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .phone("phone2")
            .gender("gender2");
    }

    public static UserDetail getUserDetailRandomSampleGenerator() {
        return new UserDetail()
            .id(longCount.incrementAndGet())
            .appUserDetail(UUID.randomUUID())
            .phone(UUID.randomUUID().toString())
            .gender(UUID.randomUUID().toString());
    }
}
