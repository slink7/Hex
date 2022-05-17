package Hex;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ModeDebug extends Board  {

    ModeDebug() {
        addMouseListener(this);
        fill(); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        Point p = screenToIndex(getMouse());
        if(isInbetween(new Point(0,0), p, arraySize)){
            array[p.x][p.y].status = (array[p.x][p.y].status + 1)%3;
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
