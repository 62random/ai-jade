package World;
import Agents.Fighter;
import javafx.geometry.Pos;
import sun.reflect.generics.tree.Tree;
import sun.tools.tree.PostIncExpression;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
	
	private Map<Position, Cell> 	map;
	private int 					dimension;
	private Map<String,FighterInfo> fighters;
	private Map<Integer,Fire> 		fires;
	private int						nBurningCells = 0;
	
	
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

	public Map<String, FighterInfo> getFighters() {
		return fighters;
	}

	public void setFighters(Map<String, FighterInfo> fighters) {
		this.fighters = fighters;
	}
	
	public void addFighter(FighterInfo f) {
		fighters.put(f.getAID().toString(), f);
	}
	
	public List<FighterInfo> fightersAtPosition(Position p) {
		return fighters.values().stream().filter( a -> a.getPos().equals(p)).collect(Collectors.toList());
	}

	public Map<Integer, Fire> getFires() {
		return fires;
	}

	public void setFires(Map<Integer, Fire> fires) {
		this.fires = fires;
	}

	public void addFire(Fire f) {
		fires.put(nBurningCells, f);
	}

	public int getnBurningCells() {
		return nBurningCells;
	}

	public void setnBurningCells(int nBurningCells) {
		this.nBurningCells = nBurningCells+1;
	}

	public void changeCellStatus (Position p, boolean burning){
		Cell c = map.get(p);
		c.setBurning(burning);
		map.put(p,c);
	}

	public void propagateFire(Position p, Fire f){
		Position position1 = p.getAdjacentRight(p);
		Position position2 = p.getAdjacentLeft(p);
		Position position3 = p.getAdjacentDown(p);
		Position position4 = p.getAdjacentUp(p);

		Cell c1 = map.get(position1);
		Cell c2 = map.get(position2);
		Cell c3 = map.get(position3);
		Cell c4 = map.get(position4);
		if(f.getIntensity() > 4){
			c1.setBurning(true);
			c2.setBurning(true);
			c3.setBurning(true);
			c4.setBurning(true);
		}
		else if(f.getIntensity() < 4 && f.getIntensity() > 1){
			c1.setBurning(true);
			c2.setBurning(true);
		}
		else{
			c1.setBurning(true);
		}
	}

	public Map<String,Double> calculateClosestFighters (Position p){
		Map<String,Double> distFightersMap = new HashMap<>();

		for (String fighterID: fighters.keySet()) {
			double distance = p.distanceBetweenTwoPositions(fighters.get(fighterID).getPos());
			distFightersMap.put(fighterID, distance);
		}

		final Map<String, Double> sortedByDistance = distFightersMap.entrySet()
						.stream()
						.sorted((Map.Entry.<String,Double>comparingByValue()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));

		return  sortedByDistance;
	}

	public void changeFighterData(String fname,FighterInfo f){
		fighters.put(fname,f);
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
		this.fighters 	= new HashMap<String, FighterInfo>();
		this.fires 		= new HashMap<Integer, Fire>();
		
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
					c = new Cell(pos, true, true); 	//Water and fuel
				else if(tmp < 3)
					c = new Cell(pos, true, false); //Just water
				else if(tmp < 5)
					c = new Cell(pos, false, true); //Just fuel
				else				
					c = new Cell(pos, false, false); //Nothing
				
				map.put(pos, c);
			}
	}
}
