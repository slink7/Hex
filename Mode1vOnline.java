package Hex;

import java.awt.Point;

import java.awt.event.*;

public class Mode1vOnline extends Board {

    Mode1vOnline() {
        addMouseListener(this);
        while(true){
            //Get from channel left
            String play = "zrgf";

            if(play.equals("SWAP")){
                if(!swap()) {/* on a gagné */}
            }else{
                
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print(playCount + " " + playCount%2 + " \n");
        if(playCount%2 == 0){
            Point playPos = screenToIndex(getMouse());
            if(play(playPos)){
                //Send to channel right
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
