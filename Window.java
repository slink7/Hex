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
    JPanel menu = new JPanel();

    Window() {
        setTitle("Jeu de hex");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1024,512));

        add(menu);
        
        initMenu();

        setVisible(true);
    }

    public void initMenu(){
        menu.setBounds(16, 16, 128, 128);
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
    }
}
