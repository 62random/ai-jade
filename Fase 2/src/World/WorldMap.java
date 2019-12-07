package World;
import Graphics.Configs;
import Main.MainContainer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class WorldMap implements Serializable {
	
	private Map<Position, Cell> 	map;
	private int 					dimension;
	private Map<String,FighterInfo> fighters;
	private Map<Position,Fire> 		fires;
	private MainContainer			container;
	
	
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

	public Map<Position, Fire> getFires() {
		return fires;
	}

	public void setFires(Map<Position, Fire> fires) {
		this.fires = fires;
	}

	public void addFire(Fire f) {
		changeCellStatus(f.getPos(),true);
		fires.put(f.getPos(), f);
	}

	public void changeCellStatus (Position p, boolean burning){
		Cell c = map.get(p);
		c.setBurning(burning);
		map.put(p,c);
	}

	public void propagateFire(){

		if(fires.size() > 0) {

			Random generator = new Random();
			Object[] values = fires.values().toArray();
			Fire f = (Fire) values[generator.nextInt(values.length)];

			ArrayList<Cell> cells = new ArrayList<Cell>();
			cells.add(map.get(f.getPos().getAdjacentUp()));
			cells.add(map.get(f.getPos().getAdjacentDown()));
			cells.add(map.get(f.getPos().getAdjacentRight()));
			cells.add(map.get(f.getPos().getAdjacentLeft()));

			int prop = 0;
			if (f.getIntensity() > 4) {
				prop = 4;
			} else if (f.getIntensity() > 1) {
				prop = 2;
			} else {
				prop = 1;
			}

			for(int i = 0; i < prop; i++){
				Cell c = cells.remove(generator.nextInt(cells.size()));
				if(!c.isBurning())
					container.newFire(c.getPos());
			}

			System.out.println("Fire on position " + f.getPos() + " has propagated");
		}

	}

	public Map<String,Double> calculateClosestFighters (Position p){
		Map<String,Double> distFightersMap = new HashMap<>();

		for (String fighterID: fighters.keySet()) {
			double distance = p.distance(fighters.get(fighterID).getPos());
			distFightersMap.put(fighterID, distance);
		}

		final Map<String, Double> sortedByDistance = distFightersMap.entrySet()
						.stream()
						.sorted((Map.Entry.<String,Double>comparingByValue()))
						.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		System.out.println(sortedByDistance.toString());
		return  sortedByDistance;
	}

	public void changeFighterData(String fname,FighterInfo f){
		fighters.put(fname,f);
	}

	public Fire extinguishFire(Fire f){
		if (f == null)
			return null;

		ArrayList<Fire> fs = new ArrayList<Fire>();

		for(Fire fire : fires.values())
			if (fire.getPos().equals(f.getPos())) {
				fire.extinguish();
				fs.add(fire);
				map.get(fire.getPos()).setBurning(false);
			}
		for (Fire t:
			 fs) {
			fires.remove(t.getPos(), t);
		}
		return f;
	}

	public WorldMap(int dim, MainContainer container) {
		this.dimension 	= dim;
		this.map 		= new TreeMap<Position, Cell>(Position.getComparator());
		this.fires 		= new TreeMap<Position, Fire>(Position.getComparator());
		this.fighters 	= new HashMap<String, FighterInfo>();
		this.container 	= container;


		Position pos;
		Cell c;
		Random r = new Random();

		int tmp;
		
		for(int i = 0; i < dimension; i++)
			for(int j = 0; j < dimension; j++) {
				pos = new Position(i, j);

				tmp = r.nextInt(100);
				if(tmp < Configs.PERCENT_FUEL_WATER) {
					c = new Cell(this, pos, true, true);    //Water and fuel
				}
				else if(tmp < Configs.PERCENT_FUEL_WATER + Configs.PERCENT_WATER) {
					c = new Cell(this, pos, true, false); //Just water
				}
				else if(tmp < Configs.PERCENT_FUEL_WATER + Configs.PERCENT_WATER + Configs.PERCENT_FUEL) {
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
