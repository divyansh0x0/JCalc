package org.divyansh;

import material.constants.Size;
import material.containers.MaterialPanel;
import material.window.MaterialWindow;
import material.window.MaterialWindowGrip;
import org.divyansh.components.IKeypadHandler;
import org.divyansh.components.Keypad;
import org.divyansh.components.SpecialKeyValues;

public class Main {
    private MaterialPanel screen;
    private static final Size WINDOW_SIZE = new Size(400,500);
    private static IKeypadHandler KeypadHandler = new KeypadHandlerImpl();
    public static void main(String[] args) {
        MaterialWindow materialWindow = new MaterialWindow("Calculator",WINDOW_SIZE,true,true);
        materialWindow.getRootPanel().add(new Keypad(KeypadHandler),"grow");
        materialWindow.setGrip(MaterialWindowGrip.EXCLUDE_CAPTION_BAR_WIDTH);
        materialWindow.pack();
        materialWindow.setVisible(true);
    }

    private static class KeypadHandlerImpl implements IKeypadHandler{

        @Override
        public void handleNumericKeyPress(int value) {

        }

        @Override
        public void handleSpecialKeyPress(SpecialKeyValues value) {

        }
    }
}