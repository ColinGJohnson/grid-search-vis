package dev.cgj.searchvis.gui;

import dev.cgj.searchvis.grid.Grid;
import dev.cgj.searchvis.grid.GridNode;

import java.awt.Color;

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
