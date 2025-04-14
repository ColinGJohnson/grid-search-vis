package dev.cgj.searchvis.search;

import dev.cgj.searchvis.grid.GridSearchNode;

import java.util.Comparator;

public enum SearchAlgorithm {
    RANDOM_DFS("Random Depth First Search", new RandomDepthFirstComparator()),
    DFS("Depth First Search", new DepthFirstComparator()),
    BFS("Breadth First Search", new BreadthFirstComparator()),
    A_STAR("A*", new AStarComparator());

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
