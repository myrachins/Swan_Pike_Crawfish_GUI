package settings;

public class RunAttributes {
    public RunAttributes() { }
    // TODO: Make custom constructor

    private int sleepLowBound = 1000;
    private int sleepUpperBound = 5000;
    private int repeatOutputTime = 2;

    private int forceLowBound = 0;
    private int forceUpperBound = 10; // TODO: Check for real default values

    private int swanAngle = 60;
    private int pikeAngle = 180;
    private int crawfishAngle = 300;

    private int coeffLowBound = 1;
    private int coeffUpperBound = 10;

    private int workTime = 25 * 1000;

    private double startX;
    private double startY;

    public int getSleepLowBound() {
        return sleepLowBound;
    }

    public int getSleepUpperBound() {
        return sleepUpperBound;
    }

    public int getRepeatOutputTime() {
        return repeatOutputTime;
    }

    public int getSwanAngle() {
        return swanAngle;
    }

    public int getPikeAngle() {
        return pikeAngle;
    }

    public int getCrawfishAngle() {
        return crawfishAngle;
    }

    public int getCoeffLowBound() {
        return coeffLowBound;
    }

    public int getCoeffUpperBound() {
        return coeffUpperBound;
    }

    public int getWorkTime() {
        return workTime;
    }

    public double getStartX() {
        return startX;
    }

    public double getStartY() {
        return startY;
    }

    public int getForceLowBound() {
        return forceLowBound;
    }

    public int getForceUpperBound() {
        return forceUpperBound;
    }
}
