package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridNode;

import java.awt.*;

interface GridColoringStrategy<T extends GridNode> {
    Color getColor(Grid<T> grid, int x, int y);
}
