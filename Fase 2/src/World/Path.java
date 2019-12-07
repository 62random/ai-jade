package World;

import java.io.Serializable;
import java.util.*;

public class Path implements Serializable {
    private Position            pos;
    private Map<Path, Integer>  paths;
    private WorldMap            map;

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public Map<Path, Integer> getPaths() {
        return paths;
    }

    public void setPaths(Map<Path, Integer> paths) {
        this.paths = paths;
    }

    public Path(WorldMap map, Position pos) {
        this.pos    = pos;
        this.paths  = new TreeMap<Path, Integer>(Path.getComparator());
        this.map    = map;
    }

    private static Comparator<Path> getComparator() {
        return new Comparator<Path>() {
            @Override
            public int compare(Path p1, Path p2) {
                int dx = p1.getPos().getX() - p2.getPos().getX(), dy = p1.getPos().getY() - p2.getPos().getY();
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
