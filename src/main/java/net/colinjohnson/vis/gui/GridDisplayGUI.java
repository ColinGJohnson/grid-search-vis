package net.colinjohnson.vis.gui;

import net.colinjohnson.vis.grid.GridSearch;
import net.colinjohnson.vis.grid.GridSearchNode;
import net.colinjohnson.vis.search.RandomDepthFirstComparator;
import net.colinjohnson.vis.search.SearchAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

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
    private JLabel colorShiftLabel;
    private JLabel colorRangeLabel;
    private JLabel brightnessLabel;
    private JLabel saturationLabel;
    private JSpinner widthSpinner;
    private JSpinner spinner1;
    private JSpinner spinner2;
    private JCheckBox showGridLinesCheckBox;


    public GridDisplayGUI() {
        startButton.addActionListener(e -> {

        });

        resetButton.addActionListener(e -> {

        });

        saveAsImageButton.addActionListener(e -> {

        });

        colorShiftSlider.addChangeListener(new SliderLabelChangeListener(colorShiftSlider, colorShiftLabel));
        colorRangeSlider.addChangeListener(new SliderLabelChangeListener(colorRangeSlider, colorRangeLabel));
        brightnessSlider.addChangeListener(new SliderLabelChangeListener(brightnessSlider, brightnessLabel));
        saturationSlider.addChangeListener(new SliderLabelChangeListener(saturationSlider, saturationLabel));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
            log.error("Failed to set system look and feel", e);
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
        algorithmSelector = new JComboBox<>(SearchAlgorithm.values());
    }
}
