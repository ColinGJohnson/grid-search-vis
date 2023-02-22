package net.colinjohnson.gridsearch;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author colin johnson
 * created on 2018/03/04
 */
public class IterativeMaze {

    // settings
    private boolean printMaze = false;
    private boolean askSize = false;
    private boolean saveAsImage = true;
    private int iterations = 10;

    private boolean colorMaze = true;
    private boolean colorByProgress = false;
    private float colorRange = 0.55f;
    private float colorShift = 0.5f;
    private float saturation = 0.7f;
    private float brightness = 0.9f;

    private boolean showGeneration = true;
    private boolean showGrid = false;
    private int delay = 1;
    private int speed = 20; // must be >= 1, limits painted frames to increase drawing speed
    private int scale = 1;
    private int jumpDist = 1; // must be >= 1, controls the distance between maze halls
    private int size = 1001; // must be >= 2, controls the height & width of the maze

    private double lastDirectionWeighting = 1; // must be at least 1
    private double randomnessWeighting = 1;
    private Direction directionalWeighting = Direction.Up;
    private double directionWeightingAmount = -1; // must be at least 1

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

        // create the requested number of mazes
        for (int i = iterations; i > 0; i--) {

            // estimate max stack size for real-time coloration
            approxMaxStack = (int) (Math.pow(size / jumpDist, 2) / 4);

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
        }

        // if the maze generation was shown, get rid of that jFrame
        if (showGeneration) {
            frame.setVisible(false);
            frame.dispose();
        }

