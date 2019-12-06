package World;

import java.io.Serializable;

public class Fire implements Serializable {

    private Position 	pos;
    private int         intensity;
    private long        startTime;
    private boolean     active;
    private int         extinguisher;

    public Fire (Position pos, int intensity){
        startTime = System.currentTimeMillis();
        this.pos = new Position(pos);
        this.intensity  = intensity;
        this.active     = true;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getExtinguisher() {
        return extinguisher;
    }

    public void setExtinguisher(int extinguisher) {
        this.extinguisher = extinguisher;
    }

    public void extinguish(){
        this.setActive(false);
    }
}
