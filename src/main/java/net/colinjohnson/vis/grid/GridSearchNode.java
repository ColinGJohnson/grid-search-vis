package net.colinjohnson.vis.grid;

import java.util.ArrayList;
import java.util.List;

public class GridSearchNode extends GridNode {
    private boolean visited;
    private GridSearchNode previous;
    private int pathLength;

    public GridSearchNode(int x, int y) {
        super(x, y);
        this.visited = false;
    }

    public List<GridSearchNode> getPath() {
        List<GridSearchNode> path = new ArrayList<>();
        GridSearchNode node = previous;
        while (node != null) {
            path.add(node);
            node = node.getPrevious();
        }
        return path;
    }

    public int getPathLength() {
        return pathLength;
    }

    public void visit() {
        this.visited = true;
        this.pathLength = getPath().size();
    }

    public void visit(GridSearchNode previous) {
        this.previous = previous;
        visit();
    }

    public boolean isUnvisited() {
        return !visited;
    }

    public GridSearchNode getPrevious() {
        return previous;
    }
}
