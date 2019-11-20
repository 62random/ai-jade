package World;

public class FighterInfo {

    private String      name;
    private Position 	pos;
    private Boolean 	available;

    public FighterInfo(String name, Position pos, Boolean available){
        this.name = name;
        this.pos = pos;
        this.available = available;
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

}
