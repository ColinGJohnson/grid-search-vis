package net.colinjohnson.vis.grid;

import java.util.*;

public class GridSearch {
    private final PriorityQueue<GridSearchNode> searchQueue;
    private final Grid<GridSearchNode> grid;
    private int maxQueue;

    public GridSearch(Comparator<GridSearchNode> gridSearchNodeComparator) {
        grid = new Grid<>(new GridSearchNode.DefaultSupplier(), 400, 400);
        searchQueue = new PriorityQueue<>(gridSearchNodeComparator);

        // start with the top left square
        GridSearchNode topLeft = grid.getNode(0, 0);
        topLeft.visit();
        searchQueue.add(topLeft);
    }

    public boolean done() {
        return searchQueue.isEmpty();
    }

    public void step() {
        if (searchQueue.isEmpty()) {
            throw new IllegalStateException("Can't proceed with search because queue is empty.");
        }
        GridSearchNode node = searchQueue.poll();

        if (node.isUnvisited()) {
            throw new IllegalStateException("Encountered unvisited node in search queue.");
        }

        // expand adjacent nodes which have not yet been visited
        for (GridSearchNode adjacent : getAdjacentNodes(grid, node)) {
            if (adjacent.isUnvisited()) {
                adjacent.visit(node);
                searchQueue.add(adjacent);
                maxQueue = searchQueue.size() > maxQueue ? maxQueue + 1 : maxQueue;
            }
        }
    }

    private List<GridSearchNode> getAdjacentNodes(Grid<GridSearchNode> grid, GridSearchNode node) {
        List<GridSearchNode> adjacent = new ArrayList<>();
        Set<Integer> deltas = Set.of(-1, 0, 1);

        for (int dx: deltas) {
            for (int dy: deltas) {
                // don't add the same node, or nodes on the diagonals
                if ((dx == 0 && dy == 0) || (Math.abs(dx) > 0 && Math.abs(dy) > 0)) continue;
                grid.getNodeOptional(node.getX() + dx, node.getY() + dy).ifPresent(adjacent::add);
            }
        }

        return adjacent;
    }

    public Grid<GridSearchNode> getGrid() {
        return grid;
    }

    public int getMaxQueue() {
        return maxQueue;
    }
}
