package Hex;

import java.awt.Point;
import java.awt.event.*;
import javax.swing.JOptionPane;

public class ModeOnline extends Board {

    String suffixLeft = "_left", suffixRight = "_right";

    Channel channel_send, channel_receive;
    String channel_name;

    int startPlayer = 0;

    public void bobo() {
        log("Waiting for message...");
        String play = channel_receive.getNext();
        if(play != null) log("Received \"" + play + "\""); else return;
        if(playCount%2 == startPlayer) /* on a gagné */return;
        if(play.equals("SWAP")){
            if(!swap()) {/* on a gagné */}
        }else{
            String[] words = play.split("\\W+");
            Point playPos = new Point(Integer.parseInt(words[0]), Integer.parseInt(words[1]));
            if(!play(playPos)) { /* on a gagné */}
        }
        repaint();
    }

    ModeOnline() {

        Object[] options = {"P1", "P2", "Cancel"};
        startPlayer = JOptionPane.showOptionDialog(null, "What player are you ?", "Player Selection", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null,options,options[2]);
        channel_name = JOptionPane.showInputDialog("What channel ?");

        switch(startPlayer){
        case 0:
            channel_send = new Channel(channel_name+suffixLeft);
            channel_receive = new Channel(channel_name+suffixRight);
            break;
        case 1:
            channel_send = new Channel(channel_name+suffixRight);
            channel_receive = new Channel(channel_name+suffixLeft);
            break;
        default:
            return;
        }
        log("Channel details ---\nSend on : " + channel_send.getName() + "\nReceive on : " + channel_receive.getName() + "\n");

        log("Connection .",0);
        channel_send.connect();
        log(" .",1);
        channel_receive.connect();
        log(" . Done.", 2);
        
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if(playCount%2 == startPlayer){
            Point playPos = screenToIndex(getMouse());
            if(play(playPos)){
                String message = playPos.x + " " + playPos.y;
                log("Sent \"" + message + "\" \n");
                channel_send.send(message);
            }else if(playPos.equals(swapButtonPosition)){
                String message = "SWAP";
                log("Sent \"" + message + "\" \n");
                channel_send.send(message);
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
