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

    public FighterInfo(String name, Position pos, Boolean available){
        this.name = name;
        this.pos = pos;
        this.available = available;
    }

    public FighterInfo(Fighter f){
        this.name = f.getName();
        this.pos = f.getPos();
        this.available = f.isAvailable();

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

    public boolean isAt(int i, int j) {
        return pos.coordsEqual(i,j);
    }
}
