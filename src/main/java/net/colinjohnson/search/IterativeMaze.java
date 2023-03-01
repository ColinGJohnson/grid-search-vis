package net.colinjohnson.search;

import net.colinjohnson.search.grid.GridSearchConfig;
import net.colinjohnson.search.gui.MazePanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class IterativeMaze {
    GridSearchConfig gridSearchConfig = new GridSearchConfig();

    // enum for directions
    public enum Direction {
        Up,
        Down,
        Left,
        Right,
        Disabled
    }

    // maze representation
    private int[][] maze;
    public final static int TILE_DEFAULT = -1;
    public final static int TILE_MAZE = 1;
    private int maxStack = 0;
    private int approxMaxStack = 0;
    private int numFilled = 0;
    private float totalSquares = 0;
    JFrame frame = null;
    JPanel panel = null;

    // generation
    private Point startPosition = new Point(250, 250);
    private Random random = new Random();
    private Stack<MazePosition> history = new Stack<MazePosition>();
    private Direction last = Direction.Up;

    public IterativeMaze() {

        // get and set size of maze to generate with scanner
        if (askSize) {
            System.out.print("Enter maze size: ");
            Scanner scanner = new Scanner(System.in);
            size = scanner.nextInt();
            scanner.close();
        }

        // initialize the maze array
        maze = new int[size][size];

        // create a jpanel to show progress if showGeneration is enabled
        if (showGeneration) {

            // create a new window and set its size
            frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1280, 720);

            // create and add a panel for drawing graphics to
            panel = new MazePanel(this);
            frame.add(panel);

            // center the window on the screen
            frame.setLocationRelativeTo(null);

            // give the window a title and set it visible
            frame.setTitle("Recursive Maze Generator");
            frame.setVisible(true);
        }



        // get total number of squares
        totalSquares = (int) (Math.pow(size / jumpDist, 2));

        // clear the maze
        clearMaze();

        // generate the maze
        generate();

        // print the maze if requested
        if (printMaze) displayMaze();

        // save an image if requested
        if (saveAsImage) saveMazeImage("maze" + i + ".png");


        // if the maze generation was shown, get rid of that jFrame
        if (showGeneration) {
            frame.setVisible(false);
            frame.dispose();
        }

        // end execution
        System.exit(0);
    } // IterativeMaze constructor

    /**
     * Finds an appropriate color for a given maze square according to the current display settings.
     * @param x The x-coordinate on the maze.
     * @param y The y-coordinate on the maze.
     * @return An appropriate color for the maze square.
     */
    public Color getColor(int x, int y) {
        if (!colorMaze) {
            return Color.WHITE;
        } else if (colorByProgress) {
            return Color.getHSBColor(colorShift + (colorRange * (float) (maze[x][y]) / totalSquares), saturation, brightness);
        } else {
            return Color.getHSBColor(colorShift + (colorRange * (float) (maze[x][y]) / approxMaxStack), saturation, brightness);
        }
    }


    public boolean isColorMaze() {
        return colorMaze;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public int getScale() {
        return scale;
    }

    public int getSize() {
        return size;
    }

    public int[][] getMaze() {
        return maze;
    }

    public Stack<MazePosition> getHistory() {
        return history;
    }

    public int getNumFilled() {
        return numFilled;
    }

}
