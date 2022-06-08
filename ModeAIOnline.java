package Hex;

import javax.swing.JOptionPane;
import java.awt.*;
import java.util.ArrayList;

public class ModeAIOnline extends Board {
    
    int startPlayer;
    String channel_name;
    String suffixLeft = "_left", suffixRight = "_right";
    Channel channel_send, channel_receive;

    int AIstatus = 2;
    int advStatus = 1;
    int attackDir = 1;

    ModeAIOnline() {

        //addMouseListener(this);

        Object[] options = {"P1", "P2", "Cancel"};
        startPlayer = JOptionPane.showOptionDialog(null, "What player are you ?", "Player Selection", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[2]);
        channel_name = JOptionPane.showInputDialog("What channel ?");
        if(startPlayer == 0){
            
        }else if(startPlayer == 1){
            
        }
        switch(startPlayer){
        case 0:
            AIstatus = 1;
            advStatus = 2;
            attackDir = 2;
            
            channel_send = new Channel(channel_name+suffixLeft);
            channel_receive = new Channel(channel_name+suffixRight);
            break;
        case 1:
            channel_send = new Channel(channel_name+suffixRight);
            channel_receive = new Channel(channel_name+suffixLeft);

            AIstatus = 2;
            advStatus = 1;
            attackDir = 1;

            break;
        default:
            log("???");
            return;
        }
        log("Channel details ---\n Send on : " + channel_send.getName() + "\n Receive on : " + channel_receive.getName() + "\n");
    
        log("Connection .",0);
        channel_send.connect();
        log(" .",1);
        channel_receive.connect();
        log(" . Done.", 2);

        if(startPlayer == 0) {
            AIPlay();
        }
            
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
            String msg = ""+p.x+" "+p.y;
            log("Sent : \"" + msg+"\"");
            channel_send.send(msg);
            checkCollapsers(getTile(p));
            checkConnectors(getTile(p));
            return true;
        }
        return false;
    } 

    public boolean AIPlay(){

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
        int initDir = attackDir;
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
        if(Math.random() > 0.5){
            Point[] dirPoints = Pathfinder.dirToPoints((attackDir+1)%4);
            for(int l = 0;l<dirPoints.length;l++)
                if(iplay(new Point(lastPlay.x + dirPoints[l].x ,lastPlay.y + dirPoints[l].y))) { if(debug) log("IAPlay attack" + Pathfinder.dirToString((attackDir+1)%4)); return true; }
            dirPoints = Pathfinder.dirToPoints((attackDir+3)%4);
            for(int l = 0;l<dirPoints.length;l++)
                if(iplay(new Point(lastPlay.x + dirPoints[l].x ,lastPlay.y + dirPoints[l].y))) { if(debug) log("IAPlay attack" + Pathfinder.dirToString((attackDir+1)%4)); return true; }
        }else{
            Point[] dirPoints = Pathfinder.dirToPoints((attackDir+3)%4);
            for(int l = 0;l<dirPoints.length;l++)
                if(iplay(new Point(lastPlay.x + dirPoints[l].x ,lastPlay.y + dirPoints[l].y))) { if(debug) log("IAPlay attack" + Pathfinder.dirToString((attackDir+1)%4)); return true; }
            dirPoints = Pathfinder.dirToPoints((attackDir+1)%4);
            for(int l = 0;l<dirPoints.length;l++)
                if(iplay(new Point(lastPlay.x + dirPoints[l].x ,lastPlay.y + dirPoints[l].y))) { if(debug) log("IAPlay attack" + Pathfinder.dirToString((attackDir+1)%4)); return true; }
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
    
    public void wait_msg() {
        log("Waiting for message...");
        String play = channel_receive.getNext();
        if(play != null) log("Received \"" + play + "\""); else return;
        if(playCount%2 == startPlayer) /* on a gagné */ return;
        if(play.equals("SWAP")){
            if(!swap()) {/* on a gagné */}
        }else{
            String[] words = play.split("\\W+"); // sépare la String 'play' en 
            Point playPos = new Point(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            if(!play(playPos)) { /* on a gagné */}
        }
        repaint();
        AIPlay();
    }
    
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e) {}

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseEntered(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseExited(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseDragged(java.awt.event.MouseEvent e) {}

    @Override
    public void mouseMoved(java.awt.event.MouseEvent e) {}
}
