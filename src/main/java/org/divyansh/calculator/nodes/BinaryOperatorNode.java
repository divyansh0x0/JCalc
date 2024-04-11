package org.divyansh.calculator.nodes;

import org.divyansh.calculator.BigDecimalMath;
import org.divyansh.calculator.tokens.OperatorToken;

import java.math.BigDecimal;

import static org.divyansh.calculator.BigDecimalMath.MATH_CONTEXT;

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

//        Log.info("EVALUATING: " + leftOperand+ op + rightOperand);

        if(leftOperand == null)
            throw new NullPointerException("LEFT OPERAND IS NULL FOR BINARY OPERATOR: " + op);
        if(rightOperand == null)
            throw new NullPointerException("RIGHT OPERAND IS NULL FOR BINARY OPERATOR:" + op);
        return switch (op.getOperatorType()){
            case PLUS -> leftOperand.add(rightOperand);
            case MINUS -> leftOperand.subtract(rightOperand);
            case DIVISION -> leftOperand.divide(rightOperand, BigDecimalMath.MATH_CONTEXT);
            case MULTIPLICATION -> leftOperand.multiply(rightOperand);
            case REMAINDER -> leftOperand.remainder(rightOperand);
            case EXPONENTIATION -> BigDecimalMath.pow(leftOperand,rightOperand);
            default -> throw new UnsupportedOperationException("UNKNOWN OPERATION: " + op);
        };
    }
}
