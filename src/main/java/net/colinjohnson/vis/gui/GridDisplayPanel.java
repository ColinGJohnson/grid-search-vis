package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A JPanel that displays a grid.
 *
 * @param <N> The type of node in the grid.
 * @param <C> The type of coloring strategy used to color the grid.
 */
public class GridDisplayPanel<N extends GridNode, C extends GridColoringStrategy<N>> extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(GridDisplayGUI.class);
    private Grid<N> grid;
    private C coloringStrategy;
    private boolean showGridLines = false;
    private int scale = 2;

    public GridDisplayPanel(Grid<N> grid, C coloringStrategy) {
        super();
        this.grid = grid;
        this.coloringStrategy = coloringStrategy;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Calculate the boundaries of the grid
        int XMin = getBoundaryLocation(this.getWidth(), grid.getWidth(), scale);
        int YMin = getBoundaryLocation(this.getHeight(), grid.getHeight(), scale);
        int XMax = XMin + grid.getWidth() * scale;
        int YMax = YMin + grid.getHeight() * scale;

        // Draw grid squares
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                g.setColor(coloringStrategy.getColor(grid, x, y));
                g.fillRect(XMin + x * scale, YMin + y * scale, scale, scale);
            }
        }

        // Grid lines form a solid block if scale is 1, so hide them in that case
        if (showGridLines && scale > 1) {
            drawGridLines(g, YMax, YMin, XMax, XMin);
        }
    }

    /**
     * Calculates the location of the top-left corner of the grid in the x or y direction.
     * The grid is centered in the panel.
     *
     * @param panelSize The size of the panel in pixels.
     * @param gridSize The size of the grid in grid squares.
     * @param scale The number of pixels per grid square.
     * @return The location of the top-left corner of the grid, in pixels.
     */
    public static int getBoundaryLocation(double panelSize, double gridSize, double scale) {
        return (int) ((panelSize - gridSize * scale) / 2.0);
    }

    /**
     * Draws the grid lines.
     *
     * @param g The graphics object to draw with.
     * @param YMax The maximum y value of the grid.
     * @param YMin The minimum y value of the grid.
     * @param XMax The maximum x value of the grid.
     * @param XMin The minimum x value of the grid.
     */
    private void drawGridLines(Graphics g, int YMax, int YMin, int XMax, int XMin) {
        g.setColor(Color.DARK_GRAY);
        for (int y = YMin; y <= YMax; y += scale) {
            g.drawLine(XMax, y, XMin, y);
        }
        for (int x = XMin; x <= XMax; x += scale) {
            g.drawLine(x, YMax, x, YMin);
        }
    }

    /**
     * Saves the current grid as an image.
     *
     * @param outputFile The file to save the image to.
     */
    public void saveAsImage(File outputFile) {
        BufferedImage image = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                image.setRGB(x, y, coloringStrategy.getColor(grid, x, y).getRGB());
            }
        }

        try {
            ImageIO.write(image, "png", outputFile);
            log.info("Saving image as {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to save image as {}", outputFile.getAbsolutePath(), e);
        }
    }

    /**
     * Zooms in or out by the given amount.
     *
     * @param amount The amount to zoom by. Positive values zoom in, negative values zoom out.
     */
    public void zoom(int amount) {
        scale += amount;
        if (scale < 1) {
            scale = 1;
        }
    }

    public Grid<N> getGrid() {
        return grid;
    }

    public void setGrid(Grid<N> grid) {
        this.grid = grid;
    }

    public void setShowGridLines(boolean showGridLines) {
        this.showGridLines = showGridLines;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public C getColoringStrategy() {
        return coloringStrategy;
    }

    public void setColoringStrategy(C coloringStrategy) {
        this.coloringStrategy = coloringStrategy;
    }
}
