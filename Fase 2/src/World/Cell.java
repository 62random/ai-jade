package World;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Cell implements Serializable {
	
	private Position 	pos;
	private boolean 	water;
	private boolean 	fuel;
	private boolean		burning;
	private WorldMap	world;
	private Path 		paths;
	
	
	public Position getPos() {
		return pos;
	}
	public void setPos(Position pos) {
		this.pos = pos;
	}
	public boolean isWater() {
		return water;
	}
	public void setWater(boolean water) {
		this.water = water;
	}
	public boolean isFuel() {
		return fuel;
	}
	public void setFuel(boolean fuel) {
		this.fuel = fuel;
	}
	public boolean isBurning() {
		return burning;
	}
	public void setBurning(boolean burning) {
		this.burning = burning;
	}

	public Path getPaths() {
		return paths;
	}

	public void setPaths(Path p) {
		this.paths = p;
	}

	public Cell(WorldMap world, Position pos, boolean water, boolean fuel) {
		this.pos = pos;
		this.water = water;
		this.fuel = fuel;
		this.burning = false;
		this.world = world;
		this.paths = new Path(world, pos);
	}
	
}
