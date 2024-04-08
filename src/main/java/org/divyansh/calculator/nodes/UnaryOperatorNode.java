package org.divyansh.calculator.nodes;

import org.divyansh.calculator.tokens.OperatorToken;

import java.math.BigDecimal;
import java.math.BigInteger;

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
                long val = operand.longValue();
                return BigDecimal.valueOf(getFactorial(val));
            }
            default -> throw  new UnsupportedOperationException("UNSUPPORTED OPERATOR: " + op);
        }
    }

    private long getFactorial(long val) {
        long fact = 1;
        while (val > 0){
            fact *= val;
            val--;
        }
        return fact;
    }

    @Override
    public String toString() {
        return "[(" + op + ") --> "+" (val)]";
    }
}
