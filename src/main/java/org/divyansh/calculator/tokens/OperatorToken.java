package org.divyansh.calculator.tokens;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

public class OperatorToken implements Token {
    private final String val;
    private final OperatorType type;
    private static final HashMap<OperatorType, OperatorToken> cache = new HashMap<>();

    public OperatorToken(OperatorType type){
        val = String.valueOf(type.getOperatorSymbol());
        this.type = type;
    }
    public OperatorToken(String c) {
        this(OperatorType.getType(c));
    }
    public OperatorToken(char c){
        this(String.valueOf(c));
    }


    public static @NotNull OperatorToken create(OperatorType operatorType){
        OperatorToken token = cache.get(operatorType);
        if(token == null) {
            token = new OperatorToken(operatorType);
            cache.put(operatorType, token);
        }
        return token;
    }

    public OperatorType getOperatorType() {
        return type;
    }

    @Override
    public String getValue() {
        return val;
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.OPERATOR;
    }
    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperatorToken that)) return false;
        return Objects.equals(val, that.val) && type == that.type;
    }
}
