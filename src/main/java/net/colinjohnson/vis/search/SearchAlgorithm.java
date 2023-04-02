package net.colinjohnson.vis.search;

import net.colinjohnson.vis.grid.GridSearchNode;

import java.util.Comparator;

public enum SearchAlgorithm {
    RANDOM_DFS("Random Depth First Search", new RandomDepthFirstComparator()),
    DFS("Depth First Search", new DepthFirstComparator()),
    BFS("Breadth First Search", new BreadthFirstComparator());

    private final String name;
    private final Comparator<GridSearchNode> comparator;

    SearchAlgorithm(String name, Comparator<GridSearchNode> comparator) {
        this.name = name;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        return name;
    }

    public Comparator<GridSearchNode> getComparator() {
        return comparator;
    }
}
