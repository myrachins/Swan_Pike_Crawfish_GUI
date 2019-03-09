import models.Creature;
import models.Truck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class CreatureTests {
    @Test
    public void testDirection() throws InterruptedException {
        Truck truck = new Truck(0, 0);
        ArrayList<Thread> threads = new ArrayList<>();

        // Creating 100 threads, each of them to move stuck 100 times
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                for(int j = 0; j < 100; j++) {
                    new Creature("Creature", 0).moveTruck(truck, 1);
                }
            });
            threads.add(thread);
            thread.start();
        }

        // Waiting for all threads to finish
        for (Thread thread : threads) {
            thread.join();
        }

        Assertions.assertEquals(truck.getX(), 100 * 100d);
        Assertions.assertEquals(truck.getY(), 0d);
    }
}
