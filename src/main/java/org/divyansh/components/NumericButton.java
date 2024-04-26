package org.divyansh.components;

import material.component.MaterialIconButton;

public class NumericButton extends MaterialIconButton {
    private int value;

    public NumericButton(int value) {
        super(null,String.valueOf(value), true);
        this.value = value;
        this.setElevation(Settings.ELEVATION_LEVEL_1);
        this.setAlignmentX(0.5f);
        this.setFontSize(Settings.KEY_FONT_SIZE);
        this.setMinimumSize(Settings.KEY_SIZE);
    }

    public int getValue() {
        return value;
    }
}
