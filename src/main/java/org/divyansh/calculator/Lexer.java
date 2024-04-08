package org.divyansh.calculator;

import org.divyansh.calculator.tokens.*;

import java.util.ArrayList;

public class Lexer {
    private final ArrayList<Token> tokenArrayList = new ArrayList<>();
    private int nextIndex = 0;
    private final StringBuffer SB = new StringBuffer();

    public Lexer(String exp) {
        exp = exp.replaceAll(" ", "");
        char[] charArr = exp.toCharArray();
        parseCharArray(charArr);
    }

    private void parseCharArray(char[] arr) {
        Token token = null;
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (Character.isDigit(c)) {
                String num = evaluateNumber(arr, i);
                i += num.length() - 1;
                token = new NumberToken(num);
            } else if (c == '(') {
                token = BracketToken.OPEN;
            } else if (c == ')') {
                token = BracketToken.CLOSE;
            } else if (isOperator(c)) {
                token = new OperatorToken(c);
            }
            if (token != null)
                tokenArrayList.add(token);
        }
    }

    private String evaluateNumber(char[] arr, int index) {
        while (index < arr.length) {
            char c = arr[index];
            if (Character.isDigit(c) || c == '.')
                SB.append(c);
            else
                break;
            index++;
        }
        String num = SB.toString();
        SB.delete(0, SB.length());
        return num;
    }

    public int getCurrIndex() {
        return nextIndex - 1;
    }
    public int getNextIndex() {
        return nextIndex;
    }


    public Token nextToken() {
        Token token = null;
        if (nextIndex < tokenArrayList.size()) {
            token = tokenArrayList.get(nextIndex);
            nextIndex++;
        }
        return token;
    }

    public Token seekToken() {
        return nextIndex < tokenArrayList.size() ? tokenArrayList.get(nextIndex) : null;
    }

    private boolean isOperator(char c) {
        return !OperatorType.getType(c).equals(OperatorType.UNKNOWN);
    }


    @Override
    public String toString() {
        return tokenArrayList.toString();
    }
}
