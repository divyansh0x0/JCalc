package org.divyansh.components;

import material.component.MaterialIconButton;
import org.kordamp.ikonli.Ikon;

public class SpecialButton extends MaterialIconButton {
    private SpecialKeyValues value;

    public SpecialButton(Ikon icon, SpecialKeyValues value) {
        super(icon,"",true);
        if (icon == null) {
            String text = switch (value){
                case DOT -> ".";
                case E -> "e";
                case BRACKETOPEN -> "(";
                case SQUARE_BRACKETOPEN -> "[";
                case SQUARE_CURLYBRACKETOPEN -> "{";

                case BRACKETCLOSE -> ")";
                case SQUARE_BRACKETCLOSE -> "]";
                case SQUARE_CURLYBRACKETCLOSE-> "}";
                default -> createName(value);
            };
            setText(text);
        }
        this.setElevation(Settings.ELEVATION_LEVEL_2);
        this.setAlignmentX(0.5f);
        this.setIconSizeRatio(Settings.KEY_ICON_SIZE_RATIO);
        this.setFontSize(30);
        this.setMinimumSize(Settings.KEY_SIZE);

        this.value = value;
    }

    private String createName(SpecialKeyValues value) {
        return value.toString().replaceAll("_", "").toLowerCase();
    }

    public SpecialKeyValues getValue() {
        return value;
    }
}
