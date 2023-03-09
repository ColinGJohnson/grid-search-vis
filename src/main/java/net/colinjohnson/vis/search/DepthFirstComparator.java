package net.colinjohnson.vis.search;

import net.colinjohnson.vis.grid.GridSearchNode;

import java.util.Comparator;

public class DepthFirstComparator implements Comparator<GridSearchNode> {
    @Override
    public int compare(GridSearchNode n1, GridSearchNode n2) {
        return n1.getPathLength().compareTo(n2.getPathLength());
    }
}
