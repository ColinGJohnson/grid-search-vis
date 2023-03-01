package net.colinjohnson.search;

import java.util.ArrayList;

// object to store information about generation progress
public class MazePosition {
    int x = 0;
    int y = 0;
    ArrayList<IterativeMaze.Direction> directions = new ArrayList<IterativeMaze.Direction>();
    MazePosition formerPosition = null;

    MazePosition(int x, int y, MazePosition formerPosition) {
        this.x = x;
        this.y = y;
        this.formerPosition = formerPosition;
    } // MazePosition constructor

    private void allow(IterativeMaze.Direction direction) {

        // add direction to allowed directions if doesn't exist
        if (!directions.contains(direction)) {
            directions.add(direction);
        }
    }

    private void disallow(IterativeMaze.Direction direction) {

        // remove direction from allowed directions
        directions.remove(direction);
    }

    public void checkDirections() {

        // check up
        if (y - jumpDist >= 0 && maze[x][y - jumpDist] == TILE_DEFAULT) {
            allow(IterativeMaze.Direction.Up);
        } else {
            disallow(IterativeMaze.Direction.Up);
        }

        // check down
        if (y + jumpDist < maze[x].length && maze[x][y + jumpDist] == TILE_DEFAULT) {
            allow(IterativeMaze.Direction.Down);
        } else {
            disallow(IterativeMaze.Direction.Down);
        }

        // check left
        if (x - jumpDist >= 0 && maze[x - jumpDist][y] == TILE_DEFAULT) {
            allow(IterativeMaze.Direction.Left);
        } else {
            disallow(IterativeMaze.Direction.Left);
        }

        // check right
        if (x + jumpDist < maze.length && maze[x + jumpDist][y] == TILE_DEFAULT) {
            allow(IterativeMaze.Direction.Right);
        } else {
            disallow(IterativeMaze.Direction.Right);
        }
    } // checkDirection

    public IterativeMaze.Direction select() {

        double totalWeights = 0;
        RandomCollection<IterativeMaze.Direction> selectMap = new RandomCollection<>();

        for (IterativeMaze.Direction direction: directions) {
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
