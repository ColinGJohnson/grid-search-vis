package net.colinjohnson.vis.search;

import net.colinjohnson.vis.grid.GridSearchNode;

import java.util.Comparator;

public class BreadthFirstComparator implements Comparator<GridSearchNode> {
    @Override
    public int compare(GridSearchNode n1, GridSearchNode n2) {
        return Integer.compare(n1.getPathLength(), n2.getPathLength());
    }
}
