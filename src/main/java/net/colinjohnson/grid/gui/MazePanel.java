package net.colinjohnson.grid.gui;

import net.colinjohnson.grid.IterativeMaze;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    IterativeMaze generator;

    public MazePanel(IterativeMaze generator) {
        super();
        this.generator = generator;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // flat background
        g.setColor(new Color(30, 30, 30));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        int YMax = (int) (this.getHeight() / 2.0 + generator.getSize() / 2.0 * generator.getScale());
        int YMin = (int) (this.getHeight() / 2.0 - generator.getSize() / 2.0 * generator.getScale());
        int XMax = (int) (this.getWidth() / 2.0 + generator.getSize() / 2.0 * generator.getScale());
        int XMin = (int) (this.getWidth() / 2.0 - generator.getSize() / 2.0 * generator.getScale());

        // fill grid squares
        for (int y = 0; y < generator.getMaze().length; y++) {
            for (int x = 0; x < generator.getMaze()[0].length; x++) {
                if(generator.getHistory().size() <= 0) continue;
                if (generator.getMaze()[x][y] != IterativeMaze.TILE_DEFAULT) {
                    if (generator.getHistory().peek().x == x && generator.getHistory().peek().y == y) {
                        if (generator.isColorMaze()) {
                            g.setColor(Color.WHITE);
                        } else {
                            g.setColor(Color.RED);
                        }
                    } else {
                        g.setColor(generator.getColor(x, y));
                    }

                    g.fillRect(XMin + x * generator.getScale(), YMin + y * generator.getScale(), generator.getScale(), generator.getScale());
                }
            }
        }

        if (generator.isShowGrid()) {
            g.setColor(Color.DARK_GRAY);

            // horizontal lines
            for (int y = YMin; y <= YMax; y += generator.getScale()) {
                g.drawLine(XMax, y, XMin, y);
            }

            // vertical lines
            for (int x = XMin; x <= XMax; x += generator.getScale()) {
                g.drawLine(x, YMax, x, YMin);
            }
        }

        // debug info
        g.setColor(Color.WHITE);
        g.drawString( "Stack Size: " + generator.getHistory().size(), 15, 25);
        g.drawString( "Squares Filled: " + generator.getNumFilled(), 15, 40);
    }
}
