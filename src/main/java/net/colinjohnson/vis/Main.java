package net.colinjohnson.vis;

import net.colinjohnson.vis.gui.MazePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        // create a new window and set its size
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);

        // create and add a panel for drawing graphics to
        JPanel panel = new MazePanel(this);
        frame.add(panel);

        // center the window on the screen
        frame.setLocationRelativeTo(null);

        // give the window a title and set it visible
        frame.setTitle("Recursive Maze Generator");
        frame.setVisible(true);
    }
}
