package Hex;

import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

public abstract class Board extends JPanel implements MouseInputListener {

    //
    Point firstPlay;

    //Variables plateau
    Point arraySize = new Point(11,11);
    Tile[][] array = new Tile[arraySize.x][arraySize.y];
    Tile[] bases = new Tile[] {
        new Tile(new Point(arraySize.x/2, -1), 1),
        new Tile(new Point(-1, arraySize.y/2), 2),
        new Tile(new Point(arraySize.x/2, arraySize.y), 1),
        new Tile(new Point(arraySize.x, arraySize.y/2), 2)
    };

    //Tile var
    RegularPolygon hexagon = new RegularPolygon(24, 6, 90.0);

    //Render Values
    Color colorShift = new Color(32,32,32);
    Dimension offset = new Dimension(70,70);
    int strokeSize = 2;

    //Player array
    Player[] players = new Player[]{
        new Player(0, new Color(192,192,192)),
        new Player(1, new Color(0,128,128)),
        new Player(2, new Color(128,0,128))
    };

    //Player values
    int playCount = 0;

    public void fill(){
        for(int k = 0;k<array.length;k++){
            for(int l = 0;l<array[k].length;l++){
                array[k][l].status = (int) Math.ceil(Math.random()*3.0) - 1;
            }
        }
    }

    public void fill(Player p){
        for(int k = 0;k<array.length;k++){
            for(int l = 0;l<array[k].length;l++){
                array[k][l].status = p.ID;
            }
        }
    }

    Board(){
        setPreferredSize(new Dimension(1024,512));
        System.out.print("new " + this.getClass().toString()+"\n");
        for(int k = 0;k<array.length;k++){
            for(int l = 0;l<array[k].length;l++){
                array[k][l] = new Tile(new Point(k,l));
                array[k][l].position = indexToScreen(array[k][l].pos);
            }
        }
        // fill();
    }

    public boolean play(Point p){
        if(isInbetween(0, p.x, arraySize.x-1) && isInbetween(0, p.y, arraySize.y-1) && array[p.x][p.y].status == players[0].ID){
            if(playCount == 0) firstPlay = p;
            array[p.x][p.y].status = (playCount++ % 2) + 1;
            return true;
        }
        return false;
    }

    public boolean swap(){
        if(playCount == 1){
            array[firstPlay.y][firstPlay.x].status = array[firstPlay.x][firstPlay.y].status;
            array[firstPlay.x][firstPlay.y].status = 0;
            return true;
        }
        return false;
    }

    public Point getMouse(){
        Point p = MouseInfo.getPointerInfo().getLocation();
        Point wp = getLocationOnScreen();
        return new Point(p.x-wp.x,p.y-wp.y);
    }

    /*
        Methodes d'affichage
    */
    private void clearScreen(Graphics2D g2D){
        //Efface l'ecran
        g2D.setColor(players[0].color);
        g2D.fillRect(0, 0, getWidth(), getHeight());
    }

    public void drawBases(Graphics2D g2D){
        Point p00 = indexToScreen(new Point(-1, -1)), p10 = indexToScreen(new Point(arraySize.x, -1));
        Point p01 = indexToScreen(new Point(-1, arraySize.y)), p11 = indexToScreen(new Point(arraySize.y, arraySize.y));
        g2D.setStroke(new BasicStroke(2));
        g2D.setColor(sumColors(players[1].color, colorShift));
        g2D.fillPolygon(new int[]{p00.x,p10.x,p01.x,p11.x}, new int[]{p00.y,p10.y,p01.y,p11.y}, 4);
        g2D.setColor(sumColors(players[2].color, colorShift));
        g2D.fillPolygon(new int[]{p00.x,p01.x,p10.x,p11.x}, new int[]{p00.y,p01.y,p10.y,p11.y}, 4);
        g2D.setColor(new Color(0,0,0));
        g2D.drawLine(p00.x, p00.y, p11.x, p11.y);
        g2D.drawLine(p10.x, p10.y, p01.x, p01.y);
    }

    private void drawCoords(Graphics2D g2D){
        g2D.setColor(new Color(255,255,255));
        //Affiche les coordonnées horizontales
        for(int k = 0;k<arraySize.x;k++){
            Point p0 = indexToScreen(new Point(k, -1)), p1 = indexToScreen(new Point(k, arraySize.y));
            g2D.drawString( ""+ (char) (k+65) , p0.x-4, p0.y+12);
            g2D.drawString( ""+ (char) (k+65) , p1.x-4, p1.y-4);
        }
        //Affiche les coordonnées verticales
        for(int l = 0;l<arraySize.y;l++){
            Point p0 = indexToScreen(new Point(-1, l)), p1 = indexToScreen(new Point(arraySize.x, l));
            g2D.drawString( ""+l , p0.x+4, p0.y+4);
            g2D.drawString( ""+l , p1.x-10, p1.y+4);
        }
    }

