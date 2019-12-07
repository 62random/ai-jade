package World;

import Graphics.Configs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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

	public Position(Position p) {
		this.x = p.x;
		this.y = p.y;
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

	public float distance(Position p){

		return  (float) Math.sqrt((x - p.getX())*(x-p.getX()) + (y - p.getY())*(y-p.getY()));
	}

	public boolean isAdjacent(Position p){
		return (x - p.getX())*(x - p.getX()) == 1 || (y - p.getY())*(y - p.getY()) == 1;
	}

	public Position getAdjacentLeft(){
		return getX() > 0 ? new Position(getX() - 1, getY()) : null;
	}

	public Position getAdjacentRight(){
		return getX() < Configs.MAP_SIZE ? new Position(getX() + 1, getY()) : null;
	}

	public Position getAdjacentUp(){
		return getY() > 0 ? new Position(getX(), getY() - 1) : null;
	}

	public Position getAdjacentDown(){
		return getY() < Configs.MAP_SIZE ? new Position(getX(), getY() + 1) : null;
	}

    public boolean coordsEqual(int i, int j) {
		return x==i && y==j;
    }

    public static Comparator<Position> getComparator(){
		return new Comparator<Position>() {
			@Override
			public int compare(Position p1, Position p2) {
				int dx = p1.getX() - p2.getX(), dy = p1.getY() - p2.getY();
				if(dx == 0) {
					if (dy == 0)
						return 0;
					else
						return dy;
				}
				else
					return dx;
			}
		};
	}
}
