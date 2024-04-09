package org.divyansh.calculator.tokens;

public enum OperatorType {
    PLUS,
    MINUS,
    DIVISION,
    MULTIPLICATION,
    EXPONENTIATION,
    REMAINDER,
    FACTORIAL,
    UNKNOWN
    ;

    public static OperatorType getType(char c) {
        return switch (c) {
            case '+' -> PLUS;
            case '-' -> MINUS;
            case '*' -> MULTIPLICATION;
            case '/' -> DIVISION;
            case '!' -> FACTORIAL;
            case '^' -> EXPONENTIATION;
            case '%' -> REMAINDER;
            default -> UNKNOWN;
        };
    }

}
