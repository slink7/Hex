package Hex;

import java.awt.Point;

import java.awt.event.*;

import javax.swing.JOptionPane;

public class Mode1vOnline extends Board {

    String suffixLeft = "_left", suffixRight = "_right";

    Channel channel_send, channel_receive;
    String channel_name;

    int startPlayer = 0;

    public void bobo() {
        log("Waiting for message...\n");
        //Get from channel left
        String play = channel_receive.getNext();
        log("Received " + play + "\n");
        if(play != null) log("Received " + play + "\n");
        if(playCount%2 == startPlayer) return;
        if(play.equals("SWAP")){
            if(!swap()) {/* on a gagné */}
        }else{
            String[] words = play.split("\\W+");
            Point playPos = new Point(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            if(!play(playPos)) { /* on a gagné */}
        }
        repaint();
    }

    Mode1vOnline() {

        Object[] options = {"P1", "P2", "Cancel"};
        startPlayer = JOptionPane.showOptionDialog(null, "What player are you ?", "Player Selection", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[2]);
        channel_name = JOptionPane.showInputDialog("What channel ?");
        if(startPlayer == 0){ // J1
            channel_send = new Channel(channel_name+suffixLeft);
            channel_receive = new Channel(channel_name+suffixRight);
            log("Channel deteils ---\nSend on : " + channel_name+suffixLeft + "\nReceive on : " + channel_name+suffixRight+"\n");
        }
        if(startPlayer == 1){ // J2
            channel_send = new Channel(channel_name+suffixRight);
            channel_receive = new Channel(channel_name+suffixLeft);
            log("Channel deteils ---\nSend on : " + channel_name+suffixRight + "\nReceive on : " + channel_name+suffixLeft+"\n");
        }
        log("Preconnect");
        channel_send.connect();
        channel_receive.connect();
        log("Postconnect");

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(playCount%2 == startPlayer){
            Point playPos = screenToIndex(getMouse());
            if(play(playPos)){
                log("Sent " + playPos.x + " " + playPos.y + " \n");
                channel_send.send(playPos.x + " " + playPos.y);
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
