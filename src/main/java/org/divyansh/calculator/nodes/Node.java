package org.divyansh.calculator.nodes;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class Node {
    protected static final String CONNECTOR = "-";
    protected static final String REPEATER = "\t|";
    protected int level=0;
    public abstract BigDecimal evaluateValue();
//    protected abstract String getFormattedRepresentation(int level);

}
