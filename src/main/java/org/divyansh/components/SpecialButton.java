package org.divyansh.components;

import material.component.MaterialIconButton;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.Ikonli;

public class SpecialButton extends MaterialIconButton {
    private SpecialKeyValues value;

    public SpecialButton(Ikon icon, SpecialKeyValues value) {
        super(icon,"",true);
        if (icon == null) {
            setText(createName(value));
        }
        this.setAlignmentX(0.5f);
        this.setIconSizeRatio(0.3f);
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
