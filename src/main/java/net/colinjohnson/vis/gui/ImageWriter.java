package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    /**
     * Saves a .png image representation of the maze array.
     * @param title The file name for the generated image.
     */
    public static void saveGridImage(Grid grid) {

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

    // prints a representation of the maze array
    public void displayMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                System.out.print(maze[i][j] + " ");
            }
            System.out.print("\n");
        }
    } // displayMaze
}
