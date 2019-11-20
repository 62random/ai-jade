package World;

import java.io.Serializable;

public class Fire implements Serializable {

    private Position 	pos;
    private int         intensity;

    public Fire (Position pos, int intensity){
        this.pos = pos;
        this.intensity = intensity;
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
}