        // end execution
        System.exit(0);
    } // IterativeMaze constructor

    // fills a given integer array with a maze
    public void generate() {

        // add a starting point in the history stack
        history.push(new MazePosition(startPosition.x, startPosition.y, null));
        maze[startPosition.x][startPosition.y] = TILE_MAZE;

        // loop through maze generation until there are no more possible positions to move to
        while (!history.empty()) {

            // if the maze generation is being shown in real-time, update the panel
            // displaying it every couple iterations
            if (showGeneration && history.size() % speed == 0) {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                panel.repaint();
            }

            // update locations to proceed to
            history.peek().checkDirections();

            // abandon position if there is nowhere to proceed to
            if (history.peek().stuck()) {
                history.pop();

                // otherwise proceed to one of the available locations
            } else {

                // choose a location
                Direction proceed = history.peek().select();

                // "move to" found location
                int dx, dy;
                switch (proceed) {
                    case Up -> {
                        dx = 0;
                        dy = -jumpDist;
                    }
                    case Down -> {
                        dx = 0;
                        dy = jumpDist;
                    }
                    case Left -> {
                        dx = -jumpDist;
                        dy = 0;
                    }
                    case Right -> {
                        dx = jumpDist;
                        dy = 0;
                    }
                    default -> {
                        dx = 0;
                        dy = 0;
                    }
                }

                // record position in history stack
                history.add(new MazePosition(history.peek().x + dx, history.peek().y + dy, history.peek()));

                // record max stack sizes
                if (colorMaze) {
                    if (history.size() > maxStack) maxStack = history.size();
                }

                // set fill color to default
                int fillNum = TILE_MAZE;

                // if the maze should be colored, use special values instead
                if (colorMaze) {
                    if (colorByProgress) {
                        numFilled++;
                        fillNum = numFilled;
                    } else {
                        fillNum = history.size();
                    }
                }

                if (dx == 0) {
                    for (int i = 1; i <= Math.abs(dy); i++) {
                        maze[history.peek().x][history.peek().y - (int) (i * Math.signum(dy))] = fillNum;
                    }
                } else {
                    for (int i = 1; i <= Math.abs(dx); i++) {
                        maze[history.peek().x - (int) (i * Math.signum(dx))][history.peek().y] = fillNum;
                    }
                }
                maze[history.peek().x][history.peek().y] = fillNum;
            }
        }
        System.out.println("Maze array complete.");
    } // generate

    // fills the maze array with the default tile
    public void clearMaze() {

        // reset filled square total
        numFilled = 0;

        // fill maze with wall tiles
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                maze[i][j] = TILE_DEFAULT;
            }
        }
    } // clearMaze

    // prints a representation of the maze array
    public void displayMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.print("\n");
        }
    } // displayMaze

    /**
     * Finds an appropriate color for a given maze square according to the current display settings.
     * @param x The x-coordinate on the maze.
     * @param y The y-coordinate on the maze.
     * @return An appropriate color for the maze square.
     */
    Color getColor(int x, int y) {
        if (!colorMaze) {
            return Color.WHITE;
        } else if (colorByProgress) {
            return Color.getHSBColor(colorShift + (colorRange * (float) (maze[x][y]) / totalSquares), saturation, brightness);
        } else {
            return Color.getHSBColor(colorShift + (colorRange * (float) (maze[x][y]) / approxMaxStack), saturation, brightness);
        }
    }

    /**
     * Saves a .png image representation of the maze array.
     * @param title The file name for the generated image.
     */
    public void saveMazeImage(String title) {

        BufferedImage mazeImage = new BufferedImage(maze.length, maze[0].length, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[0].length; x++) {
                if (maze[x][y] != TILE_DEFAULT) {
                    mazeImage.setRGB(x, y, getColor(x, y).getRGB());
                } else {
                    mazeImage.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        File output = new File(title);
        try {
            ImageIO.write(mazeImage, "png", output);
            System.out.println("Maze saved as \"" + title + "\"");
        } catch (IOException e) {
            System.out.println("[!] - Failed to write maze image.");
        }
    }

    // object to store information about generation progress
    class MazePosition {
        int x = 0;
        int y = 0;
        ArrayList<Direction> directions = new ArrayList<Direction>();
        MazePosition formerPosition = null;

        MazePosition(int x, int y, MazePosition formerPosition) {
            this.x = x;
            this.y = y;
            this.formerPosition = formerPosition;
        } // MazePosition constructor

        private void allow(Direction direction) {

            // add direction to allowed directions if doesn't exist
            if (!directions.contains(direction)) {
                directions.add(direction);
            }
        }

        private void disallow(Direction direction) {

            // remove direction from allowed directions
            directions.remove(direction);
        }

        public void checkDirections() {

            // check up
            if (y - jumpDist >= 0 && maze[x][y - jumpDist] == TILE_DEFAULT) {
                allow(Direction.Up);
            } else {
                disallow(Direction.Up);
            }

            // check down
            if (y + jumpDist < maze[x].length && maze[x][y + jumpDist] == TILE_DEFAULT) {
                allow(Direction.Down);
            } else {
                disallow(Direction.Down);
            }

            // check left
            if (x - jumpDist >= 0 && maze[x - jumpDist][y] == TILE_DEFAULT) {
                allow(Direction.Left);
            } else {
                disallow(Direction.Left);
            }

            // check right
            if (x + jumpDist < maze.length && maze[x + jumpDist][y] == TILE_DEFAULT) {
                allow(Direction.Right);
            } else {
                disallow(Direction.Right);
            }
        } // checkDirection

        public Direction select() {

            double totalWeights = 0;
            RandomCollection<Direction> selectMap = new RandomCollection<>();

            for (Direction direction: directions) {
                if (direction == last) {
                    selectMap.add(lastDirectionWeighting, direction);
                } else {
                    selectMap.add(randomnessWeighting, direction);
                }

                if (direction == directionalWeighting){
                    selectMap.add(directionWeightingAmount, direction);
                }
            }

            last = selectMap.next();
            return last;
        }

        public boolean stuck() {
            return directions.isEmpty();
        } // stuck
    } // MazePosition

    public boolean isPrintMaze() {
        return printMaze;
    }

    public void setPrintMaze(boolean printMaze) {
        this.printMaze = printMaze;
    }

    public boolean isSaveAsImage() {
        return saveAsImage;
    }

    public void setSaveAsImage(boolean saveAsImage) {
        this.saveAsImage = saveAsImage;
    }

    public boolean isColorMaze() {
        return colorMaze;
    }

    public void setColorMaze(boolean colorMaze) {
        this.colorMaze = colorMaze;
    }

    public boolean isColorByProgress() {
        return colorByProgress;
    }

    public void setColorByProgress(boolean colorByProgress) {
        this.colorByProgress = colorByProgress;
    }

    public boolean isAskSize() {
        return askSize;
    }

    public void setAskSize(boolean askSize) {
        this.askSize = askSize;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public boolean isShowGeneration() {
        return showGeneration;
    }

    public void setShowGeneration(boolean showGeneration) {
        this.showGeneration = showGeneration;
    }

    public boolean isShowGrid() {
        return showGrid;
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getJumpDist() {
        return jumpDist;
    }

    public void setJumpDist(int jumpDist) {
        this.jumpDist = jumpDist;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getColorRange() {
        return colorRange;
    }

    public void setColorRange(float colorRange) {
        this.colorRange = colorRange;
    }

    public float getColorShift() {
        return colorShift;
    }

    public void setColorShift(float colorShift) {
        this.colorShift = colorShift;
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public float getBrightness() {
        return brightness;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }

    public double getLastDirectionWeighting() {
        return lastDirectionWeighting;
    }

    public int[][] getMaze() {
        return maze;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public Stack<MazePosition> getHistory() {
        return history;
    }

    public void setHistory(Stack<MazePosition> history) {
        this.history = history;
    }

    public int getNumFilled() {
        return numFilled;
    }

    public void setNumFilled(int numFilled) {
        this.numFilled = numFilled;
    }
} // Main