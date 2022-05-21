package Hex;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Window extends JFrame {
    
    Board game;
    Window w = this;
    JPanel gamePanel;

    void decoy() {}

    Window() {

        /* Initialise la fenetre et ajoute le background */
        setTitle("Jeu de hex");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1142,576));
        JPanel background = new JPanel();
        background.setPreferredSize(new Dimension(1142,576));
        background.setSize(new Dimension(1142,512));
        
        /* Créé et ajoute le menu au background */
        JPanel menu = new JPanel();
        menu.setBackground(new Color(64,64,64));
        menu.setBounds(0,0,128,576);

        /* Créé et ajoute la case pour le titre dans le menu */
        JPanel titleBackground = new JPanel();
        titleBackground.setBackground(new Color(255,255,255));
        JLabel title = new JLabel("Game of Hex");
        titleBackground.add(title);
        menu.add(titleBackground);

        /* Créé et ajoute le boutton de séléction ModeLocal */
        JButton b0 = new JButton("Mode local");
        b0.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.removeAll();
                game = new ModeLocal();
                gamePanel.add(game);
                pack();
                repaint();
            }
        });
        menu.add(b0);

        /* Créé et ajoute le boutton de séléction ModeDebug */
        JButton b1 = new JButton("Mode Debug");
        b1.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.removeAll();
                game = new ModeDebug();
                gamePanel.add(game);
                pack();
                repaint();
            }
        });
        menu.add(b1);

        /* Créé et ajoute le boutton de séléction Mode1vRDM */
        JButton b2 = new JButton("Mode AI naïve");
        b2.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.removeAll();
                game = new Mode1vRDM();
                gamePanel.add(game);
                pack();
                repaint();
            }
        });
        menu.add(b2);

        /* Créé et ajoute le boutton de séléction ModeOnline */
        JButton b3 = new JButton("Mode Online");
        b3.addActionListener(new ActionListener(){  
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.removeAll();
                game = new ModeOnline();
                gamePanel.add(game);
                pack();
                repaint();
            }
        });
        menu.add(b3);

        background.add(menu);
        gamePanel = new JPanel();
        gamePanel.setBounds(128, 0, 1024, 512);;
        gamePanel.setBackground(new Color(64,64,64));
        background.add(gamePanel);
        
        //initMenu();
        add(background);
        repaint();
        setVisible(true);
        pack();
    }

    public void run(){
        Board.log("Start run\n");
        while(true){
            System.out.print("");
            if(game != null){
                if(game.getClass() == ModeOnline.class) ((ModeOnline)game).bobo();
            }
        }
    }
}
