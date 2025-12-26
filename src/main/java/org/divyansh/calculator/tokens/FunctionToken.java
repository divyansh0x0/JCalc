package org.divyansh.calculator.tokens;

import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class FunctionToken implements Token {
    private final String name;
    private FunctionType type;
    private String base; //base is used in functions like log
    private static final HashMap<FunctionType, FunctionToken> cache = new HashMap<>();

    public static FunctionToken create(FunctionType functionType){
        FunctionToken token = cache.get(functionType);
        if(token == null) {
            token = new FunctionToken(functionType.name());
            cache.put(functionType, token);
        }
        return token;
    }
    public FunctionToken(String name) {
        this.name = name.toLowerCase(Locale.ENGLISH);
        if (name.startsWith("log")) {
            char[] arr = new char[name.length() - 3];
            name.getChars(3, name.length(), arr, 0);
            base = String.copyValueOf(arr);
            type = FunctionType.LOG;
        } else {
            type = switch (name) {
                case "sin" -> FunctionType.SIN;
                case "cos" -> FunctionType.COS;
                case "tan" -> FunctionType.TAN;
                case "sec" -> FunctionType.SEC;
                case "cosec" -> FunctionType.COSEC;
                case "cot" -> FunctionType.COT;
                case "floor" -> FunctionType.FLOOR;
                case "ceil" -> FunctionType.CEIL;
                case "abs" -> FunctionType.ABSOLUTE_VALUE;
                case "sig", "signum" -> FunctionType.SIGNUM;
                case "round" -> FunctionType.ROUND;
                case "power", "pow" -> FunctionType.POWER;
                case "sqrt","square_root"->FunctionType.SQRT;
                default -> FunctionType.UNKNOWN;
            };
        }
    }

    @Override
    public String getValue() {
        return name;
    }

    public FunctionType getFunctionType() {
        return type;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.FUNCTION;
    }

    @Override
    public String toString() {
        return name + (base == null ? "(...)" : "_" + base);
    }
}
