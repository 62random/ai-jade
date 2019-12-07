package World;
import Graphics.Configs;
import Main.MainContainer;

import java.io.ObjectInputFilter;
import java.io.Serializable;
import java.lang.reflect.Array;
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
			if (f.getPos().getAdjacentUp() != null)
				cells.add(map.get(f.getPos().getAdjacentUp()));
			if (f.getPos().getAdjacentDown() != null)
				cells.add(map.get(f.getPos().getAdjacentDown()));
			if (f.getPos().getAdjacentRight() != null)
				cells.add(map.get(f.getPos().getAdjacentRight()));
			if (f.getPos().getAdjacentLeft() != null)
				cells.add(map.get(f.getPos().getAdjacentLeft()));

			int prop = 0;
			if (f.getIntensity() > 4) {
				prop = 4;
			} else if (f.getIntensity() > 1) {
				prop = 2;
			} else {
				prop = 1;
			}

			for(int i = 0; i < prop && cells.size() > 0; i++){
				Cell c = cells.remove(generator.nextInt(cells.size()));
				if(c != null && !c.isBurning())
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

		fillCellResourceInfo();
	}

	private void fillCellResourceInfo() {
		Position p;
		Cell aux;
		int d, x, y;
		for (Cell c: map.values()) {
			p = c.getPos();
			for(int i = -Configs.TRUCK_FUEL; i < Configs.TRUCK_FUEL; i++)
				for(int j = -Configs.TRUCK_FUEL; j < Configs.TRUCK_FUEL; j++) {
					d = Math.abs(i) + Math.abs(j);
					x = p.getX() + i;
					y = p.getY() + j;
					if( 0 < x && x < Configs.MAP_SIZE && 0 < y && y < Configs.MAP_SIZE)
						if(0 < d && d < Configs.TRUCK_FUEL){
							aux = map.get(new Position(x, y));
							if(aux.isFuel())
								c.getPaths().getPaths().put(aux.getPaths(), d);
						}
				}
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

	public boolean inRange(FighterInfo fighterInfo, ArrayList<Position> poss) {

		Position current = poss.get(poss.size()-1);
		if(current.steps(fighterInfo.getPos()) < fighterInfo.getCurrentFuel() / 2)
			return true;
		else {
			boolean ret = false;
			ArrayList<Position> nextit;
			for (Map.Entry<Path, Integer> e : map.get(current).getPaths().getPaths().entrySet()){
				if(e.getKey().getPos().notIn(poss) && e.getValue() <= fighterInfo.getFuelCapacity()) {
					nextit = new ArrayList<>(poss);
					nextit.add(e.getKey().getPos());
					ret = ret || inRange(fighterInfo, nextit);
				}
			}
			return ret;
		}
	}

	public  void fillStepsTable(TreeMap<Position, Integer> steps, Path p, int fuel){
		int current_steps;
		if(steps.get(p.getPos()) == null)
			current_steps = 0;
		else
			current_steps = steps.get(p.getPos());

		for(Map.Entry<Path, Integer> e : p.getPaths().entrySet()) {
			if(e.getValue() < fuel) {
				if(steps.get(e.getKey().getPos()) == null || steps.get(e.getKey().getPos()) > current_steps + 1){
					steps.put(e.getKey().getPos(), current_steps + 1);
					fillStepsTable(steps, e.getKey(), fuel);
				}
			}
		}
	}

	public Position getCheckpoint(FighterInfo fighterInfo, Position destination) {
		TreeMap<Position, Integer> steps = new TreeMap<Position, Integer>(Position.getComparator());
		Path current = map.get(destination).getPaths();
		int min = Configs.MAP_SIZE;
		for( int i : current.getPaths().values())
			if (i < min)
				min = i;
		if(current.getPos().steps(fighterInfo.getPos()) + min < fighterInfo.getCurrentFuel())
			return current.getPos();

		fillStepsTable(steps, current, fighterInfo.getFuelCapacity());

		Position ret = destination;
		min = 100;
		for(Map.Entry<Position, Integer> e : steps.entrySet()) {
			if(!e.getKey().equals(fighterInfo.getPos()) && e.getKey().steps(fighterInfo.getPos()) < fighterInfo.getCurrentFuel())
				if(destination.steps(e.getKey()) < min) {
					min = destination.steps(e.getKey());
					ret = e.getKey();
				}
		}
		return ret;
	}

	public Position getNearestFuel(FighterInfo fighterInfo) {
		int min = Configs.MAP_SIZE;
		Position ret = fighterInfo.getPos();

		for (Map.Entry<Path, Integer> e : map.get(fighterInfo.getPos()).getPaths().getPaths().entrySet()) {
			if (e.getValue() < fighterInfo.getCurrentFuel() && e.getValue() < min) {
				min = e.getValue();
				ret = e.getKey().getPos();
			}
		}

		return  ret;
	}
}
