package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.GridSearch;
import net.colinjohnson.vis.grid.GridSearchNode;
import net.colinjohnson.vis.search.RandomDepthFirstComparator;
import net.colinjohnson.vis.search.SearchAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GridDisplayGUI extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(GridDisplayGUI.class);
    private JPanel rootPanel;
    private JButton saveAsImageButton;
    private JButton resetButton;
    private JButton startButton;
    private JComboBox<SearchAlgorithm> algorithmSelector;
    private GridDisplayPanel<GridSearchNode> gridDisplayPanel;
    private JSlider colorRangeSlider;
    private JSlider colorShiftSlider;
    private JSlider saturationSlider;
    private JSlider brightnessSlider;


    public GridDisplayGUI() {
        algorithmSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        saveAsImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            //log.error("Failed to set system look and feel", e);
        }

        SwingUtilities.invokeLater(() -> {
            GridDisplayGUI gridDisplayGUI = new GridDisplayGUI();
            gridDisplayGUI.setContentPane(gridDisplayGUI.rootPanel);
            gridDisplayGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gridDisplayGUI.setSize(1000, 700);
            gridDisplayGUI.setTitle("Grid Search Visualization");
            gridDisplayGUI.setLocationRelativeTo(null);
            gridDisplayGUI.setVisible(true);
        });
    }

    private void createUIComponents() {
        GridSearch gridSearch = new GridSearch(new RandomDepthFirstComparator());
        gridDisplayPanel = new GridDisplayPanel<>(gridSearch.getGrid(), gridSearch);

    }
}
