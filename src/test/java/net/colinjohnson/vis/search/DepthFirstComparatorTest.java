package net.colinjohnson.vis.search;

import net.colinjohnson.vis.grid.GridSearchNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class DepthFirstComparatorTest {
    DepthFirstComparator comparator;

    @BeforeEach
    void setUp() {
        comparator = new DepthFirstComparator();
    }

    /**
     * When n1 has a path length of 0 and n2 has a path length of 1, n1 > n2 because n1 it has a shorter path length.
     */
    @Test
    void compare_lessThan() {
        GridSearchNode n1 = new GridSearchNode(0, 0);
        GridSearchNode n2 = new GridSearchNode(0, 1);
        n1.visit();
        n2.visit(n1);
        assertTrue(comparator.compare(n1, n2) > 0);
    }

    /**
     * When n1 has a path length of 0 and n2 has a path length of 1, n2 < n1 because n2 has a longer path length.
     */
    @Test
    void compare_greaterThan() {
        GridSearchNode n1 = new GridSearchNode(0, 0);
        GridSearchNode n2 = new GridSearchNode(0, 1);
        n1.visit();
        n2.visit(n1);
        assertTrue(comparator.compare(n2, n1) < 0);
    }

    /**
     * The comparator should return zero when the nodes have the same path length.
     */
    @Test
    void compare_equal() {
        GridSearchNode n1 = new GridSearchNode(0, 0);
        GridSearchNode n2 = new GridSearchNode(0, 1);
        n1.visit();
        n2.visit();
        assertEquals(0, comparator.compare(n1, n2));
    }

    /**
     * The comparator should return zero when a node is compared to itself.
     */
    @Test
    void compare_same() {
        GridSearchNode n1 = new GridSearchNode(0, 0);
        n1.visit();
        //noinspection EqualsWithItself
        assertEquals(0, comparator.compare(n1, n1));
    }
}