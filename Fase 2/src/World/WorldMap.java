package World;
import Graphics.Configs;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements Serializable {
	
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
		fighters.put(f.getAID(), f);
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

	public void propagateFire(){

		if(fires.size()>0) {

			Random generator = new Random();
			Object[] values = fires.values().toArray();
			Fire f = (Fire) values[generator.nextInt(values.length)];

			Position p = f.getPos();

			Position position1 = p.getAdjacentRight(p);
			Position position2 = p.getAdjacentLeft(p);
			Position position3 = p.getAdjacentDown(p);
			Position position4 = p.getAdjacentUp(p);

			Cell c1 = map.get(position1);
			Cell c2 = map.get(position2);
			Cell c3 = map.get(position3);
			Cell c4 = map.get(position4);

			if (f.getIntensity() > 4) {
				c1.setBurning(true);
				c2.setBurning(true);
				c3.setBurning(true);
				c4.setBurning(true);
				map.put(position1, c1);
				map.put(position2, c2);
				map.put(position3, c3);
				map.put(position4, c4);
			} else if (f.getIntensity() < 4 && f.getIntensity() > 1) {
				c1.setBurning(true);
				c2.setBurning(true);
				map.put(position1, c1);
				map.put(position2, c2);
			} else {
				c1.setBurning(true);
				map.put(position1, c1);
			}

			System.out.println("Fire on position " + f.getPos() + " has propagated");
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

	public void extinguishFire(Position pos){
		Integer removalKey = null;

		for (Map.Entry<Integer,Fire> entry: fires.entrySet()) {
			if(entry.getValue().getPos().equals(pos)){
				removalKey = entry.getKey();
				break;
			}
		}

		if(removalKey != null) {
			fires.remove(removalKey);
		}
	}

	public WorldMap(int dim) {
		this.map 		= new TreeMap<Position, Cell>(
				new Comparator<Position>() {
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
	            });

		this.dimension 	= dim;
		this.fighters 	= new HashMap<String, FighterInfo>();
		this.fires 		= new HashMap<Integer, Fire>();
		
		Position pos;
		Cell c;
		Random r = new Random();

		int tmp;
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++) {
				pos = new Position(i, j);

				tmp = r.nextInt(100);
				if(tmp < 3) {
					c = new Cell(this, pos, true, true);    //Water and fuel
				}
				else if(tmp < 6) {
					c = new Cell(this, pos, true, false); //Just water
				}
				else if(tmp < 9) {
					c = new Cell(this, pos, false, true); //Just fuel
				}
				else {
					c = new Cell(this, pos, false, false); //Nothing
				}

				map.put(pos, c);
			}
	}

	public ArrayList<Integer> agentsOn(int i, int j) {
		ArrayList<Integer> list = new ArrayList<>();
		for(FighterInfo f : fighters.values())
			if(f.isAt(i,j))
				list.add(f.getType());

		return list;
	}

	public ArrayList<Integer> cellPropertiesOn(int i, int j) {
		ArrayList<Integer> list = new ArrayList<>();
		Position p = new Position(i,j);
		Cell c = map.get(p);

		if(c.isFuel())
			list.add(Configs.CELL_FUEL);

		if(c.isWater())
			list.add(Configs.CELL_WATER);

		if(c.isBurning())
			list.add(Configs.CELL_FIRE);

		return list;
	}
}
