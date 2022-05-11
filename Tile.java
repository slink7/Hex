package Hex;

import java.awt.*;
import java.util.ArrayList;

public class Tile {
    Point pos;
    int status = 0;

    //PathFinding
    ArrayList<Tile> neighbors = new ArrayList<>();
    Tile parent;
    Point position;
    int cost, heuristique, sum;

    Tile(Point p){
        pos = p;
    }
    Tile(Point p, int status){
        pos = p;
        this.status = status;
    }
}
