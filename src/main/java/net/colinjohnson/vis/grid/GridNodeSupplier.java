package net.colinjohnson.vis.grid;

public interface GridNodeSupplier<T extends GridNode> {
    T get(int x, int y);
}
