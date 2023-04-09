package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * A JPanel that displays a grid.
 *
 * @param <N> The type of node in the grid.
 */
public class GridDisplayPanel<N extends GridNode, C extends GridColoringStrategy<N>> extends JPanel {
    private static final Logger log = LoggerFactory.getLogger(GridDisplayGUI.class);
    private Grid<N> grid;
    private BufferedImage gridImage;
    private C coloringStrategy;
    private boolean showGridLines = false;
    private int scale = 2; // Number of pixels per grid square
    private Point selectedLocation = new Point();

    public GridDisplayPanel(Grid<N> grid, C coloringStrategy) {
        super();
        this.grid = grid;
        this.coloringStrategy = coloringStrategy;

        // Set up mouse listener to select locations on the grid
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                Point topLeft = getTopLeftCorner(getSize(), grid.getSize(), scale);
                Point bottomRight = getBottomRightCorner(getSize(), grid.getSize(), scale);
                if (evt.getX() >= topLeft.x && evt.getX() <= bottomRight.x
                        && evt.getY() >= topLeft.y && evt.getY() <= bottomRight.y) {
                    int gridX = (evt.getX() - topLeft.x) / scale;
                    int gridY = (evt.getY() - topLeft.y) / scale;
                    selectedLocation = new Point(gridX, gridY);
                    repaint();
                    log.info("Selected location: {}", selectedLocation);
                }
            }
        });
    }

    /**
     * Draw the grid. Could be optimized by caching the image.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        // Calculate the boundaries of the grid
        Point topLeft = getTopLeftCorner(this.getSize(), grid.getSize(), scale);
        Point bottomRight = getBottomRightCorner(this.getSize(), grid.getSize(), scale);

        // Draw the grid
        updateGridImage();
        g.drawImage(gridImage, topLeft.x, topLeft.y, bottomRight.x, bottomRight.y, 0, 0, grid.getWidth(), grid.getHeight(), null);

        // Draw the selected location
        drawSelectedLocation(g);

        // Grid lines form a solid block if scale is 1, so hide them in that case
        if (showGridLines && scale > 1) {
            drawGridLines(g, topLeft, bottomRight);
        }
    }

    private void drawSelectedLocation(Graphics g) {
        final int MARKER_SIZE = 5;
        g.setColor(Color.WHITE);
        g.fillRect(
            selectedLocation.x * scale - MARKER_SIZE / 2,
            selectedLocation.y * scale - MARKER_SIZE / 2,
            MARKER_SIZE, MARKER_SIZE
        );
    }

    private void updateGridImage() {
        if (gridImage == null || gridImage.getWidth() != grid.getWidth() || gridImage.getHeight() != grid.getHeight()) {
            gridImage = new BufferedImage(grid.getWidth(), grid.getHeight(), BufferedImage.TYPE_INT_RGB);
        }
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                gridImage.setRGB(x, y, coloringStrategy.getColor(grid, x, y).getRGB());
            }
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

    public static Point getTopLeftCorner(Dimension panelSize, Dimension gridSize, int scale) {
        return new Point(
            getBoundaryLocation(panelSize.getWidth(), gridSize.getWidth(), scale),
            getBoundaryLocation(panelSize.getHeight(), gridSize.getHeight(), scale)
        );
    }

    public static Point getBottomRightCorner(Dimension panelSize, Dimension gridSize, int scale) {
        return new Point(
            getBoundaryLocation(panelSize.getWidth(), gridSize.getWidth(), scale) + gridSize.width * scale,
            getBoundaryLocation(panelSize.getHeight(), gridSize.getHeight(), scale) + gridSize.height * scale
        );
    }

    /**
     * Draws grid lines on top of the grid.
     *
     * @param g The graphics object to draw with.
     * @param bottomRight The location of the bottom-right corner of the grid.
     * @param topLeft The location of the top-left corner of the grid.
     */
    private void drawGridLines(Graphics g, Point topLeft, Point bottomRight) {
        g.setColor(Color.DARK_GRAY);
        for (int y = topLeft.y; y <= bottomRight.y; y += scale) {
            g.drawLine(bottomRight.x, y, topLeft.x, y);
        }
        for (int x = topLeft.x; x <= bottomRight.x; x += scale) {
            g.drawLine(x, bottomRight.y, x, topLeft.y);
        }
    }

    /**
     * Saves the current grid as an image.
     *
     * @param outputFile The file to save the image to.
     */
    public void saveAsImage(File outputFile) {
        updateGridImage();
        try {
            ImageIO.write(gridImage, "png", outputFile);
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

    public Optional<Point> getSelectedPoint() {
        if (selectedLocation.getX() > grid.getWidth() || selectedLocation.getY() > grid.getHeight()) {
            return Optional.empty();
        }
        return Optional.ofNullable(selectedLocation);
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
