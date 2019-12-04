package World;

import Graphics.Configs;
import sun.security.krb5.Config;

import java.io.Serializable;
import java.util.ArrayList;
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

	public double distanceBetweenTwoPositions(Position p2){

		int distance = 0;
		int x1 = this.getX();
		int y1 = this.getY();

		while(!(x1 == p2.getX() && y1 == p2.getY())) {
			if (x1 < p2.getX() && y1 < p2.getY()) {
				x1++;
				y1++;
			} else if (x1 < p2.getX() && y1 > p2.getY()) {
				x1++;
				y1--;
			} else if (x1 > p2.getX() && y1 > p2.getY()) {
				x1--;
				y1--;
			} else if (x1 > p2.getX() && y1 < p2.getY()) {
				x1--;
				y1++;
			} else if (x1 < p2.getX()) {
				x1++;
			} else if (x1 > p2.getX()) {
				x1--;
			} else if (y1 < p2.getY()) {
				y1++;
			} else {
				y1--;
			}
			distance++;
		}
		return  distance;
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

	public Position getAdjacentLeft(Position p){
		if(p.getX()!=0) {
			Position position = new Position(p.getX(), p.getY());
			position.setX(p.getX() - 1);
			return position;
		}
		else return p;
	}

	public Position getAdjacentRight(Position p){
		if(p.getX()!= Configs.MAP_SIZE) {
			Position position = new Position(p.getX(), p.getY());
			position.setX(p.getX() + 1);
			return position;
		}
		else return p;
	}

	public Position getAdjacentUp(Position p){
		if(p.getY()!=0) {
			Position position = new Position(p.getX(), p.getY());
			position.setX(p.getY() - 1);
			return position;
		}
		else return p;
	}

	public Position getAdjacentDown(Position p){
		if(p.getY()!=Configs.MAP_SIZE) {
			Position position = new Position(p.getX(), p.getY());
			position.setX(p.getY() + 1);
			return position;
		}
		else return p;
	}

    public boolean coordsEqual(int i, int j) {
		return x==i && y==j;
    }
}
