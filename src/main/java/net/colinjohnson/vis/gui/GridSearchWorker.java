package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;

import javax.swing.*;

public class GridSearchWorker<T> extends SwingWorker<Void, Void> {
    private Grid<T> grid;
    private GridSearchAlgorithm<T> algorithm;
    private GridDisplayPanel<T> displayPanel;


    public GridSearchWorker(Grid<T> grid, GridSearchAlgorithm<T> algorithm, GridDisplayPanel<T> displayPanel) {
        this.grid = grid;
        this.algorithm = algorithm;
        this.displayPanel = displayPanel;
    }

    @Override
    protected Void doInBackground() throws Exception {
        algorithm.search(grid);
        return null;
    }

    @Override
    protected void done() {
        displayPanel.repaint();
    }
}
