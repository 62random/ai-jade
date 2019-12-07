package World;

import Agents.Aircraft;
import Agents.Drone;
import Agents.Fighter;
import Agents.Truck;
import Graphics.Configs;

public class FighterInfo {

    private String      name;
    private Position 	pos;
    private Boolean 	available;
    private int         type;
    private int         currentFuel;
    private int         currentWater;
    private int         fuelCapacity;
    private int         waterCapacity;

    public FighterInfo(Fighter f){
        this.name = f.getName();
        this.pos = f.getPos();
        this.available = f.isAvailable();
        this.currentFuel = f.getCurrentFuel();
        this.currentWater = f.getCurrentWater();
        this.fuelCapacity = f.getFuelCapacity();
        this.waterCapacity = f.getWaterCapacity();

        if(f instanceof Drone)
            this.type = Configs.AG_DRONE;
        if(f instanceof Aircraft)
            this.type = Configs.AG_AIRCRAFT;
        if(f instanceof Truck)
            this.type = Configs.AG_TRUCK;
    }

    public String getAID(){
        return  name;
    }

    public void setAID(String name){
        this.name = name;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailable() {
        return available;
    }

    public int getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(int currentFuel) {
        this.currentFuel = currentFuel;
    }

    public int getCurrentWater() {
        return currentWater;
    }

    public void setCurrentWater(int currentWater) {
        this.currentWater = currentWater;
    }

    public int getFuelCapacity() {
        return fuelCapacity;
    }

    public void setFuelCapacity(int fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }

    public int getWaterCapacity() {
        return waterCapacity;
    }

    public void setWaterCapacity(int waterCapacity) {
        this.waterCapacity = waterCapacity;
    }

    public boolean isAt(int i, int j) {
        return pos.coordsEqual(i,j);
    }

}
