package dev.cgj.searchvis.grid;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class GridSearch {
    private final PriorityQueue<GridSearchNode> searchQueue;
    private final Grid<GridSearchNode> searchGrid;
    private final Grid<ObstacleNode> obstacleGrid;
    private int maxQueue;

    public GridSearch(Comparator<GridSearchNode> gridSearchNodeComparator, Grid<ObstacleNode> obstacleGrid, Point searchOrigin) {
        this.obstacleGrid = obstacleGrid;
        searchGrid = new Grid<>(GridSearchNode::new, obstacleGrid.getWidth(), obstacleGrid.getHeight());
        searchQueue = new PriorityQueue<>(gridSearchNodeComparator);

        // start with the top left square
        GridSearchNode topLeft = searchGrid.getNode(searchOrigin);
        topLeft.visit();
        searchQueue.add(topLeft);
    }

    public boolean hasNextStep() {
        return !searchQueue.isEmpty();
    }

    public void step() {
        if (searchQueue.isEmpty()) {
            throw new IllegalStateException("Can't proceed with search because queue is empty.");
        }
        GridSearchNode node = searchQueue.poll();

        if (node.isUnvisited()) {
            throw new IllegalStateException("Encountered unvisited node in search queue.");
        }

        // Expand adjacent nodes which have not yet been visited and are not blocked
        for (GridSearchNode adjacent : getAdjacentNodes(searchGrid, node)) {
            if (adjacent.isUnvisited() && !obstacleGrid.getNode(adjacent.getX(), adjacent.getY()).isBlocked()) {
                adjacent.visit(node);
                searchQueue.add(adjacent);
                maxQueue = searchQueue.size() > maxQueue ? maxQueue + 1 : maxQueue;
            }
        }
    }

    /**
     * Returns a list of adjacent nodes to the given node. Adjacent nodes are nodes that are directly above, below,
     * left, or right of the given node.
     *
     * @param grid The grid to search.
     * @param node The grid node to search from.
     * @return A list of adjacent nodes.
     */
    private List<GridSearchNode> getAdjacentNodes(Grid<GridSearchNode> grid, GridSearchNode node) {
        List<GridSearchNode> adjacent = new ArrayList<>();
        Set<Integer> deltas = Set.of(-1, 0, 1);
        for (int dx: deltas) {
            for (int dy: deltas) {
                if ((dx == 0 && dy == 0) || (Math.abs(dx) > 0 && Math.abs(dy) > 0)) continue;
                grid.getNodeOptional(node.getX() + dx, node.getY() + dy).ifPresent(adjacent::add);
            }
        }
        return adjacent;
    }

    public Grid<GridSearchNode> getSearchGrid() {
        return searchGrid;
    }

    public int getMaxQueue() {
        return maxQueue;
    }
}
