package World;

import java.io.Serializable;

public class Position implements Serializable{
	private int y;
	private int x;
	
	
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	
	public Position reverse() {
		return new Position(y,x);
	}
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null || (o.getClass() != this.getClass())) return false;
		Position p = (Position) o;
		
		return this.x == p.getX() && this.y == p.getY();
	}
	
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