    public void drawFancyPoly(Graphics2D g2D, int[] x, int[] y, Color c){
        g2D.setColor(c);
        g2D.fillPolygon(x, y, x.length);
        g2D.setColor(sumColors(colorShift, c));
        g2D.drawPolygon(x, y, x.length);
    }

    public void drawBoard(Graphics2D g2D){
        g2D.setStroke(new BasicStroke(2*strokeSize));
        for(int k = 0;k<array.length;k++){
            for(int l = 0;l<array[k].length;l++){
                Point currentScreen = indexToScreen(new Point(k,l));
                drawFancyPoly(g2D, hexagon.getXs(currentScreen.x), hexagon.getYs(currentScreen.y), players[array[k][l].status].color);
            }
        }
    }

    public void paint(Graphics g){
        Graphics2D g2D = (Graphics2D) g;
        clearScreen(g2D);
        drawBases(g2D);
        drawCoords(g2D);
        drawBoard(g2D);
    }

    public void updateNetwork(){
        for(int l = 0;l<arraySize.y;l++) { bases[1].neighbors.add(array[arraySize.x-1][l]); bases[3].neighbors.add(array[0][l]);}
        for(int k = 0;k<arraySize.x;k++){
            bases[0].neighbors.add(array[k][0]); bases[2].neighbors.add(array[k][arraySize.y-1]);
            for(int l = 0;l<arraySize.y;l++) {
                if(k == 0) { array[k][l].neighbors.add(bases[3]); }
                if(l == 0) { array[k][l].neighbors.add(bases[0]); }
                if(k == arraySize.x-1) { array[k][l].neighbors.add(bases[1]); }
                if(l == arraySize.y-1) { array[k][l].neighbors.add(bases[2]); }

                if(k>0){ array[k][l].neighbors.add(array[k-1][l]); }
                if(k<arraySize.x-1){ array[k][l].neighbors.add(array[k+1][l]); }
                
                if(l>0){ array[k][l].neighbors.add(array[k][l-1]); }
                if(l<arraySize.y-1){ array[k][l].neighbors.add(array[k][l+1]); }

                if(k<arraySize.x-1 && l >0){ array[k][l].neighbors.add(array[k+1][l-1]); }
                if(k>0 && l <arraySize.y-1){ array[k][l].neighbors.add(array[k-1][l+1]); }
            }
        }
    }

    /*
        Methodes de conversion
    */
    protected Point indexToScreen(Point p) {                                                                               //[hexagon.inscribedRadius] surplus pour centrer les tiles
        int nx = p.x*2*(hexagon.inscribedRadius + strokeSize) + p.y * (hexagon.inscribedRadius + strokeSize) + offset.width + hexagon.inscribedRadius;
        int ny = (int)Math.round(p.y*1.5*(hexagon.circumscribedRadius+strokeSize)) + offset.height;
        return new Point(nx, ny);
    }
    protected Point screenToIndex(Point p) {
        int ny = (int) Math.round((p.y-offset.height)/(1.5*(hexagon.circumscribedRadius+strokeSize)));
        int nx = (p.x-offset.width-ny*(hexagon.inscribedRadius+strokeSize))/(2*(hexagon.inscribedRadius+strokeSize));
        return new Point(nx, ny);
    }

    /*
        Operateurs
    */
    public static boolean isInbetween(int min, int val, int max){
        return (min <= val) && (val <= max);
    }
    public static boolean isInbetween(Point min, Point val, Point max){
        return isInbetween(min.x, val.x, max.x) && isInbetween(min.y, val.y, max.y);
    }
    public static int clip(int val, int max){
        return (val+max)%max; 
    }
    public static Color sumColors(Color c0, Color c1){
        return new Color(clip(c0.getRed()+c1.getRed(), 255), clip(c0.getGreen()+c1.getGreen(), 255), clip(c0.getBlue()+c1.getBlue(), 255));
    }
    public static Color subColors(Color c0, Color c1){
        return new Color(clip(c0.getRed()-c1.getRed(), 255), clip(c0.getGreen()-c1.getGreen(), 255), clip(c0.getBlue()-c1.getBlue(), 255));
    }
    public static Point getRandomPoint(Point min, Point max){
        return new Point((int)(Math.random()*(max.x-min.x)+min.x),(int)(Math.random()*(max.y-min.y)+min.y));
    }

    /* 
        Methodes de debug
    */
    public static void log(String s){
        System.out.print("["+LocalDateTime.now().getHour()+":"+LocalDateTime.now().getMinute()+":"+LocalDateTime.now().getSecond()+"] "+s+"\n");
    }
}