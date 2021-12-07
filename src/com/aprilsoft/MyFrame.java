package com.aprilsoft;

import javax.swing.*;
import java.awt.*;

public class MyFrame extends JFrame {

    public MyFrame() {
        MyPanel pan = new MyPanel();
        Container cont = getContentPane();
        cont.add(pan);

        setTitle("Игра \"Морской бой\"");
        setBounds(0, 0, 900,600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setResizable(false);

        setVisible(true);
    }
}
