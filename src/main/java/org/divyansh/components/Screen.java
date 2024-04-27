package org.divyansh.components;

import material.Padding;
import material.animation.MaterialFixedTimer;
import material.component.MaterialComponent;
import material.theme.ThemeColors;
import material.theme.enums.Elevation;
import material.theme.models.ElevationModel;
import material.tools.ColorUtils;
import material.utils.Log;
import org.divyansh.Main;
import org.divyansh.calculator.tokens.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Screen extends MaterialComponent implements ElevationModel {
    private  IKeypadHandler KeypadHandler = new KeypadHandlerImpl();
    private static final float BLINKING_RATE = 2;
    private boolean makeCursorBright;
    private StringBuilder mathStringBuilder = new StringBuilder();
    private MaterialFixedTimer blinkerTimer = new MaterialFixedTimer(1000/BLINKING_RATE) {
        @Override
        public void tick(float deltaMillis) {
            makeCursorBright = !makeCursorBright;
            SwingUtilities.invokeLater(()->repaint());
        }
    };
    private int caretPosition = 0;
    private static int CornerRadius = 5;
    private static Padding pad= new Padding(5);
    private Color borderColor;
    private Elevation elevation = Elevation._1;
    private boolean isUnderUse = false;

    public Screen(Elevation elevation) throws HeadlessException {
        super();
        blinkerTimer.start();
        setElevation(elevation);
        setFontSize(Settings.KEY_FONT_SIZE*2);
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        isUnderUse = true;
                        SwingUtilities.invokeLater(()->repaint());
                    }

            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRoundRect(0,0,getWidth(),getHeight(),CornerRadius,CornerRadius);
        final FontMetrics fm = g.getFontMetrics();
        int xMin = pad.getLeft();
        int yMin = pad.getTop();
        int widthAvail = getWidth() - pad.getHorizontal();
        int heightAvail = getHeight() - pad.getVertical();
        g.setColor(borderColor);
        if(isUnderUse)
            g.drawRoundRect(xMin,yMin,widthAvail,heightAvail,CornerRadius,CornerRadius);
        Graphics2D g2d = (Graphics2D) g;
        drawText(g2d);
        g2d.setColor(ThemeColors.getTextPrimary());
        int tX = widthAvail;
        FontMetrics ft = g2d.getFontMetrics();
        int tY = (int) (((this.getHeight() - ft.getAscent()) * getAlignmentY()) + ft.getAscent()); //Text y coordinate
        String str = mathStringBuilder.toString();
        tX -= fm.stringWidth(str);
        g2d.drawString(str,tX,tY);


        //caret
        if(makeCursorBright){
            int caretHeight = getHeight() / 3;
            Log.info(caretPosition + "||" + mathStringBuilder.length());
            int y = (getHeight() - caretHeight)/2;
            int x = widthAvail - fm.stringWidth(mathStringBuilder.substring(getCaretPosition()));
            g2d.setColor(ThemeColors.getAccent());
            g2d.fillRect(x,y,3, caretHeight);
        }
    }
    private void decrementCaretPos() {
        if(caretPosition > 0)
            caretPosition--;
    }

    private void incrementCaretPosition() {
        if(caretPosition < mathStringBuilder.length())
            caretPosition++;
    }

    private void setCaretPosition(int index){
        if(index < mathStringBuilder.length() && index > 0)
            caretPosition = index;
    }
    private int getCaretPosition() {
        return caretPosition;
    }



    @Override
    protected void animateMouseEnter() {

    }

    @Override
    protected void animateMouseExit() {

    }

    @Override
    public void updateTheme() {
        Color bg;
        if(elevation  != null) {
            bg = ThemeColors.getColorByElevation(elevation);
            borderColor = ColorUtils.lighten(bg, 10);
        }
        else
            bg = ThemeColors.TransparentColor;
        animateBG(bg);
    }



    private void drawText(Graphics2D g2d) {

    }

    private void drawCaret(Graphics2D g2d) {

    }

    @Override
    public @Nullable Elevation getElevation() {
        return elevation;
    }

    @Override
    public ElevationModel setElevation(@Nullable Elevation elevation) {
        this.elevation = elevation;
        updateTheme();
        return this;
    }

    public IKeypadHandler getKeyHandler() {
        return KeypadHandler;
    }

    private class KeypadHandlerImpl implements IKeypadHandler{

        @Override
        public void handleNumericKeyPress(int value) {
            mathStringBuilder.insert(getCaretPosition(), value);
            incrementCaretPosition();
            SwingUtilities.invokeLater(Screen.this::repaint);
        }

        @Override
        public void handleOperatorKeyPress(OperatorType value) {
            mathStringBuilder.insert(getCaretPosition(), OperatorToken.create(value));

            incrementCaretPosition();
            SwingUtilities.invokeLater(Screen.this::repaint);
        }

        @Override
        public void handleFunctionKeyPress(FunctionType value) {
            mathStringBuilder.insert(getCaretPosition(), FunctionToken.create(value));
            incrementCaretPosition();
            SwingUtilities.invokeLater(Screen.this::repaint);
        }

        @Override
        public void handleSpecialKeyPress(SpecialButton btn) {
            switch (btn.getValue()){
                case BACKSPACE -> {
                    deleteChar(getCaretPosition());
                }
                default -> {
                    mathStringBuilder.insert(getCaretPosition(), btn.getText());
                    incrementCaretPosition();
                    SwingUtilities.invokeLater(Screen.this::repaint);
                }
            }

        }
    }

    private void deleteChar(int index) {
        if(index > 0 && index < mathStringBuilder.length()) {
            mathStringBuilder.deleteCharAt(index - 1);
            decrementCaretPos();
            SwingUtilities.invokeLater(this::repaint);
        }
    }

}
