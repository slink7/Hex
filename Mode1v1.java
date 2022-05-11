package Hex;

//import java.awt.*;
import java.awt.event.*;

public class Mode1v1 extends Board {

    Mode1v1() {
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print("Play success : " + play(screenToIndex(getMouse())) + "\n");
        repaint();

        System.out.print("Swap success : " + swap() + "\n");
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
