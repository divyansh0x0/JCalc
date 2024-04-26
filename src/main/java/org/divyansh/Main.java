package org.divyansh;

import material.constants.Size;
import material.containers.MaterialPanel;
import material.utils.Log;
import material.window.MaterialWindow;
import material.window.MaterialWindowGrip;
import org.divyansh.calculator.tokens.FunctionType;
import org.divyansh.calculator.tokens.NumberToken;
import org.divyansh.calculator.tokens.OperatorType;
import org.divyansh.components.*;

import java.math.MathContext;
import java.math.RoundingMode;

public class Main {
    public static final MathContext MATH_CONTEXT = new MathContext(10, RoundingMode.HALF_EVEN);
    private static final Size WINDOW_SIZE = new Size(400,500);
    private static IKeypadHandler KeypadHandler = new KeypadHandlerImpl();
    public static final CalculatorPanel COMP = new CalculatorPanel(KeypadHandler);
    private static final Screen screen = new Screen(Settings.ELEVATION_LEVEL_0);
    public static void main(String[] args) {

//        boolean isFontSet = MaterialFonts.getInstance().setDefaultFont("NotoSansMath.ttf");
//        Log.warn("IS CALCULATOR FONT SET: " + isFontSet);
        MaterialWindow materialWindow = new MaterialWindow("Calculator",WINDOW_SIZE,true,true);
        materialWindow.getRootPanel().add(screen,"top,grow,height 50%");
        materialWindow.getRootPanel().add(COMP,"grow");
        materialWindow.setGrip(MaterialWindowGrip.EXCLUDE_CAPTION_BAR_WIDTH);

        materialWindow.pack();
        materialWindow.setVisible(true);
    }

    private static class KeypadHandlerImpl implements IKeypadHandler{

        @Override
        public void handleNumericKeyPress(int value) {
            screen.addDigit(value);
        }

        @Override
        public void handleOperatorKeyPress(OperatorType value) {
            screen.addOperator(value);
        }

        @Override
        public void handleFunctionKeyPress(FunctionType value) {
            screen.addFunction(value);
        }

        @Override
        public void handleSpecialKeyPress(SpecialKeyValues value) {

        }
    }
}