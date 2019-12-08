package Statistics;

import Graphics.Configs;
import World.Fire;

import java.time.LocalDateTime;

public class Stats {

    //env
    private long startTime;

    //fires
    private int firesLit;
    private int firesExtinguished;
    private int firesExtinguishedByDrones;
    private int firesExtinguishedByAircrafts;
    private int firesExtinguishedByTrucks;
    private float avgTimeBurning;


    //resources
    private int waterRefills;
    private int fuelRefills;


    public long getStartTime() {
        return startTime;
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

    public int getFiresExtinguishedByDrones() {
        return firesExtinguishedByDrones;
    }

    public int getFiresExtinguishedByAircrafts() {
        return firesExtinguishedByAircrafts;
    }

    public int getFiresExtinguishedByTrucks() {
        return firesExtinguishedByTrucks;
    }

    public float getAvgTimeBurning() {
        return avgTimeBurning;
    }

    public void updateAvgTimeBurning(float time) {
        this.avgTimeBurning = (avgTimeBurning*(firesExtinguished - 1) + time)/firesExtinguished;
    }

    public int getWaterRefills() {
        return waterRefills;
    }

    public void incrementWaterRefills(int n) {
        this.waterRefills +=n;
    }

    public int getFuelRefills() {
        return fuelRefills;
    }

    public void incrementFuelRefills(int n) {
        this.fuelRefills += n;
        System.out.println("Fuel refills up till now: " + fuelRefills);
    }

    public void extinguishedFire(Fire f){
        this.firesExtinguished++;
        this.updateAvgTimeBurning(System.currentTimeMillis() - f.getStartTime());
        switch (f.getExtinguisher()){
            case Configs.AG_AIRCRAFT:
                firesExtinguishedByAircrafts++;
                break;
            case Configs.AG_DRONE:
                firesExtinguishedByDrones++;
                break;
            case Configs.AG_TRUCK:
                firesExtinguishedByTrucks++;
                break;
        }

    }

    public Stats() {
        startTime = System.currentTimeMillis();
        this.firesLit = 0;
        this.firesExtinguished = 0;
        this.firesExtinguishedByDrones = 0;
        this.firesExtinguishedByAircrafts = 0;
        this.firesExtinguishedByTrucks = 0;
        this.avgTimeBurning = 0;
        this.waterRefills = 0;
        this.fuelRefills = 0;
    }
}
