package models;

import settings.AppSettings;
import settings.RunAttributes;
import sun.awt.windows.ThemeReader;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Runner {
    private Runner() { }

    private static ArrayList<Thread> threads;
    private final static ArrayList<TruckListener> truckListeners = new ArrayList<>();
    private static Thread executor = new Thread();

    public static void addTruckListener(TruckListener listener) {
        synchronized (truckListeners) {
            Runner.truckListeners.add(listener);
            truckListeners.notifyAll();
        }
    }

    public static void start(RunAttributes attributes) { // start is invoked only from application thread
        if (executor.isAlive()) {
            throw new RuntimeException("Runner is already started");
        }
        synchronized (truckListeners) {
            for(TruckListener truckListener : truckListeners) {
                truckListener.onStart();
            }
            truckListeners.notifyAll();
        }
        Truck truck = new Truck(attributes.getStartX(), attributes.getStartY()); // Main truck to move

        System.out.println("Truck's start location: \t " + truck.getCoordinates() + System.lineSeparator());

        // Creating list of creatures
        ArrayList<Creature> creatures = new ArrayList<>();
        creatures.add(new Creature("Swan", attributes.getSwanAngle(), AppSettings.SWAN_COLOR));
        creatures.add(new Creature("Pike", attributes.getPikeAngle(), AppSettings.PIKE_COLOR));
        creatures.add(new Creature("Crawfish", attributes.getCrawfishAngle(), AppSettings.CRAWFISH_COLOR));

        // Creating list of threads to execute
        threads = new ArrayList<>();

        for(Creature creature : creatures) {
            threads.add(new Thread(() -> {
                while (true) {
                    synchronized (truckListeners) {
                        creature.moveTruck(truck, attributes.getForceLowBound(), attributes.getForceUpperBound());
                        System.out.println("Trucked moved by " + creature.getName());
                        for(TruckListener truckListener : truckListeners) {
                            truckListener.truckMoved(truck, creatures);
                        }
                        truckListeners.notifyAll();
                    }
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
        executor = new Thread(() -> {
            try {
                Thread.sleep(attributes.getWorkTime() * 1000L); // Converting seconds to milliseconds
                stop();
            } catch (InterruptedException e) {
                System.out.println("Task was denied");
            }
        });
        executor.start();
    }

    public static synchronized void stop() { // stop can be invoked either from executor thread or from application thread
        if (!executor.isAlive()) {
            throw new RuntimeException("Runner hasn't been started yet");
        }
        threads.forEach(Thread::interrupt); // Interrupting all threads
        synchronized (truckListeners) {
            for(TruckListener truckListener : truckListeners) {
                truckListener.onFinish();
            }
        }
        executor.interrupt();
    }

    public interface TruckListener extends EventListener {
        void truckMoved(Truck truck, Collection<Creature> creatures);
        void onStart();
        void onFinish();
    }
}
