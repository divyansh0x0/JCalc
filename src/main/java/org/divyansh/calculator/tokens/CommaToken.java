package org.divyansh.calculator.tokens;

public class CommaToken implements Token {
    @Override
    public String getValue() {
        return ",";
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.COMMA;
    }

    @Override
    public String toString() {
        return "(comma)";
    }
}
