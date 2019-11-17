package World;
import java.util.TreeMap;
import java.util.Random;
import java.util.Map;

public class WorldMap {
	
	private Map<Position, Cell> map;
	private int 				dimension;
	
	
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

	public WorldMap(int dim) {
		this.map = new TreeMap<Position, Cell>();
		this.dimension = dim;
		
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
