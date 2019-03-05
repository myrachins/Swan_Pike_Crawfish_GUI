package models;

import settings.RunAttributes;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.concurrent.ThreadLocalRandom;

public class Runner {
    private Runner() { }

    private static ArrayList<Thread> threads;
    private static TruckListener truckListener;

    public static void setTruckListener(TruckListener listener) {
        Runner.truckListener = listener;
    }

    public static void start(RunAttributes attributes) throws InterruptedException {
        truckListener.onStart();
        Truck truck = new Truck(attributes.getStartX(), attributes.getStartY()); // Main truck to move

        System.out.println("Truck's start location: \t " + truck.getCoordinates() + System.lineSeparator());

        // Creating list of creatures
        ArrayList<Creature> creatures = new ArrayList<>();
        creatures.add(new Creature("Swan", attributes.getSwanAngle()));
        creatures.add(new Creature("Pike", attributes.getPikeAngle()));
        creatures.add(new Creature("Crawfish", attributes.getCrawfishAngle()));

        // Creating list of threads to execute
        threads = new ArrayList<>();

        for(Creature creature : creatures) {
            threads.add(new Thread(() -> {
                while (true) {
                    creature.moveTruck(truck, attributes.getForceLowBound(), attributes.getForceUpperBound());
                    truckListener.truckMoved(truck);
                    try {
                        Thread.sleep(ThreadLocalRandom.current().nextInt(attributes.getSleepLowBound(),
                                attributes.getSleepUpperBound()));
                    } catch (InterruptedException e) {
                        System.out.println("- " + creature.getName() + " finished 'work'");
                        break; // after interrupting going out from cycle
                    }
                }
            }));
        }

        // We start executing threads only here to make competition more fair - we do not spend much time on creation new
        // objects of streams, while already created objects are executing at the moment
        threads.forEach(Thread::start);
        Thread.sleep(attributes.getWorkTime());

        stop();
    }

    public static void stop() {
        threads.forEach(Thread::interrupt); // Interrupting all threads
        truckListener.onFinish();
    }

    public interface TruckListener extends EventListener {
        void truckMoved(Truck truck);
        void onStart();
        void onFinish();
    }
}
