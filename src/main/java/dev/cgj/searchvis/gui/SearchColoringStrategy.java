package dev.cgj.searchvis.gui;

import dev.cgj.searchvis.grid.Grid;
import dev.cgj.searchvis.grid.GridSearch;
import dev.cgj.searchvis.grid.GridSearchNode;

import java.awt.Color;

public class SearchColoringStrategy implements GridColoringStrategy<GridSearchNode> {
    float colorRange = 1f;
    float colorShift = 0f;
    float saturation = 0.7f;
    float brightness = 0.9f;
    final GridSearch gridSearch;

    public SearchColoringStrategy(GridSearch gridSearch) {
        this.gridSearch = gridSearch;
    }

    @Override
    public Color getColor(Grid<GridSearchNode> grid, int x, int y) {
        GridSearchNode node = grid.getNode(x, y);
        if (node == null) return Color.RED;
        if (node.isUnvisited()) return Color.BLACK;
        float hue = colorShift + (colorRange * ((float) node.getPathLength() / gridSearch.getMaxQueue()));
        return Color.getHSBColor(hue, saturation, brightness);
    }

    public void setColorRange(float colorRange) {
        this.colorRange = colorRange;
    }

    public void setColorShift(float colorShift) {
        this.colorShift = colorShift;
    }

    public void setSaturation(float saturation) {
        this.saturation = saturation;
    }

    public void setBrightness(float brightness) {
        this.brightness = brightness;
    }
}
