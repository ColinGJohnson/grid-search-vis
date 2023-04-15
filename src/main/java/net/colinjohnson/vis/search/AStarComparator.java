package net.colinjohnson.vis.search;

import net.colinjohnson.vis.grid.GridSearchNode;

import java.util.Comparator;

public class AStarComparator implements Comparator<GridSearchNode> {
    @Override
    public int compare(GridSearchNode n1, GridSearchNode n2) {
        if (n1 == n2) return 0;
        return Integer.compare(getCost(n1), getCost(n2));
    }

    private int getCost(GridSearchNode node) {
        return node.getPathLength() + node.getHeuristicCost();
    }
}
