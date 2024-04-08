package org.divyansh.calculator.nodes;

import material.utils.Log;
import org.divyansh.calculator.tokens.OperatorToken;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BinaryOperatorNode implements Node{
    private final Node leftOperand;
    private final Node rightOperand;
    private final OperatorToken op;

    public BinaryOperatorNode(Node leftOperand, OperatorToken op,Node rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.op = op;
    }

    @Override
    public String toString() {
        return "[" + leftOperand + "<-- ("+op+") -->" + rightOperand + "]";
    }

    @Override
    public BigDecimal evaluateValue() {
        final BigDecimal leftOperand = this.leftOperand.evaluateValue();
        final BigDecimal rightOperand = this.rightOperand.evaluateValue();
        Log.info("EVALUATING: " + leftOperand+ op + rightOperand);

        if(leftOperand == null)
            throw new NullPointerException("LEFT OPERAND IS NULL FOR BINARY OPERATOR: " + op);
        if(rightOperand == null)
            throw new NullPointerException("RIGHT OPERAND IS NULL FOR BINARY OPERATOR:" + op);
        return switch (op.getOperatorType()){
            case ADDITION -> leftOperand.add(rightOperand);
            case SUBTRACTION -> leftOperand.subtract(rightOperand);
            case DIVISION -> leftOperand.divide(rightOperand,16, RoundingMode.HALF_EVEN);
            case MULTIPLICATION -> leftOperand.multiply(rightOperand);
            case REMAINDER -> leftOperand.remainder(rightOperand);
            case EXPONENTIATION -> leftOperand.pow(Integer.parseInt(rightOperand.toString()));
            default -> throw new UnsupportedOperationException("UNKNOWN OPERATION: " + op);
        };
    }
}
