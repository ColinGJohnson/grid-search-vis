package dev.cgj.searchvis.gui;

import dev.cgj.searchvis.grid.Grid;
import dev.cgj.searchvis.grid.GridNode;

import java.awt.Color;

interface GridColoringStrategy<N extends GridNode> {
    Color getColor(Grid<N> grid, int x, int y);
}
