package net.colinjohnson.search.grid;

import java.util.function.Supplier;

public class Grid<T extends GridNode> {
    private final GridNode[][] grid;
    private final Supplier<T> defaultNodeSupplier;

    public Grid(Supplier<T> defaultNodeSupplier, int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Grid width and height must be > 1");
        }
        this.defaultNodeSupplier = defaultNodeSupplier;
        grid = new GridNode[width][height];
        clearGrid();
    }

    /**
     * Fill the grid with default nodes, as generated by the {@code defaultNodeSupplier}.
     */
    public void clearGrid() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                grid[x][y] = defaultNodeSupplier.get();
            }
        }
    }

    public int getWidth() {
        return grid.length;
    }

    public int getHeight() {
        return grid[0].length;
    }
}
