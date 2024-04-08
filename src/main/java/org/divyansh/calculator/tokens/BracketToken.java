package org.divyansh.calculator.tokens;

public enum BracketToken implements Token {
    OPEN,
    CLOSE;

    @Override
    public String getValue() {
        return this.equals(OPEN) ? "(" : ")";
    }

    @Override
    public TokenType getTokenType() {
        return TokenType.BRACKET;
    }
    @Override
    public String toString() {
        return getValue();
    }


}
