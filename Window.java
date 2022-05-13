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
                System.out.print("Btn0");
                backGround.removeAll();
                backGround.add(new Mode1v1());
                pack();
                repaint();
            }
        });
        m0.add(b0);

        JButton b1 = new JButton("Mode Debug");
        b1.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Btn0");
                backGround.removeAll();
                backGround.add(new ModeDebug());
                pack();
                repaint();
            }
        });
        m0.add(b1);

        JButton b2 = new JButton("Mode AI naïve");
        b2.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("Btn0");
                backGround.removeAll();
                backGround.add(new Mode1vRDM());
                pack();
                repaint();
            }
        });
        m0.add(b2);

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
/*
    public void initMenu(){
        menu.setSize( 128, 128);
        menu.setBackground(new Color(0,255,0));
        JButton b = new JButton("Play 1v1");
        b.setBounds(16, 16, 64, 32);

        System.out.println(b.getBounds());
        b.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.print("0\n");
                
                game = new Mode1v1();
                menu.remove(b);
                menu.add(game);
                repaint();
                
                System.out.print("1\n");
            }
        }); 
        menu.add(b);
    }*/
}
