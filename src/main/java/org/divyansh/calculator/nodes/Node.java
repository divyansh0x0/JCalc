package org.divyansh.calculator.nodes;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public abstract class Node {
    protected static final String CONNECTOR = "-";
    protected static final String REPEATER = "\t|";
    protected static final MathContext MATH_CONTEXT = new MathContext(128, RoundingMode.HALF_EVEN);

    protected int level=0;
    public abstract BigDecimal evaluateValue();
//    protected abstract String getFormattedRepresentation(int level);

}
