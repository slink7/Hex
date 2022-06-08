package Hex;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ModeAI extends Board {

    ModeAI() {
        addMouseListener(this);
    }

    public class Collapser {
        Tile pos0, pos1;
        int triggerStatus;

        Collapser(Tile p0, Tile p1, int sta){
            pos0 = p0;
            pos1 = p1;
            triggerStatus = sta;
        }

        public Point collapse() {
            if(pos0 == null || pos1 == null) return null;
            if(pos0.status == triggerStatus) return pos1.pos;
            else if( pos1.status == triggerStatus) return pos0.pos;
            return null;
        }

        public Point forceCollapse(){
            if(pos0 == null || pos1 == null) return null;
            if(Math.random() > 0.5) return pos0.pos;
            return pos1.pos;
        }

        public boolean isNull(){
            return (pos0 == null || pos1 == null);
        }
    } 

    ArrayList<Collapser> collapsers = new ArrayList<>();
    ArrayList<Tile> connecters = new ArrayList<>();

    Tile[] lastTwo = new Tile[3];
    Point playVec;

    int AIstatus = 2;
    int advStatus = 1;
    int AIPlayer = 2;

    boolean directionUp = true;

    boolean debug = true;

    public void checkCollapser(Tile t, Point p0, Point p1, Point p2, int s){
        if(!isInbetween(new Point(0,0), p0, arraySize)){
            if((p0.x == -1 && t.status == bases[3].status) || (p0.x == 11 && t.status == bases[1].status) || (p0.y == -1 && t.status == bases[0].status) || (p0.y == 11 && t.status == bases[2].status)) collapsers.add(new Collapser(getTile(p1), getTile(p2), s));
            return;
        }
        if(t.status == getTile(p0).status) collapsers.add(new Collapser(getTile(p1), getTile(p2), s));
    }

    public void checkCollapsers(Tile t){
        int tx = t.pos.x, ty = t.pos.y;
        checkCollapser(t, new Point(tx + 1, ty + 1), new Point(tx + 1, ty), new Point(tx, ty + 1), advStatus);
        checkCollapser(t, new Point(tx - 1, ty + 2), new Point(tx - 1, ty + 1), new Point(tx, ty + 1), advStatus);
        checkCollapser(t, new Point(tx - 2, ty + 1), new Point(tx - 1, ty), new Point(tx - 1, ty + 1), advStatus);
        checkCollapser(t, new Point(tx - 1, ty - 1), new Point(tx - 1, ty), new Point(tx, ty - 1), advStatus);
        checkCollapser(t, new Point(tx + 1, ty - 2), new Point(tx, ty - 1), new Point(tx + 1, ty - 1), advStatus);
        checkCollapser(t, new Point(tx + 2, ty - 1), new Point(tx + 1, ty), new Point(tx + 1, ty - 1), advStatus);
    }

    public void checkConnector(Tile t, Point p0, Point p1){
        if(!isInbetween(new Point(0,0), p0, arraySize)) return;
        if(t.status == getTile(p0).status) connecters.add(getTile(p1));
    }

    public void checkConnectors(Tile t){
        int tx = t.pos.x, ty = t.pos.y;
        checkConnector(t, new Point(tx+2, ty), new Point(tx+1,ty));
        checkConnector(t, new Point(tx, ty+2), new Point(tx,ty+1));
        checkConnector(t, new Point(tx+2, ty+2), new Point(tx+1,ty+1));
        checkConnector(t, new Point(tx-2, ty), new Point(tx-1,ty));
        checkConnector(t, new Point(tx, ty-2), new Point(tx,ty-1));
        checkConnector(t, new Point(tx+2, ty-2), new Point(tx+1,ty-1));
    }

    public boolean iplay(Point p){
        if(play(p)){
            checkCollapsers(getTile(p));
            checkConnectors(getTile(p));
            return true;
        }
        return false;
    } 

    public boolean AIPlay(){

        //Priorité a la resolution des collapsers
        for (Collapser c : collapsers) {
            Point p = c.collapse();
            if(p != null) {
                if(iplay(p)){
                    if(debug) log("AIPlay wave collapse");
                    collapsers.remove(c);
                    return true;
                }
            }
        }

        //Connecte les trous de 1
        if(connecters.size() > 0 && iplay(connecters.get(0).pos)) { if(debug) log("AIPlay connect"); connecters.remove(0); return true; }

        //Attaque
        int initDir = 1;
        for(int k = 0;k<2;k++){
            int dirInt = (initDir + k*2)%4;
            Point bestTile = Pathfinder.findDeepest(bases[(dirInt+2)%4], dirInt).pos;
            //log("attack toward "+Pathfinder.dirToString(dirInt)+" "+bestTile.x+" "+bestTile.y+" "+dirPoint.x+" "+dirPoint.y+" ["+(bestTile.x+dirPoint.x)+";"+(bestTile.y+dirPoint.y)+"]");
            Point[] dirPoints = Pathfinder.dirToPoints(dirInt);
            log(" bt : " + bestTile.x + " " + bestTile.y);
            for(int l = 0;l<dirPoints.length;l++)
                if(iplay(new Point(bestTile.x + dirPoints[l].x ,bestTile.y + dirPoints[l].y))) { if(debug) log("IAPlay attack" + Pathfinder.dirToString(dirInt)); return true; }
        }

        //Counter le joueur
        if(AIPlayer == 2){
            if(!directionUp){
                if(iplay(new Point(lastPlay.x - 1, lastPlay.y + 2))) { if(debug) log("AIPlay counter down"); return true; }
                else if (iplay(new Point(lastPlay.x + 1, lastPlay.y - 2))) { if(debug) log("AIPlay counter up"); return true; }
            }else{
                if (iplay(new Point(lastPlay.x + 1, lastPlay.y - 2))) { if(debug) log("AIPlay counter up"); return true; }
                else if(iplay(new Point(lastPlay.x - 1, lastPlay.y + 2))) { if(debug) log("AIPlay counter down"); return true; }
            }
        }

        //Dernier recours logique
        for(int k = 0;k<collapsers.size();k++){
            if(collapsers.get(k).isNull()) { collapsers.remove(k); continue; }
            if(iplay(collapsers.get(k).forceCollapse())) { if(debug) log("AIPlay wave force collapse"); collapsers.remove(k); return true; }
        }

        //Dernier recours
        for(int k = 0;k<arraySize.x*arraySize.y;k++) {
            if(iplay(new Point(k%arraySize.x,k/arraySize.y))) { if(debug) log("AIPlay random"); return true; }
        }

        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(playCount%2 == 0){
            if(play(screenToIndex(getMouse()))){
                log("NEW ROUND =======================");
                repaint();
                lastTwo[2] = lastTwo[1];
                lastTwo[1] = lastTwo[0];
                lastTwo[0] = getTile(screenToIndex(getMouse()));
                AIPlay();
            }
            repaint();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
    
}

