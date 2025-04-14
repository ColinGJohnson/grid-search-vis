package dev.cgj.searchvis.search;

import dev.cgj.searchvis.grid.GridSearchNode;

import java.util.Comparator;
import java.util.Random;

public class RandomDepthFirstComparator implements Comparator<GridSearchNode> {
    static final Random random = new Random();
    @Override
    public int compare(GridSearchNode n1, GridSearchNode n2) {
        if (n1 == n2) return 0;
        int comparison = -Integer.compare(n1.getPathLength(), n2.getPathLength());
        if (comparison != 0) return comparison;
        return random.nextBoolean() ? 1 : -1;
    }
}
