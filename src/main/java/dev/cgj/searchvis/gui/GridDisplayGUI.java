package dev.cgj.searchvis.gui;

import dev.cgj.searchvis.grid.Grid;
import dev.cgj.searchvis.grid.GridSearch;
import dev.cgj.searchvis.grid.GridSearchNode;
import dev.cgj.searchvis.grid.ObstacleNode;
import dev.cgj.searchvis.search.SearchAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class GridDisplayGUI extends JFrame {
    private static final Logger log = LoggerFactory.getLogger(GridDisplayGUI.class);
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    GridSearchWorker searchWorker;
    private JPanel rootPanel;
    private JButton saveAsImageButton;
    private JButton resetButton;
    private JButton startButton;
    private JComboBox<SearchAlgorithm> algorithmSelector;
    private GridDisplayPanel<GridSearchNode, SearchColoringStrategy> gridDisplayPanel;
    private JSlider colorRangeSlider;
    private JSlider colorShiftSlider;
    private JSlider saturationSlider;
    private JSlider brightnessSlider;
    private JLabel colorShiftLabel;
    private JLabel colorRangeLabel;
    private JLabel brightnessLabel;
    private JLabel saturationLabel;
    private JSpinner widthSpinner;
    private JSpinner heightSpinner;
    private JSpinner scaleSpinner;
    private JCheckBox showGridLinesCheckBox;

    public GridDisplayGUI() {
        $$$setupUI$$$();
        colorShiftSlider.addChangeListener(new SliderLabelChangeListener(colorShiftSlider, colorShiftLabel));
        colorRangeSlider.addChangeListener(new SliderLabelChangeListener(colorRangeSlider, colorRangeLabel));
        brightnessSlider.addChangeListener(new SliderLabelChangeListener(brightnessSlider, brightnessLabel));
        saturationSlider.addChangeListener(new SliderLabelChangeListener(saturationSlider, saturationLabel));

        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            GridSearch gridSearch = getGridSearchWithSelectedValues();
            gridDisplayPanel.setColoringStrategy(new SearchColoringStrategy(gridSearch));
            searchWorker = new GridSearchWorker(gridDisplayPanel, gridSearch);
            searchWorker.addPropertyChangeListener(propertyChange -> {
                if ("state".equals(propertyChange.getPropertyName())
                        && propertyChange.getNewValue() == SwingWorker.StateValue.DONE) {
                    startButton.setEnabled(true);
                }
            });
            searchWorker.execute();
        });

        resetButton.addActionListener(e -> {
            if (searchWorker != null) {
                log.info("Cancelling search worker");
                searchWorker.cancel(true);
            }
            GridSearch gridSearch = getGridSearchWithSelectedValues();
            gridDisplayPanel.setGrid(gridSearch.getSearchGrid());
            gridDisplayPanel.setColoringStrategy(new SearchColoringStrategy(gridSearch));
            gridDisplayPanel.repaint();
        });

        saveAsImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save As Image");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new FileNameExtensionFilter("PNG Image", "png"));
            fileChooser.setAcceptAllFileFilterUsed(false);
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                if (!path.toLowerCase(Locale.ROOT).endsWith(".png")) {
                    path += ".png";
                }
                gridDisplayPanel.saveAsImage(new File(path));
            }
        });

        colorShiftSlider.addChangeListener(e -> {
            gridDisplayPanel.getColoringStrategy().setColorShift(colorShiftSlider.getValue() / 100f);
            gridDisplayPanel.repaint();
        });

        colorRangeSlider.addChangeListener(e -> {
            gridDisplayPanel.getColoringStrategy().setColorRange(colorRangeSlider.getValue() / 100f);
            gridDisplayPanel.repaint();
        });

        brightnessSlider.addChangeListener(e -> {
            gridDisplayPanel.getColoringStrategy().setBrightness(brightnessSlider.getValue() / 100f);
            gridDisplayPanel.repaint();
        });

        saturationSlider.addChangeListener(e -> {
            gridDisplayPanel.getColoringStrategy().setSaturation(saturationSlider.getValue() / 100f);
            gridDisplayPanel.repaint();
        });

        gridDisplayPanel.addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            gridDisplayPanel.zoom(-notches);
            scaleSpinner.setValue(gridDisplayPanel.getScale());
        });

        scaleSpinner.addChangeListener(e -> {
            gridDisplayPanel.setScale((int) scaleSpinner.getValue());
            gridDisplayPanel.repaint();
        });

        showGridLinesCheckBox.addChangeListener(e -> {
            gridDisplayPanel.setShowGridLines(showGridLinesCheckBox.isSelected());
            gridDisplayPanel.repaint();
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
                log.error("Failed to set system look and feel. The default will be used instead.", e);
            }
            GridDisplayGUI gridDisplayGUI = new GridDisplayGUI();
            gridDisplayGUI.setContentPane(gridDisplayGUI.rootPanel);
            gridDisplayGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gridDisplayGUI.setSize(1200, 900);
            gridDisplayGUI.setTitle("Grid Search Visualization");
            URL ImageUrl = gridDisplayGUI.getClass().getClassLoader().getResource("window_icon_64x64.png");
            Image image = Toolkit.getDefaultToolkit().getImage(ImageUrl);
            gridDisplayGUI.setIconImage(image);
            gridDisplayGUI.setLocationRelativeTo(null);
            gridDisplayGUI.setVisible(true);
        });
    }

    private GridSearch getGridSearchWithSelectedValues() {
        Comparator<GridSearchNode> comparator = Optional.ofNullable(algorithmSelector)
                .map(selectedItem -> (SearchAlgorithm) selectedItem.getSelectedItem())
                .map(SearchAlgorithm::getComparator)
                .orElseGet(() -> {
                    log.error("No search algorithm selected. Defaulting to RandomDepthFirstComparator.");
                    return SearchAlgorithm.RANDOM_DFS.getComparator();
                });

        Grid<ObstacleNode> obstacleNodeGrid = new Grid<>(
                ObstacleNode::new,
                (int) widthSpinner.getValue(),
                (int) heightSpinner.getValue()
        );

        Point selectedPoint = gridDisplayPanel.getSelectedPoint().orElse(new Point());
        return new GridSearch(comparator, obstacleNodeGrid, selectedPoint);
    }

    private void createUIComponents() {
        widthSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_WIDTH, 1, 100000, 1));
        heightSpinner = new JSpinner(new SpinnerNumberModel(DEFAULT_HEIGHT, 1, 100000, 1));
        algorithmSelector = new JComboBox<>(SearchAlgorithm.values());
        GridSearch defaultGridSearch = new GridSearch(
                SearchAlgorithm.RANDOM_DFS.getComparator(),
                new Grid<>(ObstacleNode::new, DEFAULT_WIDTH, DEFAULT_HEIGHT),
                new Point()
        );
        gridDisplayPanel = new GridDisplayPanel<>(defaultGridSearch.getSearchGrid(), new SearchColoringStrategy(defaultGridSearch));
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout(0, 0));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setOneTouchExpandable(false);
        splitPane1.setResizeWeight(1.0);
        rootPanel.add(splitPane1, BorderLayout.CENTER);
        final JScrollPane scrollPane1 = new JScrollPane();
        splitPane1.setRightComponent(scrollPane1);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridBagLayout());
        scrollPane1.setViewportView(panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel2, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel2.add(algorithmSelector, gbc);
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, 14, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setHorizontalAlignment(0);
        label1.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("Labels", "algorithm"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel2.add(label1, gbc);
        final JSeparator separator1 = new JSeparator();
        separator1.setEnabled(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1.add(separator1, gbc);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel3, gbc);
        colorShiftSlider = new JSlider();
        colorShiftSlider.setPaintLabels(false);
        colorShiftSlider.setPaintTicks(false);
        colorShiftSlider.setValue(0);
        colorShiftSlider.setValueIsAdjusting(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(colorShiftSlider, gbc);
        colorRangeSlider = new JSlider();
        colorRangeSlider.setMaximum(100);
        colorRangeSlider.setPaintTrack(true);
        colorRangeSlider.setValue(100);
        colorRangeSlider.setValueIsAdjusting(false);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(colorRangeSlider, gbc);
        brightnessSlider = new JSlider();
        brightnessSlider.setValue(90);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(brightnessSlider, gbc);
        saturationSlider = new JSlider();
        saturationSlider.setValue(70);
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel3.add(saturationSlider, gbc);
        final JLabel label2 = new JLabel();
        Font label2Font = this.$$$getFont$$$(null, Font.BOLD, 14, label2.getFont());
        if (label2Font != null) label2.setFont(label2Font);
        label2.setHorizontalAlignment(0);
        label2.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("Labels", "color"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel3.add(label2, gbc);
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(10, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel4, gbc);
        final JLabel label3 = new JLabel();
        label3.setHorizontalTextPosition(2);
        this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("Labels", "shift"));
        panel4.add(label3, BorderLayout.WEST);
        colorShiftLabel = new JLabel();
        colorShiftLabel.setHorizontalTextPosition(2);
        colorShiftLabel.setText("");
        panel4.add(colorShiftLabel, BorderLayout.CENTER);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(10, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel5, gbc);
        final JLabel label4 = new JLabel();
        this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("Labels", "saturation"));
        panel5.add(label4, BorderLayout.WEST);
        saturationLabel = new JLabel();
        saturationLabel.setText("");
        panel5.add(saturationLabel, BorderLayout.CENTER);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new BorderLayout(10, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel6, gbc);
        final JLabel label5 = new JLabel();
        this.$$$loadLabelText$$$(label5, this.$$$getMessageFromBundle$$$("Labels", "brightness"));
        panel6.add(label5, BorderLayout.WEST);
        brightnessLabel = new JLabel();
        brightnessLabel.setText("");
        panel6.add(brightnessLabel, BorderLayout.CENTER);
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new BorderLayout(10, 0));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        panel3.add(panel7, gbc);
        final JLabel label6 = new JLabel();
        this.$$$loadLabelText$$$(label6, this.$$$getMessageFromBundle$$$("Labels", "range"));
        panel7.add(label6, BorderLayout.WEST);
        colorRangeLabel = new JLabel();
        colorRangeLabel.setText("");
        panel7.add(colorRangeLabel, BorderLayout.CENTER);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel8, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(widthSpinner, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(heightSpinner, gbc);
        scaleSpinner = new JSpinner();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel8.add(scaleSpinner, gbc);
        final JLabel label7 = new JLabel();
        this.$$$loadLabelText$$$(label7, this.$$$getMessageFromBundle$$$("Labels", "width"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label7, gbc);
        final JLabel label8 = new JLabel();
        this.$$$loadLabelText$$$(label8, this.$$$getMessageFromBundle$$$("Labels", "height"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label8, gbc);
        final JLabel label9 = new JLabel();
        this.$$$loadLabelText$$$(label9, this.$$$getMessageFromBundle$$$("Labels", "scale"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label9, gbc);
        final JLabel label10 = new JLabel();
        Font label10Font = this.$$$getFont$$$(null, Font.BOLD, 14, label10.getFont());
        if (label10Font != null) label10.setFont(label10Font);
        label10.setHorizontalAlignment(0);
        label10.setHorizontalTextPosition(0);
        this.$$$loadLabelText$$$(label10, this.$$$getMessageFromBundle$$$("Labels", "grid"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(label10, gbc);
        showGridLinesCheckBox = new JCheckBox();
        this.$$$loadButtonText$$$(showGridLinesCheckBox, this.$$$getMessageFromBundle$$$("Labels", "show.grid.lines"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        panel8.add(showGridLinesCheckBox, gbc);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        panel1.add(panel9, gbc);
        saveAsImageButton = new JButton();
        this.$$$loadButtonText$$$(saveAsImageButton, this.$$$getMessageFromBundle$$$("Labels", "save.as.image"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(saveAsImageButton, gbc);
        resetButton = new JButton();
        this.$$$loadButtonText$$$(resetButton, this.$$$getMessageFromBundle$$$("Labels", "reset"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(resetButton, gbc);
        startButton = new JButton();
        this.$$$loadButtonText$$$(startButton, this.$$$getMessageFromBundle$$$("Labels", "start"));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel9.add(startButton, gbc);
        splitPane1.setLeftComponent(gridDisplayPanel);
        label7.setLabelFor(widthSpinner);
        label8.setLabelFor(heightSpinner);
        label9.setLabelFor(scaleSpinner);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadLabelText$$$(JLabel component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setDisplayedMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    private void $$$loadButtonText$$$(AbstractButton component, String text) {
        StringBuffer result = new StringBuffer();
        boolean haveMnemonic = false;
        char mnemonic = '\0';
        int mnemonicIndex = -1;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '&') {
                i++;
                if (i == text.length()) break;
                if (!haveMnemonic && text.charAt(i) != '&') {
                    haveMnemonic = true;
                    mnemonic = text.charAt(i);
                    mnemonicIndex = result.length();
                }
            }
            result.append(text.charAt(i));
        }
        component.setText(result.toString());
        if (haveMnemonic) {
            component.setMnemonic(mnemonic);
            component.setDisplayedMnemonicIndex(mnemonicIndex);
        }
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
