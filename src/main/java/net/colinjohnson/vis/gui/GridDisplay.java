package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.Grid;
import net.colinjohnson.vis.grid.GridNode;

import javax.swing.*;
import java.awt.*;

public class GridDisplay<T extends GridNode> extends JPanel {
    Grid<T> grid;
    boolean showGridLines = true;
    double scale = 10;

    public GridDisplay(Grid<T> grid) {
        super();
        this.grid = grid;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // flat background
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int YMax = (int) (this.getHeight() / 2.0 + grid.getHeight() / 2.0 * scale);
        int YMin = (int) (this.getHeight() / 2.0 - grid.getHeight() / 2.0 * scale);
        int XMax = (int) (this.getWidth() / 2.0 + grid.getWidth() / 2.0 * scale);
        int XMin = (int) (this.getWidth() / 2.0 - grid.getWidth() / 2.0 * scale);

        // fill grid squares
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                g.setColor(grid.getNode(x, y).map(GridNode::getColor).orElse(Color.RED));
                g.fillRect((int) (XMin + x * scale), (int) (YMin + y * scale), (int) scale, (int) scale);
            }
        }

        if (showGridLines) {
            g.setColor(Color.DARK_GRAY);

            // horizontal lines
            for (int y = YMin; y <= YMax; y += scale) {
                g.drawLine(XMax, y, XMin, y);
            }

            // vertical lines
            for (int x = XMin; x <= XMax; x += scale) {
                g.drawLine(x, YMax, x, YMin);
            }
        }
    }
}
