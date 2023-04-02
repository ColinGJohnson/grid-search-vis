package net.colinjohnson.vis.grid;

import java.awt.*;

public class ObstacleNode extends GridNode {
    private boolean blocked;
    public ObstacleNode(int x, int y, boolean blocked) {
        super(x, y);
    }

    public ObstacleNode(int x, int y) {
        this(x, y, false);
    }

    @Override
    public Color getColor() {
        return blocked ? Color.BLACK : Color.WHITE;
    }

    public boolean isBlocked() {
        return blocked;
    }
}
