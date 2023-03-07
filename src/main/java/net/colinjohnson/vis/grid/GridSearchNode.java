package net.colinjohnson.vis.grid;

import java.util.function.Supplier;

public class GridSearchNode implements GridNode {
    private boolean visited;
    private final GridSearchNode previous;

    private final int x;
    private final int y;

    public GridSearchNode() {
        this(null);
    }

    public GridSearchNode(GridSearchNode previous) {
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

    public static class SupplyDefault implements Supplier<GridNode> {

        @Override
        public GridNode get() {
            return new GridSearchNode()
        }
    }
}
