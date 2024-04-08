package org.divyansh.components;

import material.containers.MaterialPanel;
import net.miginfocom.swing.MigLayout;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.fluentui.FluentUiFilledAL;
import org.kordamp.ikonli.fluentui.FluentUiFilledMZ;
import org.kordamp.ikonli.materialdesign2.*;

public class Keypad extends MaterialPanel {
    private static final String KEY_CONTRAINT = "grow";
    private IKeypadHandler keypadHandler;

    private MaterialPanel panel1 = new MaterialPanel(createLayout("nogrid,fill,flowx, insets 0, gap 0"));
    private MaterialPanel panel2 = new MaterialPanel(createLayout("nogrid,,fill,flowx, insets 0, gap 0"));
    private MaterialPanel subPanel1 = new MaterialPanel(createLayout("fill,wrap 3, insets 0, gap 0"));
    private MaterialPanel subPanel2 = new MaterialPanel(createLayout("fill,wrap 2, insets 0, gap 0"));


    public Keypad(IKeypadHandler keypadHandler) {
        super(new MigLayout("nogrid,flowy,fill"));
        add(panel1,"top,growx");
        add(panel2,"grow");
        panel2.add(subPanel1,"gapx 0,grow");
        panel2.add(subPanel2,"gapx 0,grow");
//        add(panel4,"east,gap 0");
        this.keypadHandler = keypadHandler;
        KeyFactory factory = new KeyFactory();

        panel1.add(factory.create(SpecialKeyValues.CLEAR_ALL),KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.BACKSPACE), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.PI), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.MOD), KEY_CONTRAINT);

        subPanel1.add(factory.create(1), KEY_CONTRAINT);
        subPanel1.add(factory.create(2), KEY_CONTRAINT);
        subPanel1.add(factory.create(3), KEY_CONTRAINT);
        subPanel1.add(factory.create(4), KEY_CONTRAINT);
        subPanel1.add(factory.create(5), KEY_CONTRAINT);
        subPanel1.add(factory.create(6), KEY_CONTRAINT);
        subPanel1.add(factory.create(7), KEY_CONTRAINT);
        subPanel1.add(factory.create(8), KEY_CONTRAINT);
        subPanel1.add(factory.create(9), KEY_CONTRAINT);
        subPanel1.add(factory.create(SpecialKeyValues.NEGATE), KEY_CONTRAINT);
        subPanel1.add(factory.create(0), KEY_CONTRAINT);
        subPanel1.add(factory.create(SpecialKeyValues.EQUAL_TO), KEY_CONTRAINT);

        subPanel2.add(factory.create(SpecialKeyValues.ADD), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.SUBTRACT), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.MULTIPLY), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.DIVIDE), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.FACTORIAL), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.SQ_RT), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.LOG), KEY_CONTRAINT);
        subPanel2.add(factory.create(SpecialKeyValues.LN), KEY_CONTRAINT);

    }

    private MigLayout createLayout(String s) {
        return new MigLayout(s);
    }

    private class KeyFactory {
        private NumericButton create(int val) {
            NumericButton button = new NumericButton(val);
            button.addLeftClickListener(e -> {
                keypadHandler.handleNumericKeyPress(button.getValue());
            });
            return button;
        }

        private SpecialButton create(SpecialKeyValues val) {
            Ikon icon = getIcon(val);
            SpecialButton button = new SpecialButton(icon, val);
            button.addLeftClickListener(e -> {
                keypadHandler.handleSpecialKeyPress(button.getValue());
            });
            return button;
        }

        private Ikon getIcon(SpecialKeyValues val) {
            return switch (val) {
                case ADD -> MaterialDesignP.PLUS;
                case SUBTRACT -> MaterialDesignM.MINUS;
                case MULTIPLY -> MaterialDesignW.WINDOW_CLOSE;
                case DIVIDE -> MaterialDesignD.DIVISION;
                case SQ_RT -> MaterialDesignS.SQUARE_ROOT;
                case POWER -> MaterialDesignE.EXPONENT;
                case FACTORIAL -> MaterialDesignE.EXCLAMATION;
                case BACKSPACE -> MaterialDesignB.BACKSPACE;
                case CLEAR_ALL -> MaterialDesignD.DELETE;
                case EQUAL_TO -> MaterialDesignE.EQUAL;
                case PI -> MaterialDesignP.PI;
                default -> null;
            };
        }
    }
}
