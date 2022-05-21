package Hex;

import java.util.ArrayList;

public class Pathfinder {

    public static Tile[] findPath(Tile A, Tile B){
        ArrayList<Tile> path = findPathRec(A, B, new ArrayList<Tile>(), new ArrayList<Tile>());
        if(path == null) return null;
        Tile[] t0 = new Tile[path.size()];
        for(int k = 0;k<path.size();k++){
            t0[k] = path.get(k);
        }
        return t0;
    }

    public static ArrayList<Tile> findPathRec(Tile start, Tile end, ArrayList<Tile> visited, ArrayList<Tile> path){
        if(start.status != end.status) return null;
        if(start == end) {
            path.add(start);
            return path;
        }
        visited.add(start);
        for(Tile t : start.neighbors){
            if(!visited.contains(t)){
                if(findPathRec(t, end, visited, path) != null){
                    path.add(start);
                    return path;
                }
            }
        }
        return null;
    }

    
}
