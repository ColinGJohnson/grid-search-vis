package net.colinjohnson.vis.grid;

public class GridSearchConfig {
    private boolean colorByProgress = false;


    private boolean showGeneration = true;
    private boolean showGrid = false;
    private int delay = 1;
    private int speed = 20; // must be >= 1, limits painted frames to increase drawing speed
    private int scale = 1;
    private int jumpDist = 1; // must be >= 1, controls the distance between maze halls
    private int size = 1001; // must be >= 2, controls the height & width of the maze

    private double lastDirectionWeighting = 1; // must be at least 1
    private double randomnessWeighting = 1;
    private double directionWeightingAmount = -1; // must be at least 1
}
