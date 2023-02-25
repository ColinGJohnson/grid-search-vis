package net.colinjohnson.grid;

import java.util.Optional;

public class Grid {
    private final Optional<GridNode>[][] grid;


    public Grid(int width, int height) {
        if (width < 1 || height < 1) {
            throw new IllegalArgumentException("Grid width and height must be > 1");
        }
        grid = new Optional[width][height];
        clearGrid();
    }

    // fills the maze array with the default tile
    public void clearGrid() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                grid[x][y] = Optional.empty();
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
