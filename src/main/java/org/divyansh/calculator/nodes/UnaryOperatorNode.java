package org.divyansh.calculator.nodes;

import org.divyansh.calculator.BigDecimalMath;
import org.divyansh.calculator.tokens.OperatorToken;

import java.math.BigDecimal;

public class UnaryOperatorNode implements Node {
    private final Node val;
    private final OperatorToken op;
    public UnaryOperatorNode(OperatorToken op, Node val) {
        this.val = val;
        this.op = op;
    }

    @Override
    public BigDecimal evaluateValue() {
        BigDecimal operand = val.evaluateValue();
        if(operand== null)
            throw new NullPointerException("OPERAND IS NULL FOR UNARY OPERATOR: " + op);
        switch (op.getOperatorType()){
            case FACTORIAL -> {
                return BigDecimalMath.getFactorial(operand);
            }
            case MINUS -> {
                return operand.negate();
            }
            case PLUS -> {
                return operand;
            }
            default -> throw  new UnsupportedOperationException("UNSUPPORTED OPERATOR: " + op);
        }
    }



    @Override
    public String toString() {
        return "[(" + op + ") --> "+" ("+val+")]";
    }
}
