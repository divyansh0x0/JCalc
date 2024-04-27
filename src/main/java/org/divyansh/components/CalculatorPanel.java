package org.divyansh.components;

import material.component.MaterialIconButton;
import material.containers.MaterialPanel;
import net.miginfocom.swing.MigLayout;
import org.divyansh.calculator.tokens.FunctionType;
import org.divyansh.calculator.tokens.OperatorType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.materialdesign2.*;

import java.util.Objects;

public class CalculatorPanel extends MaterialPanel {
    private static final String KEY_CONTRAINT = "grow";
    private IKeypadHandler keypadHandler;



    public CalculatorPanel(IKeypadHandler keypadHandler) {
        super(new MigLayout("nogrid,flowy,fill"));
        MaterialPanel panel1 = new MaterialPanel(createLayout("nogrid,fill,flowx, insets 0, gap 0"));
        MaterialPanel panel2 = new MaterialPanel(createLayout("nogrid,,fill,flowx, insets 0, gap 0"));
        add(panel1,"top,growx");
        add(panel2,"grow");
        MaterialPanel subPanel1 = new MaterialPanel(createLayout("fill,wrap 3, insets 0, gap 0"));
        MaterialPanel subPanel2 = new MaterialPanel(createLayout("fill,wrap 2, insets 0, gap 0"));
        panel2.add(subPanel1,"gapx 0,grow");
        panel2.add(subPanel2,"gapx 0,grow");
//        add(panel4,"east,gap 0");
        this.keypadHandler = keypadHandler;
        KeyFactory factory = new KeyFactory();

        panel1.add(factory.create(SpecialKeyValues.CLEAR_ALL),KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.BACKSPACE), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.PI), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.E), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.BRACKETOPEN), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.BRACKETCLOSE), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.SQUARE_BRACKETOPEN), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.SQUARE_BRACKETCLOSE), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.SQUARE_CURLYBRACKETOPEN), KEY_CONTRAINT);
        panel1.add(factory.create(SpecialKeyValues.SQUARE_CURLYBRACKETCLOSE), KEY_CONTRAINT);

        subPanel1.add(factory.create(1), KEY_CONTRAINT);
        subPanel1.add(factory.create(2), KEY_CONTRAINT);
        subPanel1.add(factory.create(3), KEY_CONTRAINT);
        subPanel1.add(factory.create(4), KEY_CONTRAINT);
        subPanel1.add(factory.create(5), KEY_CONTRAINT);
        subPanel1.add(factory.create(6), KEY_CONTRAINT);
        subPanel1.add(factory.create(7), KEY_CONTRAINT);
        subPanel1.add(factory.create(8), KEY_CONTRAINT);
        subPanel1.add(factory.create(9), KEY_CONTRAINT);
        subPanel1.add(factory.create(SpecialKeyValues.DOT), KEY_CONTRAINT);
        subPanel1.add(factory.create(0), KEY_CONTRAINT);
        subPanel1.add(factory.create(SpecialKeyValues.EQUAL_TO), KEY_CONTRAINT);

        OperatorType[] operators  = OperatorType.values();
        for(OperatorType op : operators){
            if (Objects.requireNonNull(op) != OperatorType.UNKNOWN) {
                MaterialIconButton component =  factory.create(op);
                subPanel2.add(component, KEY_CONTRAINT);
            }
        }
        for (FunctionType func: FunctionType.values()){

        }

    }


    @Contract("_ -> new")
    private @NotNull MigLayout createLayout(String s) {
        return new MigLayout(s);
    }

    private class KeyFactory {
        private @NotNull NumericButton create(int val) {
            NumericButton button = new NumericButton(val);
            button.addLeftClickListener(e -> {
                keypadHandler.handleNumericKeyPress(button.getValue());
            });
            return button;
        }
        private @NotNull OperatorButton create(OperatorType val) {
            Ikon icon = getIcon(val);
            OperatorButton button = new OperatorButton(icon, val);
            button.addLeftClickListener(e -> {
                keypadHandler.handleOperatorKeyPress(button.getValue());
            });
            return button;
        }
        @Contract(pure = true)
        private Ikon getIcon(@NotNull OperatorType val) {
            return switch (val) {
                case PLUS -> MaterialDesignP.PLUS;
                case MINUS -> MaterialDesignM.MINUS;
                case MULTIPLICATION -> MaterialDesignW.WINDOW_CLOSE;
                case DIVISION -> MaterialDesignD.DIVISION;
                case EXPONENTIATION -> MaterialDesignE.EXPONENT;
                case FACTORIAL -> MaterialDesignE.EXCLAMATION;
                default -> null;
            };
        }
        private @NotNull FunctionButton create(FunctionType val){

            FunctionButton button = new FunctionButton(val);
            button.addLeftClickListener(e -> {
                keypadHandler.handleFunctionKeyPress(button.getValue());
            });
            return button;
        }
        @Contract(pure = true)
        private Ikon getIcon(@NotNull SpecialKeyValues val) {
            return switch (val) {
                case BACKSPACE -> MaterialDesignB.BACKSPACE;
                case CLEAR_ALL -> MaterialDesignD.DELETE;
                case EQUAL_TO -> MaterialDesignE.EQUAL;
                case PI -> MaterialDesignP.PI;
                default -> null;
            };
        }

        private @NotNull SpecialButton create(SpecialKeyValues val) {
            Ikon icon = getIcon(val);
            SpecialButton button = new SpecialButton(icon, val);
            button.addLeftClickListener(e -> {
                keypadHandler.handleSpecialKeyPress(button);
            });
            return button;
        }
    }
}
