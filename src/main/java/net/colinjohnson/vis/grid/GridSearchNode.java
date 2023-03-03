package net.colinjohnson.vis.grid;

import java.util.function.Supplier;

public class GridSearchNode implements GridNode {
    private GridSearchNode previous;

    public GridSearchNode(GridSearchNode previous) {
        this.previous = previous;
    }

    public long getPathLength() {
        long pathLength = 1;

    }

    public class SupplyDefault implements Supplier<GridNode> {

        @Override
        public GridNode get() {
            return new gridNode
        }
    }
}
