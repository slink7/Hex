package Hex;

import java.util.ArrayList;
import java.awt.*;

public class Pathfinder {

    public static String dirToString(int dir){
        switch(dir%4){
            case 0: return "Up";
            case 1: return "Right";
            case 2: return "Down";
            case 3: return "Left";
            default: Board.log("???wtf dirToString()"); return "[invalid dir]";
        }
    }

    public static Point dirToPoint(int dir){
        switch(dir%4){
            case 0: return new Point(0,-1);
            case 1: return new Point(1, 0);
            case 2: return new Point(0,1);
            case 3: return new Point(-1,0);
            default: Board.log("???wtf dirToPoint()"); return new Point(0,0);
        }
    }

    public static Point[] dirToPoints(int dir){
        switch(dir%4){
            case 0: return new Point[]{new Point(1,-2), new Point(-1,-1), new Point(2,-1)};
            case 1: return new Point[]{new Point(2,-1), new Point(1,1), new Point(1,-2)};
            case 2: return new Point[]{new Point(-1,2), new Point(1,1), new Point(-2,1)};
            case 3: return new Point[]{new Point(-2,1), new Point(-1,2), new Point(-1,-1)};
            default: Board.log("???wtf dirToPoints()"); return null;
        }
    }

    /*
     * public static Point[] dirToPoints(int dir){
        switch(dir%4){
            case 0: return new Point[]{new Point(-1,-1), new Point(1,-2), new Point(2,-1)};
            case 1: return new Point[]{new Point(1,-2), new Point(2,-1), new Point(1,1)};
            case 2: return new Point[]{new Point(1,1), new Point(-1,2), new Point(-2,1)};
            case 3: return new Point[]{new Point(-1,2), new Point(-2,1), new Point(-1,-1)};
            default: Board.log("???wtf dirToPoints()"); return null;
        }
    }
     */

    public static int getDepth(Point A, int dir){
        switch(dir%4){
            case 0: return -A.y;
            case 1: return  A.x;
            case 2: return  A.y;
            case 3: return -A.x;
            default: Board.log("???wtf getDepth()"); return 0;
        }
    }

    public static Tile findDeepest(Tile A, int dir){
        Board.log("fD" + A.toString() + " " + dirToString(dir));
        Tile out = findDeepestRecursive(A, dir, true, A.status, new ArrayList<>());
        return out;
    }

    public static Tile findDeepestRecursive(Tile A, int dir, boolean canJump, int status, ArrayList<Tile> visited){
        Tile best = A;
        int depth = -Integer.MAX_VALUE;
        visited.add(A);
        for(int k = 0;k<A.neighbors.size();k++){
            if(visited.contains(A.neighbors.get(k))) continue;
            if(canJump || status == A.neighbors.get(k).status){
                Board.log("["+A.pos.x+" "+A.pos.y+"]["+A.neighbors.get(k).pos.x+" "+A.neighbors.get(k).pos.y+"] J:"+Board.boolToString(canJump)+" S:"+Board.boolToString(A.status != A.neighbors.get(k).status)+" V:"+Board.boolToString(visited.contains(A.neighbors.get(k))));
                //if(!(canJump || A.status == A.neighbors.get(k).status) || visited.contains(A.neighbors.get(k))) continue;
                Tile current = findDeepestRecursive(A.neighbors.get(k), dir, status == A.neighbors.get(k).status, status, visited);
                int e = getDepth(current.pos, dir);
                if(current.status == status && e > depth) { depth = e; best = current; }
            }
            
        }
        return best;
    }

    public static Tile[] findPath(Tile A, Tile B){
        ArrayList<Tile> path = findPathRecursive(A, B, new ArrayList<Tile>(), new ArrayList<Tile>());
        if(path == null) return null;
        
        //Convertis l'ArrayList<Tile> en tableau
        Tile[] t0 = new Tile[path.size()];
        for(int k = 0;k<path.size();k++){
            t0[k] = path.get(k);
        }
        return t0;
    }

    public static ArrayList<Tile> findPathRecursive(Tile start, Tile end, ArrayList<Tile> visited, ArrayList<Tile> path){
        if(start.status != end.status) return null;
        if(start == end) {
            path.add(start);
            return path;
        }
        visited.add(start);
        for(Tile t : start.neighbors){
            if(!visited.contains(t)){
                if(findPathRecursive(t, end, visited, path) != null){
                    path.add(start);
                    return path;
                }
            }
        }
        return null;
    }
}