package org.divyansh.calculator.tokens;

public enum OperatorType {
    ADDITION,
    SUBTRACTION,
    NEGATION,
    DIVISION,
    MULTIPLICATION,
    EXPONENTIATION,
    REMAINDER,
    FACTORIAL,
    UNKNOWN
    ;

    public static OperatorType getType(char c) {
        return switch (c) {
            case '+' -> ADDITION;
            case '-' -> SUBTRACTION;
            case '*' -> MULTIPLICATION;
            case '/' -> DIVISION;
            case '!' -> FACTORIAL;
            case '^' -> EXPONENTIATION;
            case '%' -> REMAINDER;
            default -> UNKNOWN;
        };
    }

}
