package org.divyansh.calculator.nodes;

import ch.obermuhlner.math.big.BigDecimalMath;
import material.utils.Log;
import org.divyansh.Main;
import org.divyansh.calculator.BigDecimalMath2;
import org.divyansh.calculator.tokens.OperatorToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.MathContext;

public class BinaryOperatorNode extends Node {


    private final Node leftOperand;
    private final Node rightOperand;
    private final OperatorToken op;

    public BinaryOperatorNode(@NotNull Node leftOperand,@NotNull OperatorToken op ,@NotNull Node rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.op = op;
    }


    @Override
    public BigDecimal evaluateValue() {
            final BigDecimal leftOperand = this.leftOperand.evaluateValue();
            final BigDecimal rightOperand = this.rightOperand.evaluateValue();

            MathContext mc = Main.MATH_CONTEXT;
            if (leftOperand == null)
                throw new NullPointerException("LEFT OPERAND IS NULL FOR BINARY OPERATOR: " + op);
            if (rightOperand == null)
                throw new NullPointerException("RIGHT OPERAND IS NULL FOR BINARY OPERATOR:" + op);
            return switch (op.getOperatorType()) {
                case PLUS -> leftOperand.add(rightOperand);
                case MINUS -> leftOperand.subtract(rightOperand);
                case DIVISION -> leftOperand.divide(rightOperand, mc);
                case MULTIPLICATION -> leftOperand.multiply(rightOperand);
                case REMAINDER -> leftOperand.remainder(rightOperand);
                case EXPONENTIATION -> BigDecimalMath.pow(leftOperand, rightOperand, mc);
                default -> throw new UnsupportedOperationException("UNKNOWN OPERATION: " + op);
            };
    }


    @Override
    public String toString() {
        try {
            leftOperand.level = this.level + 1;
            rightOperand.level = this.level + 1;
            return ("""
                    %s%s%s[%s]
                    %s
                    %s""").formatted(REPEATER.repeat(level), CONNECTOR, op.getOperatorType(), op.getValue(), leftOperand, rightOperand);
        } catch (StackOverflowError e) {
            Log.error("STACK OVERFLOW OCCURRED AT DEPTH: " + this.level + " WITH OPERATOR: " + op);
            return "--STACK OVERFLOW--";
        }
    }
}
