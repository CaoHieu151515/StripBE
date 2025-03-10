package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PackageDriverTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static PackageDriver getPackageDriverSample1() {
        return new PackageDriver()
            .id(1L)
            .packageID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .name("name1")
            .description("description1")
            .time(1)
            .bonus(1);
    }

    public static PackageDriver getPackageDriverSample2() {
        return new PackageDriver()
            .id(2L)
            .packageID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .name("name2")
            .description("description2")
            .time(2)
            .bonus(2);
    }

    public static PackageDriver getPackageDriverRandomSampleGenerator() {
        return new PackageDriver()
            .id(longCount.incrementAndGet())
            .packageID(UUID.randomUUID())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .time(intCount.incrementAndGet())
            .bonus(intCount.incrementAndGet());
    }
}
