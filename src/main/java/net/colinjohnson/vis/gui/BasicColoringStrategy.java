package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridNode;
import net.colinjohnson.vis.grid.GridSearchNode;

import java.awt.*;

public class BasicColoringStrategy<N extends GridNode> implements GridColoringStrategy<N> {
    private final Color color;

    public BasicColoringStrategy() {
        this(Color.BLACK);
    }

    public BasicColoringStrategy(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(Grid<N> grid, int x, int y) {
        return color;
    }
}
