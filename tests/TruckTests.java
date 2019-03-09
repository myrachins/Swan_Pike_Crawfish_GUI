import models.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TruckTests {
    @Test
    public void testThreadSafe() {
        Truck truck = new Truck(0, 0);

        for(int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    synchronized (truck) {
                        double x = truck.getX();
                        truck.setX(x + 1);

                        Assertions.assertEquals(truck.getX(), x + 1);
                    }
                }
            }).start();
        }
    }
}
