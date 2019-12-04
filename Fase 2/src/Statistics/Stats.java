package Statistics;

import java.time.LocalDateTime;

public class Stats {

    //env
    private long timeElapsed;

    //fires
    private int firesLit;
    private int firesExtinguished;
    private int firesExtinguishedByDrones;
    private int firesExtinguishedByAircrafts;
    private int firesExtinguishedByTrucks;
    private float avgTimeBurning;


    //resources
    private int waterSpent;
    private int waterRefills;
    private int fuelSpent;
    private int fuelRefills;


    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long start) {
        this.timeElapsed = System.currentTimeMillis() - start;
    }

    public int getFiresLit() {
        return firesLit;
    }

    public void incrementFiresLit(int n) {
        this.firesLit += n;
    }

    public int getFiresExtinguished() {
        return firesExtinguished;
    }

    public void incrementFiresExtinguished(int n) {
        this.firesExtinguished += n;
    }

    public int getFiresExtinguishedByDrones() {
        return firesExtinguishedByDrones;
    }

    public void incrementFiresExtinguishedByDrones(int n) {
        this.firesExtinguishedByDrones += n;
    }

    public int getFiresExtinguishedByAircrafts() {
        return firesExtinguishedByAircrafts;
    }

    public void incrementFiresExtinguishedByAircrafts(int n) {
        this.firesExtinguishedByAircrafts += n;
    }

    public int getFiresExtinguishedByTrucks() {
        return firesExtinguishedByTrucks;
    }

    public void incrementFiresExtinguishedByTrucks(int n) {
        this.firesExtinguishedByTrucks += n;
    }

    public float getAvgTimeBurning() {
        return avgTimeBurning;
    }

    public void updateAvgTimeBurning(float time) {
        this.avgTimeBurning = (avgTimeBurning*(firesExtinguished - 1) + time)/firesExtinguished;
    }

    public int getWaterSpent() {
        return waterSpent;
    }

    public void incrementWaterSpent(int n) {
        this.waterSpent += n;
    }

    public int getWaterRefills() {
        return waterRefills;
    }

    public void incrementWaterRefills(int n) {
        this.waterRefills +=n;
    }

    public int getFuelSpent() {
        return fuelSpent;
    }

    public void incrementFuelSpent(int n) {
        this.fuelSpent += n;
    }

    public int getFuelRefills() {
        return fuelRefills;
    }

    public void incrementFuelRefills(int n) {
        this.fuelRefills += n;
    }

    public Stats() {

    }
}
