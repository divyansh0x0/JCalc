package org.divyansh.calculator;

import material.utils.Log;
import org.divyansh.calculator.tokens.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Lexer {
    private final ArrayList<Token> tokenArrayList = new ArrayList<>();
    private final Stack<Character> bracketsStack = new Stack<>();
    private int nextIndex = 0;
    private final StringBuffer SB = new StringBuffer();

    public Lexer(String exp) {
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
            } else if (c == '(' || c == '[' || c == '{') {
                bracketsStack.push(c);
                token = BracketToken.OPEN;
            } else if (c == ')' || c == ']' || c == '}') {
                if(!isValidClosingBracket(c))
                    throw new RuntimeException("Invalid closing bracket " + c+ " at " + i + " in " + Arrays.toString(arr));
                token = BracketToken.CLOSE;
            } else if (isOperator(c)) {
                token = new OperatorToken(c);
            } else if (Character.isWhitespace(c)) {
                continue;
            }
            else {
                throw new RuntimeException("Invalid character: " + c+ " at " + i + " in " + Arrays.toString(arr));
            }
            tokenArrayList.add(token);
        }
        if(!bracketsStack.isEmpty())
            throw new RuntimeException(Arrays.toString(arr) + " does not have appropriate closing brackets ");
    }

    private boolean isValidClosingBracket(char bracketClose) {
        if(bracketsStack.empty())
            return true;
        char bracketOpen = bracketsStack.pop();
        return bracketOpen == '(' ? bracketClose == ')' : bracketOpen  == '{' ? bracketClose == '}' : bracketOpen == '[' && bracketClose == ']';
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
