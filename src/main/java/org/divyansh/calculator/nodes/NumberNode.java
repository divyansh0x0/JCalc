package org.divyansh.calculator.nodes;

import org.divyansh.calculator.tokens.NumberToken;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberNode extends Node {
    private NumberToken numberToken;
    public NumberNode(NumberToken numberToken){
        this.numberToken = numberToken;
    }
    @Override
    public BigDecimal evaluateValue() {
        return numberToken.getBigIntegerValue();
    }
    public NumberToken getNumberToken(){
        return numberToken;
    }
    public double getValueInDouble(){
        return numberToken.getDoubleValue();
    }

    @Override
    public String toString() {
        return REPEATER.repeat(level) + CONNECTOR +numberToken;
    }
}
