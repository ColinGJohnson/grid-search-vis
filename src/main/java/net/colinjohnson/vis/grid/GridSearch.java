package net.colinjohnson.vis.grid;

import java.util.*;

public class GridSearch {
    PriorityQueue<GridSearchNode> searchQueue;
    Grid<GridSearchNode> grid;

    public GridSearch(Comparator<GridSearchNode> gridSearchNodeComparator) {
        // TODO: take grid and start position as parameters
        grid = new Grid<>(new GridSearchNode.DefaultSupplier(), 100, 100);
        searchQueue = new PriorityQueue<>(gridSearchNodeComparator);
    }

    public void search() {
        while (!searchQueue.isEmpty()) {
            step();
        }
    }

    /**
     * Take one step forward in the search.
     */
    public void step() {
        if (searchQueue.isEmpty()) {
            throw new IllegalStateException("Can't proceed with search because queue is empty.");
        }
        GridSearchNode node = searchQueue.poll();
        searchQueue.addAll(expand(grid, node));
    }

    /**
     * Get search actions that can be taken from the given search node
     */
    public List<GridSearchNode> expand(Grid<GridSearchNode> grid, GridSearchNode node) {
        List<GridSearchNode> actions = new ArrayList<>();

        // expand adjacent nodes which have not yet been visited
        for (GridSearchNode adjacent : getAdjacentNodes(grid, node)) {
            if (!adjacent.isVisited()) {
                searchQueue.add(adjacent);
                grid.setNode(adjacent);
            }
        }

        return actions;
    }

    public List<GridSearchNode> getAdjacentNodes(Grid<GridSearchNode> grid, GridSearchNode node) {
        List<GridSearchNode> adjacent = new ArrayList<>();
        Set<Integer> deltas = Set.of(-1, 0, 1);

        for (int dx: deltas) {
            for (int dy: deltas) {
                if (dx == 0 && dy == 0) continue;
                grid.getNode(node.getX() + dx, node.getY() + dy).ifPresent(adjacent::add);
            }
        }

        return adjacent;
    }
}
