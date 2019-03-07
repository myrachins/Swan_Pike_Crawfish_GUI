package models;

import settings.RunAttributes;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class Runner {
    private Runner() { }

    /**
     * If we would use Runner from different threads, we should mark it as volatile or add some synchronized blocks
     * By know it's called only from application thread
     */
    private static /*volatile*/ ExecuteState state = ExecuteState.NONWORKING;
    private static ArrayList<Thread> threads;
    private final static ArrayList<TruckListener> truckListeners = new ArrayList<>();
    private final static Timer timer = new Timer();

    public static void addTruckListener(TruckListener listener) {
        synchronized (truckListeners) {
            Runner.truckListeners.add(listener);
            truckListeners.notifyAll();
        }
    }

    public static void start(RunAttributes attributes) {
        if (state == ExecuteState.WORKING) {
            throw new RuntimeException("Runner is already started");
        }
        state = ExecuteState.WORKING;
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
        creatures.add(new Creature("Swan", attributes.getSwanAngle()));
        creatures.add(new Creature("Pike", attributes.getPikeAngle()));
        creatures.add(new Creature("Crawfish", attributes.getCrawfishAngle()));

        // Creating list of threads to execute
        threads = new ArrayList<>();

        for(Creature creature : creatures) {
            threads.add(new Thread(() -> {
                while (true) {
                    creature.moveTruck(truck, attributes.getForceLowBound(), attributes.getForceUpperBound());
                    System.out.println("Trucked moved by " + creature.getName());
                    synchronized (truckListeners) {
                        for(TruckListener truckListener : truckListeners) {
                            truckListener.truckMoved(truck);
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(state == ExecuteState.WORKING) {
                    stop();
                }
            }
        }, attributes.getWorkTime());
    }

    public static void stop() {
        if (state == ExecuteState.NONWORKING) {
            throw new RuntimeException("Runner hasn't been started yet");
        }
        threads.forEach(Thread::interrupt); // Interrupting all threads
        synchronized (truckListeners) {
            for(TruckListener truckListener : truckListeners) {
                truckListener.onFinish();
            }
        }
        state = ExecuteState.NONWORKING;
    }

    public interface TruckListener extends EventListener {
        void truckMoved(Truck truck);
        void onStart();
        void onFinish();
    }

    private enum ExecuteState {
        NONWORKING, WORKING
    }
}
