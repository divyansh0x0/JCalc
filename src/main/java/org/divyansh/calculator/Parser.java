package org.divyansh.calculator;

import material.utils.Log;
import org.divyansh.calculator.nodes.*;
import org.divyansh.calculator.tokens.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

//TODO Parsing of term is going on from right to left if the precedence is not explicitly mentioned EG 9/9*3 give 0.33333333 instead of 3
public class Parser {

    private final int precision;

    public Parser(int outputPrecision) {
        this.precision = outputPrecision;
    }

    private boolean isParsingArguments;

    public String evaluate(String exp) {
        Lexer lexer = new Lexer(exp);
//        Log.success("LEXER OUTPUT: " + lexer);
        Node root = parseExpression(lexer);
        Log.info("AST CREATED:\n"+root);
        return stripTrailingZeroes(root.evaluateValue().setScale(precision, RoundingMode.HALF_EVEN).toPlainString());
    }

    private String stripTrailingZeroes(@NotNull String s) {
        return s.contains(".") ? s.replaceAll("0*$", "").replaceAll("\\.$", "") : s;
    }


    private Node parseExpression(Lexer lexer) {
        Node leftTerm = parseTerm(lexer);
        Token token = lexer.seekToken();
        if (leftTerm != null && token != null) {
            if (!token.equals(BracketToken.CLOSE)) {
                token = lexer.nextToken();
                switch (token.getTokenType()) {
                    case OPERATOR -> {
                        OperatorToken op = (OperatorToken) token;
                        switch (op.getOperatorType()) {
                            case MINUS, PLUS -> {
                                Node rightExp = parseExpression(lexer);
                                return new BinaryOperatorNode(leftTerm, op, rightExp);
                            }
                        }
                    }
                    case COMMA-> {
//                        Log.warn("Ending exp");
                        return leftTerm;
                    }
                    default -> throw new UnsupportedOperationException("INVALID TOKEN IN EXPRESSION: " + token);
                }
            }
        }
        return leftTerm;
    }

    private Node parseTerm(Lexer lexer) {

//        Log.info("[][][" + lexer.getCurrToken());
        //        Log.info(leftFactor);
//        Log.warn("next token:" + nextToken);
        Node expression = parseFactor(lexer);
//        Log.info("exp:"+expression);
        while (!isEndOfTerm(lexer.seekToken())) {
            Token nextToken = lexer.nextToken();
//            Log.info("token:" + nextToken);
            if (nextToken.getTokenType().equals(TokenType.OPERATOR)) {
                OperatorToken op = ((OperatorToken) nextToken);
                switch (op.getOperatorType()) {
                    case MULTIPLICATION, DIVISION, REMAINDER -> {
//                        Log.info("op:"+op);
                        expression = new BinaryOperatorNode(expression, op, parseFactor(lexer));
                    }
                    case FACTORIAL ->
                        expression = new UnaryOperatorNode(op,expression);
                    default ->
                            throw new UnsupportedOperationException("INVALID TOKEN IN TERM: " + nextToken + " at index " + lexer.getCurrIndex() + " in " + lexer);
                }
            } else
                throw new UnsupportedOperationException("INVALID TOKEN " + nextToken + " at index " + lexer.getCurrIndex()+ "(AN OPERATOR IS EXPECTED HERE) in " + lexer);
        }
        return expression;
    }

    private boolean isEndOfTerm(Token nextToken) {
        if (nextToken == null)
            return true;
        if(isParsingArguments && nextToken.getTokenType().equals(TokenType.COMMA)) {
//            Log.success("Comma found");
            return true;
        }
        return switch (nextToken.getValue()) {
            case "+", "-", ")" -> true;
            default -> false;
        };
    }

    private @Nullable Node parseFactor(@NotNull Lexer lexer) {
        Token token = lexer.nextToken();
        switch (token.getTokenType()) {
            case NUMBER -> {
                NumberNode n1 = new NumberNode((NumberToken) token);
                Token nextToken = lexer.seekToken();
                if (nextToken != null && nextToken.getTokenType().equals(TokenType.OPERATOR)) {
                    switch (((OperatorToken) nextToken).getOperatorType()) {
                        case EXPONENTIATION -> {
                            OperatorToken op = (OperatorToken) lexer.nextToken();
                            Node exp = parseFactor(lexer);
                            return new BinaryOperatorNode(n1, op, exp);
                        }
                        case FACTORIAL -> {
                            OperatorToken op = (OperatorToken) lexer.nextToken();
                            return new UnaryOperatorNode(op, n1);
                        }
                    }

                }
                return n1;
            }
            case FUNCTION -> {
                lexer.nextToken();
                return new FunctionNode((FunctionToken) token, parseArgs(lexer));
            }
            case BRACKET -> {
                if (token.equals(BracketToken.OPEN)) {
                    Node exp = parseExpression(lexer);
                    lexer.nextToken();
                    return exp;
                }
            }
            case OPERATOR -> {
                OperatorToken op = (OperatorToken) token;
                switch (((OperatorToken) token).getOperatorType()) {
                    case MINUS, PLUS -> {
                        return new UnaryOperatorNode(op, parseFactor(lexer));
                    }
                    default ->
                            throw new UnsupportedOperationException("INVALID UNARY OPERATOR: " + token + " at index " + lexer.getCurrIndex() + " of " + lexer);

                }
            }
            default -> {
                throw new UnsupportedOperationException("INVALID FACTOR TOKEN: " + token + " at index " + lexer.getCurrIndex());
            }
        }
        return null;
    }

    private Node[] parseArgs(Lexer lexer) {
        ArrayList<Node> arr = new ArrayList<>();
        Token token;
        while ((token = lexer.seekToken()) != null && !token.equals(BracketToken.CLOSE)){

            isParsingArguments = true;
            Node exp = parseExpression(lexer);
            arr.add(exp);
        }
        lexer.nextToken(); //skips the close token
        isParsingArguments = false;
        return arr.toArray(new Node[0]);
    }

}
