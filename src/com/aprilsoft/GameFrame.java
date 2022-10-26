package com.aprilsoft;

import javax.swing.JFrame;
import java.awt.Container;

public class GameFrame extends JFrame {

    public GameFrame() {
        MyPanel pan = new MyPanel();
        Container cont = getContentPane();
        cont.add(pan);

        setTitle("Game \"Sea Battleship\"");
        setBounds(0, 0, 900,600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }
}
