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
        float colorRange = 1f;
        float colorShift = 0f;
        float saturation = 0.7f;
        float brightness = 0.9f;

        if (isUnvisited()) {
            return Color.black;
        }

        // TODO: color by max queue size
        float hue = colorShift + (colorRange *  ((float)getPathLength() / 1000));
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public static GridNodeSupplier<GridSearchNode> getSupplier() {
        return GridSearchNode::new;
    }
}
