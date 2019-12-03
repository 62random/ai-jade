package World;

public class Cell {
	
	private Position 	pos;
	private boolean 	water;
	private boolean 	fuel;
	private boolean		burning;
	private WorldMap	world;
	
	
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

	
	public Cell(WorldMap world, Position pos, boolean water, boolean fuel) {
		this.pos = pos;
		this.water = water;
		this.fuel = fuel;
		this.burning = false;
		this.world = world;
	}
	
}
