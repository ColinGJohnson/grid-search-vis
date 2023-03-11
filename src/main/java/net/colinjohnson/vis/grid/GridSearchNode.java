package net.colinjohnson.vis.grid;

import java.awt.*;

public class GridSearchNode extends GridNode {
    private boolean visited;
    private final GridSearchNode previous;

    public GridSearchNode(int x, int y) {
        this(x, y, null);
    }

    public GridSearchNode(int x, int y, GridSearchNode previous) {
        super(x, y);
        this.previous = previous;
        this.visited = false;
    }

    // TODO: Cache path length if this impacts performance
    public Long getPathLength() {
        GridSearchNode node = previous;
        long length = 0;
        while (node != null) {
            node = node.getPrevious();
        }
        return length;
    }

    public void visit(GridSearchNode previous) {
        this.visited = true;

    }

    public boolean isVisited() {
        return visited;
    }

    public GridSearchNode getPrevious() {
        return previous;
    }

    @Override
    public Color getColor() {
        return visited ? Color.white : Color.black;
    }

    public static class DefaultSupplier implements GridNodeSupplier<GridSearchNode> {

        @Override
        public GridSearchNode get(int x, int y) {
            return new GridSearchNode(x, y);
        }
    }
}
