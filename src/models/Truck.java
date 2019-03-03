package models;

public class Truck {
    private double x;
    private double y;

    public Truck(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public synchronized double getX() {
        return x;
    }

    public synchronized void setX(double x) {
        this.x = x;
    }

    public synchronized double getY() {
        return y;
    }

    public synchronized void setY(double y) {
        this.y = y;
    }

    /**
     *
     * @return string representation of truck's coordinates
     */
    public synchronized String getCoordinates() {
        return String.format("(%.2f, %.2f)", x, y);
    }
}

