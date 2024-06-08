package org.divyansh.calculator;

import org.divyansh.calculator.tokens.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Lexer {
    protected final ArrayList<Token> tokenArrayList = new ArrayList<>();
    private final Stack<Character> bracketsStack = new Stack<>();
    private int nextIndex = 0;
    private final StringBuffer SB = new StringBuffer();

    public Lexer(@NotNull String exp) {
        char[] charArr = exp.toCharArray();
        tokenArrayList.addAll(parseCharArray(charArr));
    }

    protected ArrayList<Token> parseCharArray(char @NotNull [] arr) {
        ArrayList<Token> arrayList = new ArrayList();
        Token token;
        EvaluationResult evalResult;
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (Character.isDigit(c)) {
                evalResult = evaluateNumber(arr, i);
                i = evalResult.newIndex;
                token = new NumberToken(evalResult.result);
            }
            else if(i+1<arr.length && Character.toLowerCase(c) == 'p' && Character.toLowerCase(arr[i+1]) == 'i'){
                i++;
                token = NumberToken.PI;
            }
            else if (c == '(' || c == '[' || c == '{') {
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
            else if(c==','){
                token = new CommaToken();
            }
            else if ((evalResult = parseFunction(arr,i)) != null) {
                token= new FunctionToken(evalResult.result);
                i = evalResult.newIndex;
            }
            else {
                throw new RuntimeException("Invalid character: " + c+ " at " + i + " in " + Arrays.toString(arr));
            }
            arrayList.add(token);
        }
        if(!bracketsStack.isEmpty())
            throw new RuntimeException(Arrays.toString(arr) + " does not have appropriate closing brackets ");
        return arrayList;
    }
    @Contract("_, _ -> new")
    private @NotNull EvaluationResult evaluateNumber(char @NotNull [] arr, int i) {
        while (i < arr.length) {
            char c = arr[i];
            if (Character.isDigit(c) || c == '.')
                SB.append(c);
            else
                break;
            i++;
        }
        String num = SB.toString();
        SB.delete(0, SB.length());
        return new EvaluationResult(num,i - 1);
    }
    private @Nullable EvaluationResult parseFunction(char @NotNull [] arr, int i) {
        for (; i  < arr.length ;i++) {
            char c = arr[i];
            if(Character.isAlphabetic(c)){
                SB.append(c);
            }
            else if(Character.isDigit(c) && SB.toString().equalsIgnoreCase("log")){
                EvaluationResult r = evaluateNumber(arr,i);
                i = r.newIndex;
                SB.append(r);
            }
            else {
                break;
            }
        }
        String name = SB.toString();
        SB.delete(0,SB.length());
        return name.isEmpty() ? null : new EvaluationResult(name,i-1);
    }

    private boolean isValidClosingBracket(char bracketClose) {
        if(bracketsStack.empty())
            return true;
        char bracketOpen = bracketsStack.pop();
        return bracketOpen == '(' ? bracketClose == ')' : bracketOpen  == '{' ? bracketClose == '}' : bracketOpen == '[' && bracketClose == ']';
    }



    public int getCurrIndex() {
        return nextIndex - 1;
    }
    public int getNextIndex() {
        return nextIndex;
    }


    public Token getAndMoveToNextToken() {
        Token token = null;
        if (nextIndex < tokenArrayList.size()) {
            token = tokenArrayList.get(nextIndex);
            nextIndex++;
        }
        return token;
    }

    public Token getNextToken() {
        return nextIndex < tokenArrayList.size() ? tokenArrayList.get(nextIndex) : null;
    }

    private boolean isOperator(char c) {
        return !OperatorType.getType(String.valueOf(c)).equals(OperatorType.UNKNOWN);
    }


    @Override
    public String toString() {
        return tokenArrayList.toString();
    }
    private class EvaluationResult{
        public String result;
        public int newIndex;

        public EvaluationResult(String  result, int newIndex) {
            this.result = result;
            this.newIndex = newIndex;
        }
        public EvaluationResult set(String result, int newIndex){
            this.result = result;
            this.newIndex = newIndex;
            return this;
        }
    }

    public ArrayList<Token> getTokenArray(){
        return tokenArrayList;
    }
}
