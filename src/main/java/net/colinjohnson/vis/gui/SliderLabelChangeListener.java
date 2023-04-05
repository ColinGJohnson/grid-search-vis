package net.colinjohnson.vis.gui;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class SliderLabelChangeListener implements ChangeListener {
    private final JSlider slider;
    private final JLabel label;

    public SliderLabelChangeListener(JSlider slider, JLabel label) {
        this.slider = slider;
        this.label = label;
        updateText();
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        updateText();
    }

    /**
     * Updates the label to display the current value of the slider.
     */
    private void updateText() {
        label.setText(String.format("%.2f", slider.getValue() / 100f));
    }
}
