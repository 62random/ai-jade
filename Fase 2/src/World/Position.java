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

	public double distanceBetweenTwoPositions(Position p2){
		double ac = Math.abs(p2.getY() - this.x);
		double cb = Math.abs(p2.getX() - this.y);

		return  Math.hypot(ac,cb);
	}

	public boolean isAdjacent(Position p2){
		if(this.x == p2.getX()+1 && this.y == p2.getY()+1){ return true; }
		if(this.x == p2.getX()+1 && this.y == p2.getY()){ return true; }
		if(this.x == p2.getX()+1 && this.y == p2.getY()-1){ return true; }
		if(this.x == p2.getX() && this.y == p2.getY()-1){ return true; }
		if(this.x == p2.getX()-1 && this.y == p2.getY()-1){ return true; }
		if(this.x == p2.getX()-1 && this.y == p2.getY()) {return true; }
		if(this.x == p2.getX()-1 && this.y == p2.getY()+1){ return true; }
		if(this.x == p2.getX() && this.y == p2.getY()+1){ return true; }

		return false;
	}
}
