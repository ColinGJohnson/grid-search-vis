package dev.cgj.searchvis.grid;

public class ObstacleNode extends GridNode {
    private boolean blocked;
    public ObstacleNode(int x, int y, boolean blocked) {
        super(x, y);
    }

    public ObstacleNode(int x, int y) {
        this(x, y, false);
    }

    public boolean isBlocked() {
        return blocked;
    }
}
