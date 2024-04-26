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

    public static OperatorType getType(String c) {
        return switch (c) {
            case "+" -> PLUS;
            case "-" -> MINUS;
            case "*" -> MULTIPLICATION;
            case "/" -> DIVISION;
            case "!" -> FACTORIAL;
            case "^" -> EXPONENTIATION;
            case "%" -> REMAINDER;
            default -> UNKNOWN;
        };
    }
    public String getOperatorSymbol(){
        return switch (this){
            case PLUS -> "+";
            case MINUS -> "-";
            case DIVISION -> "/";
            case MULTIPLICATION -> "*";
            case EXPONENTIATION -> "^";
            case REMAINDER -> "%";
            case FACTORIAL -> "!";
            case UNKNOWN -> "UNKNOWN";
        };
    }

}
