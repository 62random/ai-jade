package World;
import java.util.TreeMap;
import java.util.stream.Collectors;

import Agents.Fighter;
import java.util.Random;
import java.util.Map;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class WorldMap {
	
	private Map<Position, Cell> map;
	private int 				dimension;
	private Map<String,Fighter> fighters;
	
	
	public Map<Position, Cell> getMap() {
		return map;
	}

	public void setMap(Map<Position, Cell> map) {
		this.map = map;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public Map<String, Fighter> getFighters() {
		return fighters;
	}

	public void setFighters(Map<String, Fighter> fighters) {
		this.fighters = fighters;
	}
	
	public void addFighter(Fighter f) {
		fighters.put(f.getAID().toString(), f);
	}
	
	public List<Fighter> fightersAtPosition(Position p) {
		return fighters.values().stream().filter( a -> a.getPos().equals(p)).collect(Collectors.toList());
	}

	public void changeCellStatus (Position p, boolean burning){
		Cell c = map.getKey(p);
		c.setBurning(burning);
		map.put(p,c);
	}

	public WorldMap(int dim) {
		this.map 		= new TreeMap<Position, Cell>(
				new Comparator<Position>() {
	                @Override
	                public int compare(Position p1, Position p2) {
	                    return (p1.getX() + p1.getY()) - (p2.getX() + p2.getY());
	                }
	            });
		this.dimension 	= dim;
		this.fighters 	= new HashMap<String, Fighter>();
		
		Position pos;
		Cell c;
		Random r = new Random();
		boolean water, fuel;
		int tmp;
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++) {
				pos = new Position(i, j);
				tmp = r.nextInt(100);
				if(tmp < 1)
					c = new Cell(pos, true, true);
				else if(tmp < 3)
					c = new Cell(pos, true, false);
				else if(tmp < 5)
					c = new Cell(pos, true, true);
				else				
					c = new Cell(pos, false, false);
				
				map.put(pos, c);
			}
	}
}
