package net.colinjohnson.vis;

import net.colinjohnson.vis.grid.GridSearch;
import net.colinjohnson.vis.gui.GridDisplayPanel;
import net.colinjohnson.vis.search.RandomDepthFirstComparator;

import javax.swing.*;

public class Main {
    final static int REPAINT_INTERVAL = 100;

    public static void main(String[] args) {

        // create a new window and set its size
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1920, 1080);

        // create and add a panel for drawing graphics to
        GridSearch gridSearch = new GridSearch(new RandomDepthFirstComparator());
        JPanel panel = new GridDisplayPanel<>(gridSearch.getGrid(), gridSearch);
        frame.add(panel);

        // center the window on the screen
        frame.setLocationRelativeTo(null);

        // give the window a title and set it visible
        frame.setTitle("Grid Search Visualization");
        frame.setVisible(true);

        int steps = 0;
        while (!gridSearch.done()) {
            gridSearch.step();
            steps++;
            if (steps % REPAINT_INTERVAL == 0) {
                panel.repaint();
            }
        }
        panel.repaint();
    }
}
