package Hex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Window extends JFrame {
    
    Board game;
    Window w = this;
    JPanel backGround;

    void decoy() {}

    Window() {
        setTitle("Jeu de hex");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1142,576));
        JPanel bg = new JPanel();
        bg.setPreferredSize(new Dimension(1142,576));
        bg.setSize(new Dimension(1142,512));
        
        JPanel m0 = new JPanel();
        m0.setBackground(new Color(64,64,64));
        m0.setBounds(0,0,128,576);

        JButton b0 = new JButton("Mode 1v1");
        b0.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                backGround.removeAll();
                game = new Mode1v1();
                backGround.add(game);
                pack();
                repaint();
            }
        });
        m0.add(b0);

        JButton b1 = new JButton("Mode Debug");
        b1.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                backGround.removeAll();
                game = new ModeDebug();
                backGround.add(game);
                pack();
                repaint();
            }
        });
        m0.add(b1);

        JButton b2 = new JButton("Mode AI naïve");
        b2.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                backGround.removeAll();
                game = new Mode1vRDM();
                backGround.add(game);
                pack();
                repaint();
            }
        });
        m0.add(b2);

        JButton b3 = new JButton("Mode Online");
        b3.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                backGround.removeAll();
                game = new Mode1vOnline();
                backGround.add(game);
                pack();
                repaint();
            }
        });
        m0.add(b3);

        bg.add(m0);
         backGround = new JPanel();
         backGround.setBounds(128, 0, 1024, 512);;
        backGround.setBackground(new Color(64,64,64));
        bg.add(backGround);
        
        //initMenu();
        add(bg);
        repaint();
        setVisible(true);
        pack();
    }

    public void run(){
        Board.log("Start run\n");
        while(true){
            game.update();
        }
    }
}
