package net.colinjohnson.vis.grid;

public abstract class GridNode {
    private final int x;
    private final int y;

    GridNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
