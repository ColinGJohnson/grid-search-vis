package net.colinjohnson.vis;

import net.colinjohnson.vis.gui.GridVisGUI;

public class Main {
    final static int REPAINT_INTERVAL = 100;

    public static void main(String[] args) throws InterruptedException {
        GridVisGUI testform = new GridVisGUI();

//        GridSearch gridSearch = new GridSearch(new DepthFirstComparator());
//
//        // create a new window and set its size
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(1920, 1080);
//
//        // create and add a panel for drawing graphics to
//        JPanel panel = new GridDisplay<>(gridSearch.getGrid());
//        frame.add(panel);
//
//        // center the window on the screen
//        frame.setLocationRelativeTo(null);
//
//        // give the window a title and set it visible
//        frame.setTitle("Grid Search Visualization");
//        frame.setVisible(true);
//
//        int steps = 0;
//        while (!gridSearch.done()) {
//            gridSearch.step();
//            steps++;
//            if (steps % REPAINT_INTERVAL == 0) {
//                panel.repaint();
//            }
//        }
//        panel.repaint();
    }
}
