package org.divyansh.components;

import material.component.MaterialIconButton;
import org.divyansh.calculator.tokens.OperatorType;
import org.kordamp.ikonli.Ikon;

import java.util.Locale;

public class OperatorButton extends MaterialIconButton {
    private OperatorType value;

    public OperatorButton(Ikon icon, OperatorType value) {
        super(icon,"",true);
        if (icon == null) {
            String text = switch (value){
                case REMAINDER ->  "mod";
                default -> createName(value.toString().toLowerCase(Locale.ENGLISH));
            };
            setText(text);
        }
        this.setElevation(Settings.ELEVATION_LEVEL_2);
        this.setAlignmentX(0.5f);
        this.setIconSizeRatio(Settings.KEY_ICON_SIZE_RATIO);
        this.setMinimumSize(Settings.KEY_SIZE);

        this.value = value;
    }


    private String createName(String value) {
        return value.replaceAll("_", "").toLowerCase();
    }

    public OperatorType getValue() {
        return value;
    }
}
