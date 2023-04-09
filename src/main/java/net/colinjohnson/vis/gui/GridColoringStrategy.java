package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridNode;

import java.awt.*;

interface GridColoringStrategy<N extends GridNode> {
    Color getColor(Grid<N> grid, int x, int y);
}
