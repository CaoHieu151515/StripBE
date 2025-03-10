package strip.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class VehicleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Vehicle getVehicleSample1() {
        return new Vehicle()
            .id(1L)
            .vehicleID(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .vehicleNumber("vehicleNumber1")
            .numberOfSeats(1)
            .vehicleColor("vehicleColor1")
            .vehicleBrand("vehicleBrand1");
    }

    public static Vehicle getVehicleSample2() {
        return new Vehicle()
            .id(2L)
            .vehicleID(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .vehicleNumber("vehicleNumber2")
            .numberOfSeats(2)
            .vehicleColor("vehicleColor2")
            .vehicleBrand("vehicleBrand2");
    }

    public static Vehicle getVehicleRandomSampleGenerator() {
        return new Vehicle()
            .id(longCount.incrementAndGet())
            .vehicleID(UUID.randomUUID())
            .vehicleNumber(UUID.randomUUID().toString())
            .numberOfSeats(intCount.incrementAndGet())
            .vehicleColor(UUID.randomUUID().toString())
            .vehicleBrand(UUID.randomUUID().toString());
    }
}
