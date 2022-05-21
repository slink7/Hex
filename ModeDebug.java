package Hex;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ModeDebug extends Board  {

    ModeDebug() {
        addMouseListener(this);
        fill(); 

        //Parametre le mode Debug
        drawCurrentPlayer = false;
        endAfterWin = false;        
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        //Change la tile cliquée par la tile suivante (Innocupée -> J1 -> J2 -> Innocupée)
        Point p = screenToIndex(getMouse());
        if(isInbetween(new Point(0,0), p, arraySize)){
            setTile(p,(getTile(p).status + 1)%3);
        }
        repaint();
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
