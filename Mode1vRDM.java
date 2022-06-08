package Hex;

import java.awt.*;
import java.awt.event.*;

public class Mode1vRDM extends Board {

    Mode1vRDM() {
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print(playCount + " " + playCount%2 + " \n");
        if(playCount%2 == 0){
            if(play(screenToIndex(getMouse()))){
                repaint();
                Point p0 = getRandomPoint(new Point(0,0), arraySize);
                int tentatives = 0;
                while(!play(p0)){
                    p0 = getRandomPoint(new Point(0,0), arraySize);
                    if(tentatives++ > 1000) break;
                }
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
