package org.divyansh.calculator.nodes;

import org.divyansh.calculator.BigDecimalMath;
import org.divyansh.calculator.tokens.FunctionToken;

import java.math.BigDecimal;
import java.util.Arrays;

public class FunctionNode implements Node {

    private final FunctionToken token;
    private final Node[] args;

    public FunctionNode(FunctionToken token, Node[] args) {
        this.token = token;
        this.args = args;
    }

    @Override
    public BigDecimal evaluateValue() {
        if (!isValidArgs()) throwError();

        final BigDecimal arg0 = args[0].evaluateValue();
        BigDecimal arg1 = null;
        if (args.length > 1)
            arg1 = args[1].evaluateValue();

        return switch (token.getFunctionType()) {
            case ABSOLUTE_VALUE -> arg0.abs();
            case SIN -> BigDecimalMath.sin(arg0);
            case COS -> BigDecimalMath.cos(arg0);

            case TAN -> {
                BigDecimal cos = BigDecimalMath.cos(arg0);
                if (cos.equals(BigDecimal.ZERO))
                    throw new ArithmeticException("TAN is undefined " + arg0);
                yield BigDecimalMath.sin(arg0).divide(cos, BigDecimalMath.MATH_CONTEXT);
            }

            case COSEC -> {
                BigDecimal sin = BigDecimalMath.sin(arg0);
                if (sin.compareTo(BigDecimal.ZERO) == 0)
                    throw new ArithmeticException("COSEC is undefined for " + arg0);
                yield BigDecimal.ONE.divide(sin, BigDecimalMath.MATH_CONTEXT);
            }
            case SEC -> {

                BigDecimal cos = BigDecimalMath.cos(arg0);
                if (cos.compareTo(BigDecimal.ZERO) == 0)
                    throw new ArithmeticException("SEC is undefined for " + arg0);
                yield BigDecimal.ONE.divide(cos, BigDecimalMath.MATH_CONTEXT);
            }
            case COT -> {
                BigDecimal sin = BigDecimalMath.cos(arg0);
                if (sin.compareTo(BigDecimal.ZERO) == 0)
                    throw new ArithmeticException("TAN is undefined " + arg0);
                yield BigDecimalMath.cos(arg0).divide(sin, BigDecimalMath.MATH_CONTEXT);
            }
            case LOG -> {
                if (arg0.compareTo(BigDecimal.ZERO) > 0 && arg0.compareTo(BigDecimal.ONE) != 0) {//arg0 ie base of log cannot be negative or 1
                    if (arg1 != null && arg1.compareTo(BigDecimal.ZERO) > 0) { //arg1 i.e argument of log must be greater than 0
                        yield BigDecimalMath.lnNewton(arg0,arg1);
                    } else {
                        throw new ArithmeticException("Argument of log must be positive or zero but found " + arg1);
                    }
                } else {
                    throw new ArithmeticException("Base of log cannot be negative but found " + arg0);
                }
            }
            case SIGNUM -> BigDecimalMath.signum(arg0);
            case CEIL -> BigDecimalMath.ceil(arg0);
            case FLOOR -> BigDecimalMath.floor(arg0);
            case POWER -> BigDecimalMath.pow(arg0, arg1);
            case ROUND -> arg0.round(BigDecimalMath.MATH_CONTEXT);
            default -> throw new RuntimeException("Unknown function: " + token);
        };

    }

    private double throwError() {
        String s = switch (token.getFunctionType()) {
            case ABSOLUTE_VALUE, SIN, COS, TAN, SEC, COSEC, COT, SIGNUM, CEIL, FLOOR, ROUND -> "exactly 1";
            case LOG, POWER -> "exactly 2";
            default -> "UNKNOWN NUMBER OF";
        };
        String str = "INVALID NUMBER OF ARGUMENTS GIVEN TO " + token.getTokenType() + "! " + token + " takes " + s + " numeric argument but " + args.length + " args were given";
        throw new RuntimeException(str);
    }

    private boolean isValidArgs() {
        return switch (token.getFunctionType()) {
            case ABSOLUTE_VALUE, SIN, COS, TAN, SEC, COSEC, COT, SIGNUM, CEIL, FLOOR, ROUND -> args.length == 1;
            case LOG, POWER -> args.length == 2;
            case UNKNOWN -> throw new RuntimeException("UNKNOWN FUNCTION: " + token);
        };
    }

    @Override
    public String toString() {
        return token.getFunctionType() + Arrays.toString(args);

    }
}
