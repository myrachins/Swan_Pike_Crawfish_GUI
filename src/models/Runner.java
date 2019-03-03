package models;

import settings.RunAttributes;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Runner {
    private Runner() { }

    private volatile static ExecuteState state = ExecuteState.STOPPED;

    private static ArrayList<Thread> threads;
    private static ScheduledExecutorService exec;

    public static void start(RunAttributes attributes) throws InterruptedException {
        if (state == ExecuteState.WORKING) {
            throw new RuntimeException("Runner is working");
        }
        state = ExecuteState.WORKING;
        Truck truck = new Truck(attributes.getStartX(), attributes.getStartY()); // Main truck to move

        System.out.println("Truck's start location: \t " + truck.getCoordinates() + System.lineSeparator());

        // Creating list of creatures
        ArrayList<Creature> creatures = new ArrayList<>();
        creatures.add(new Creature("Swan", attributes.getSwanAngle()));
        creatures.add(new Creature("Pike", attributes.getPikeAngle()));
        creatures.add(new Creature("Crawfish", attributes.getCrawfishAngle()));

        // Starting timer to output message every specified seconds
        exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(() -> System.out.println("Truck's location: \t " + truck.getCoordinates()),
                0, attributes.getRepeatOutputTime(), TimeUnit.SECONDS);

        // Creating list of threads to execute
        threads = new ArrayList<>();

        for(Creature creature : creatures) {
            threads.add(new Thread(() -> {
                while (true) {
                    creature.moveTruck(truck, attributes.getForceLowBound(), attributes.getForceUpperBound()); // TODO: make random upper and low bounds
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
        if(state == ExecuteState.STOPPED) {
            throw new RuntimeException("Runner should be started before stopping");
        }
        threads.forEach(Thread::interrupt); // Interrupting all threads
        exec.shutdown();
        state = ExecuteState.STOPPED;
    }

    private enum ExecuteState {
        STOPPED, WORKING
    }
}
