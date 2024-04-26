package org.divyansh.components;

import material.component.MaterialIconButton;
import org.divyansh.calculator.tokens.FunctionType;

import java.util.Locale;

public class FunctionButton extends MaterialIconButton {
    private final String val;
    private final FunctionType type;
    public FunctionButton(FunctionType type) {
        super();
        this.type = type;
        this.val = switch (type){
            case ABSOLUTE_VALUE -> "abs(";
            case SIGNUM -> "sig(";
            case POWER -> "pow(";
            default -> type.toString().toLowerCase(Locale.ENGLISH) + "(";
        };
        setText(this.val + ")");
        this.setAlignmentX(0.5f);
        this.setIconSizeRatio(Settings.KEY_ICON_SIZE_RATIO);
        this.setMinimumSize(Settings.KEY_SIZE);
    }

    public FunctionType getValue() {
        return type;
    }
}
