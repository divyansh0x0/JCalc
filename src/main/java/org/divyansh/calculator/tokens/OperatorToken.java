package org.divyansh.calculator.tokens;

import java.util.Objects;

public class OperatorToken implements Token {
    private final String val;
    private final OperatorType type;

    public OperatorToken(char c) {
        type = OperatorType.getType(c);
        val = String.valueOf(c);
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
