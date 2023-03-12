package net.colinjohnson.vis.grid;

import java.awt.*;
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

    public GridSearchNode(int x, int y, GridSearchNode previous) {
        this(x, y);
        this.previous = previous;
        this.pathLength = previous == null ? 0 : previous.pathLength + 1;
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

    @Override
    public Color getColor() {
        //return visited ? Color.white : Color.black;

        float colorRange = 1f;
        float colorShift = 0f;
        float saturation = 0.7f;
        float brightness = 0.9f;

        if (isUnvisited()) {
            return Color.black;
        }

        return Color.getHSBColor(colorShift + (colorRange *  ((float)getPathLength() / 1000)), saturation, brightness);
    }

    public static class DefaultSupplier implements GridNodeSupplier<GridSearchNode> {

        @Override
        public GridSearchNode get(int x, int y) {
            return new GridSearchNode(x, y);
        }
    }
}
