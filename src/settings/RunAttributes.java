package settings;

public class RunAttributes {
    public RunAttributes() { }

    public RunAttributes(int sleepLowBound, int sleepUpperBound, int forceLowBound, int forceUpperBound,
                         int swanAngle, int pikeAngle, int crawfishAngle, int workTime,
                         double startX, double startY)
    {
        this.sleepLowBound = sleepLowBound;
        this.sleepUpperBound = sleepUpperBound;
        this.forceLowBound = forceLowBound;
        this.forceUpperBound = forceUpperBound;
        this.swanAngle = swanAngle;
        this.pikeAngle = pikeAngle;
        this.crawfishAngle = crawfishAngle;
        this.workTime = workTime;
        this.startX = startX;
        this.startY = startY;
    }

    private int sleepLowBound = 1000;
    private int sleepUpperBound = 5000;

    private int forceLowBound = 1;
    private int forceUpperBound = 10;

    private int swanAngle = 60;
    private int pikeAngle = 180;
    private int crawfishAngle = 300;

    /**
     * Duration of simulation in seconds
     */
    private int workTime = 25;

    private double startX;
    private double startY;

    public int getSleepLowBound() {
        return sleepLowBound;
    }

    public int getSleepUpperBound() {
        return sleepUpperBound;
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
