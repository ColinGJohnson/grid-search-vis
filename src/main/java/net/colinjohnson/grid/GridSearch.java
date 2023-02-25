package net.colinjohnson.grid;

public class GridSearch {
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
                IterativeMaze.Direction proceed = history.peek().select();

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
}
