package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridSearch;
import net.colinjohnson.vis.grid.GridSearchNode;

import javax.swing.*;
import java.util.List;

public class GridSearchWorker extends SwingWorker<Grid<GridSearchNode>, Grid<GridSearchNode>> {
    private final GridDisplayPanel<GridSearchNode> displayPanel;
    private final GridSearch gridSearch;

    public GridSearchWorker(GridDisplayPanel<GridSearchNode> displayPanel, GridSearch gridSearch) {
        this.displayPanel = displayPanel;
        this.gridSearch = gridSearch;
    }

    @Override
    protected Grid<GridSearchNode> doInBackground() {
        final int PUBLISH_INTERVAL = 100;
        int steps = 0;
        while (gridSearch.hasNextStep()) {
            gridSearch.step();
            if (steps++ % PUBLISH_INTERVAL == 0) {
                publish(gridSearch.getSearchGrid());
            }
        }
        return gridSearch.getSearchGrid();
    }

    @Override
    protected void process(List<Grid<GridSearchNode>> results) {
        Grid<GridSearchNode> latestResult = results.get(results.size() - 1);
        displayPanel.setGrid(latestResult);
        displayPanel.repaint();
    }

    @Override
    protected void done() {
        displayPanel.repaint();
    }
}
