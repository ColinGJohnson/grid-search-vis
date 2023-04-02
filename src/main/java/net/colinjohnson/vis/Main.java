package net.colinjohnson.vis;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridSearch;
import net.colinjohnson.vis.grid.ObstacleNode;
import net.colinjohnson.vis.gui.GridDisplayPanel;
import net.colinjohnson.vis.search.RandomDepthFirstComparator;

import javax.swing.*;

public class Main {
    final static int REPAINT_INTERVAL = 100;

    public static void main(String[] args) {

        // create a new window and set its size
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);

        // create and add a panel for drawing graphics to
        Grid<ObstacleNode> obstacleNodeGrid = new Grid<>(ObstacleNode::new, 100, 100);
        GridSearch gridSearch = new GridSearch(new RandomDepthFirstComparator(), obstacleNodeGrid);
        JPanel panel = new GridDisplayPanel<>(gridSearch.getSearchGrid());
        frame.add(panel);

        // center the window on the screen
        frame.setLocationRelativeTo(null);

        // give the window a title and set it visible
        frame.setTitle("Grid Search Visualization");
        frame.setVisible(true);

        int steps = 0;
        while (gridSearch.hasNextStep()) {
            gridSearch.step();
            steps++;
            if (steps % REPAINT_INTERVAL == 0) {
                panel.repaint();
            }
        }
        panel.repaint();
    }
}
