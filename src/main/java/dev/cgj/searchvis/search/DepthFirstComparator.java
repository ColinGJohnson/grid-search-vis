package dev.cgj.searchvis.search;

import dev.cgj.searchvis.grid.GridSearchNode;

import java.util.Comparator;

/**
 * Nodes with a larger path length are considered less than nodes with a smaller path length. This means deeper nodes
 * appear first in the search queue, and are visited first.
 */
public class DepthFirstComparator implements Comparator<GridSearchNode> {
    @Override
    public int compare(GridSearchNode n1, GridSearchNode n2) {
        if (n1 == n2) return 0;
        return -Integer.compare(n1.getPathLength(), n2.getPathLength());
    }
}
