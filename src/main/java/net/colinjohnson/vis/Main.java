package net.colinjohnson.vis;

import net.colinjohnson.vis.grid.GridSearch;
import net.colinjohnson.vis.gui.GridDisplay;
import net.colinjohnson.vis.search.DepthFirstComparator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        GridSearch gridSearch = new GridSearch(new DepthFirstComparator());

        // create a new window and set its size
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);

        // create and add a panel for drawing graphics to
        JPanel panel = new GridDisplay<>(gridSearch.getGrid());
        frame.add(panel);

        // center the window on the screen
        frame.setLocationRelativeTo(null);

        // give the window a title and set it visible
        frame.setTitle("Grid Search Visualization");
        frame.setVisible(true);
    }
}
