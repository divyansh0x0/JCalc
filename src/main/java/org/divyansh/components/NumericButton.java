package org.divyansh.components;

import material.component.MaterialIconButton;

import java.awt.*;
import java.util.Set;

public class NumericButton extends MaterialIconButton {
    private int value;

    public NumericButton(int value) {
        super(null,String.valueOf(value), true);
        this.value = value;
        this.setAlignmentX(0.5f);
        this.setMinimumSize(Settings.KEY_SIZE);
    }

    public int getValue() {
        return value;
    }
}
