package org.divyansh.components;

import material.Padding;
import material.animation.MaterialFixedTimer;
import material.component.MaterialComponent;
import material.theme.ThemeColors;
import material.theme.enums.Elevation;
import material.theme.models.ElevationModel;
import material.tools.ColorUtils;
import material.utils.Log;
import org.divyansh.calculator.DynamicLexer;
import org.divyansh.calculator.Lexer;
import org.divyansh.calculator.Parser;
import org.divyansh.calculator.tokens.*;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Screen extends MaterialComponent implements ElevationModel {
    private static final float BLINKING_RATE = 2;
    private static final String OPERATORS_STR = "()[]{},.+-/*^%";
    private int CARET_HEIGHT = 0;
    private int CARET_Y = 0;
    private int CARET_X = 0;
    private static int CARET_WIDTH = 3;
    private boolean showCaret;
    private long caretBlinkingPauseStartTime = 0;
    private boolean fullRepaintRequired = true;
    private StringBuilder text = new StringBuilder();

    private int caretPosition = 0;
    private static int CornerRadius = 5;
    private static Padding pad = new Padding(5);
    private Color borderColor;
    private Elevation elevation = Elevation._1;
    private boolean isUnderUse = false;
    private DynamicLexer lexer;
    private static final Parser parser = new Parser(Settings.OUTPUT_PRECISION);
    private IKeypadHandler KeyPadHandler = new KeyPadHandlerImpl();

    public Screen(Elevation elevation) throws HeadlessException {
        super();
        lexer = new DynamicLexer();
        blinkerTimer.start();
        setElevation(elevation);
        setFontSize(Settings.KEY_FONT_SIZE * 2);
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    isUnderUse = true;
                    requestFocusInWindow();
                    setCursorToClickPosition(e.getX());
                }
            }
        });
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                SwingUtilities.invokeLater(()->{
                    requestFullRepaint();

                });
            }
        });
        addKeyListener(new ScreenKeyAdapter());
    }

    private MaterialFixedTimer blinkerTimer = new MaterialFixedTimer(1000 / BLINKING_RATE) {
        @Override
        public void tick(float deltaMillis) {
            if (!isUnderUse)
                return;
            if (System.currentTimeMillis() - caretBlinkingPauseStartTime > 1000) {
                showCaret = !showCaret;
                repaint(CARET_X, CARET_Y, CARET_WIDTH, CARET_HEIGHT);
            }
        }
    };

    @Override
    public void updateTheme() {
        Color bg;
        if (elevation != null) {
            bg = ThemeColors.getColorByElevation(elevation);
            borderColor = ColorUtils.lighten(bg, 10);
        } else
            bg = ThemeColors.TransparentColor;
        animateBG(bg);
    }
    private void requestFullRepaint() {
        fullRepaintRequired = true;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (fullRepaintRequired) {
            super.paintComponent(g);
            g.setColor(getBackground());
            g.fillRoundRect(0, 0, getWidth(), getHeight(), CornerRadius, CornerRadius);
            final FontMetrics fm = g.getFontMetrics();
            int xMin = pad.getLeft();
            int yMin = pad.getTop();
            int widthAvail = getWidth() - pad.getHorizontal();
            int heightAvail = getHeight() - pad.getVertical();
            g.setColor(borderColor);
            if (isUnderUse)
                g.drawRoundRect(xMin, yMin, widthAvail, heightAvail, CornerRadius, CornerRadius);
            g.clipRect(xMin, yMin, widthAvail, heightAvail);

            drawText(g,widthAvail,fm);
            redrawCaret(g, fm, widthAvail);
            fullRepaintRequired = false;
//            try {
//                Log.info(parser.getAst(lexer));
//            }catch (Exception e){
//                Log.error("Syntax error while parsing: " +e.getMessage());
//            }
        } else {
            drawCaret(g);
        }
    }


    private void drawText(Graphics g2d,int widthAvail, FontMetrics fm) {
        g2d.setColor(ThemeColors.getTextPrimary());
        int tX = widthAvail;
        int tY = (int) (((this.getHeight() - fm.getAscent()) * getAlignmentY()) + fm.getAscent()); //Text y coordinate
        String str = text.toString();
        tX -= fm.stringWidth(str);
        g2d.drawString(str, tX, tY);
    }
    private void drawCaret(Graphics g) {
        if (showCaret) {
            g.setColor(ThemeColors.getAccent());
            g.fillRect(CARET_X, CARET_Y, CARET_WIDTH, CARET_HEIGHT);
        }
    }

    private void redrawCaret(Graphics g2d, FontMetrics fm, int widthAvail) {
        //caret
        if (showCaret) {
            CARET_HEIGHT = getHeight() / 3;
            CARET_Y = (getHeight() - CARET_HEIGHT) / 2;
            CARET_X = widthAvail - fm.stringWidth(text.substring(getCaretPosition()));
            g2d.setColor(ThemeColors.getAccent());
            g2d.fillRect(CARET_X, CARET_Y, CARET_WIDTH, CARET_HEIGHT);
        }
    }

    private void setCursorToClickPosition(int x) {
        caretBlinkingPauseStartTime = System.currentTimeMillis();
        if (text.isEmpty()) {
            showCaret = true;
            requestFullRepaint();
            return;
        }
        int currentX = getWidth();
        FontMetrics fontMetrics = getFontMetrics(getFont());
        if (x < getWidth() - fontMetrics.stringWidth(text.toString()))
            setCaretPosition(0);
        else {
            Log.info("Changing caret position");
            int i;
            for (i = text.length() - 1; i >= 0; i--) {
                char ch = text.charAt(i);
                int charWidth = fontMetrics.charWidth(ch);
                currentX -= charWidth;
                if (x >= currentX) {
                    break;
                }
            }
            Log.info(" caret position set to " + i);
            setCaretPosition(i);
        }
    }

    private void decrementCaretPos() {
        if (caretPosition > 0)
            caretPosition--;
        showCaret = true;
        requestFullRepaint();
    }

    private void incrementCaretPosition() {
        if (caretPosition < text.length()) {
            caretPosition++;
        }
        showCaret = true;
        requestFullRepaint();
    }

    private void setCaretPosition(int index) {
        if (index < text.length() && index >= 0) {
            caretPosition = index;
        }
        caretBlinkingPauseStartTime = System.currentTimeMillis();
        showCaret = true;
        requestFullRepaint();
    }

    private void deleteChar(int index) {
        Log.info("Deleting char at " + index);
        if (index >= 0 && index < text.length()) {
            text.deleteCharAt(index);
            decrementCaretPos();
            requestFullRepaint();
        }
    }

    private void handleBackspace() {
        deleteChar(getCaretPosition() - 1);
    }

    private int getCaretPosition() {
        return caretPosition;
    }

    private void insertTextAtCaretLocation(char character) {
        int offset = getCaretPosition();
        text.insert(offset, character);
        incrementCaretPosition();

    }
    @Override
    protected void animateMouseEnter() {

    }

    @Override
    protected void animateMouseExit() {

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


    public IKeypadHandler requestKeyListener() {
        return this.KeyPadHandler;
    }

    private static class KeyPadHandlerImpl implements IKeypadHandler {

        @Override
        public void handleNumericKeyPress(int value) {

        }

        @Override
        public void handleOperatorKeyPress(OperatorType value) {

        }

        @Override
        public void handleFunctionKeyPress(FunctionType value) {

        }

        @Override
        public void handleSpecialKeyPress(SpecialButton value) {

        }
    }

    private class ScreenKeyAdapter implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            caretBlinkingPauseStartTime = System.currentTimeMillis();
            if (Character.isAlphabetic(e.getKeyChar()) || Character.isDigit(e.getKeyChar()) || OPERATORS_STR.indexOf(e.getKeyChar()) != -1) {
                insertTextAtCaretLocation(e.getKeyChar());
            } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                handleBackspace();
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                decrementCaretPos();
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                incrementCaretPosition();
            }
            else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                try {
                    Log.info(parser.getAst(new Lexer(text.toString())));
                }catch (Exception err){
                    Log.error("Syntax error:" + err);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
