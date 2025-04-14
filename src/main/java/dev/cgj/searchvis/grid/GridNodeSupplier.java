package dev.cgj.searchvis.grid;

public interface GridNodeSupplier<T extends GridNode> {
    T get(int x, int y);
}
