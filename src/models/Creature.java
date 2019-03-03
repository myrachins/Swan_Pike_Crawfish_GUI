package models;

import java.util.concurrent.ThreadLocalRandom;

public class Creature {
    private String name;
    private double angle;

    /**
     * Cosine of angle value. Calculates just ones
     */
    private double cosAngle;
    /**
     * Sinus of angle value. Calculates just ones
     */
    private double sinAngle;

    /**
     *
     * @param name: Name of creature
     * @param angle: Angle of creature in degrees
     */
    public Creature(String name, int angle) {
        this.name = name;
        this.angle = angle;
        this.cosAngle = Math.cos(Math.PI * angle / 180f);
        this.sinAngle = Math.sin(Math.PI * angle / 180f);
    }

    public String getName() {
        return name;
    }

    public double getAngle() {
        return angle;
    }

    public void moveTruck(Truck truck, int forceLowBound, int forceUpperBound) {
        double force = ThreadLocalRandom.current().nextDouble(forceLowBound, forceUpperBound);
        moveTruck(truck, force);
    }

    public void moveTruck(Truck truck, double force) {
        synchronized (truck) {
            double x = truck.getX() + force * cosAngle;
            double y = truck.getY() + force * sinAngle;
            truck.setX(x);
            truck.setY(y);

            truck.notifyAll();
        }
    }
}

