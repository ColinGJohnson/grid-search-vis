package net.colinjohnson.gridsearch.gui;

import net.colinjohnson.gridsearch.IterativeMaze;

import javax.swing.*;
import java.awt.*;

/**
 * MazeInterface provides a java swing wrapper for an IterativeMaze generator which can control the generator's
 * settings and display its progress during generation.
 */
public class MazeInterface {

    // window and component wrapper
    private JFrame frame;
    private JPanel mainPanel;

    // drawable display area
    private JScrollPane scrollPane;
    private JPanel displayPanel;

    // interface
    private JPanel interfacePanel;
    private JToggleButton colorMazeToggle;

    // maze generator
    IterativeMaze generator;

    /**
     * MazeInterface constructor. Initializes GUI elements and creates a new maze generator to use.
     */
    public MazeInterface(){

        // initialize maze generator
        generator = new IterativeMaze();

        // create outer frame
        frame = new JFrame("Backtracking Maze Generator");
        frame.setSize(800,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());
        frame.add(mainPanel = new JPanel(new BorderLayout()), BorderLayout.CENTER);

        // create scrollable area and drawable area within
        scrollPane = new JScrollPane();
        scrollPane.add(displayPanel = new MazePanel(generator));

        // add the display area to the center of the window
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // create the interface
        interfacePanel = new JPanel();
        colorMazeToggle = new JToggleButton("Color Maze");
        interfacePanel.add(colorMazeToggle);

        // add the interface to the left of the window
        mainPanel.add(interfacePanel, BorderLayout.EAST);

        // make the completed window visible
        frame.setVisible(true);
    }
}
