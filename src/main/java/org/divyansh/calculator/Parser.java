package org.divyansh.calculator;

import material.utils.Log;
import org.divyansh.calculator.nodes.BinaryOperatorNode;
import org.divyansh.calculator.nodes.Node;
import org.divyansh.calculator.nodes.NumberNode;
import org.divyansh.calculator.tokens.*;

import java.math.BigDecimal;

//TODO Parsing of term is going on from right to left if the precedence is not explicitly mentioned EG 9/9*3 give 0.33333333 instead of 3
public class Parser {
    public static String evaluate(String exp) {
        Lexer lexer = new Lexer(exp);
        Log.info("warn:" + lexer);
        Node root = parseExpression(lexer);
        Log.info("ROOT:" + root);
        return stripTrailingZeroes(root.evaluateValue().toString());
    }

    private static String stripTrailingZeroes(String s) {
        return  s.contains(".") ? s.replaceAll("0*$","").replaceAll("\\.$","") : s;
    }


    private static Node parseExpression(Lexer lexer) {
        Node leftTerm = parseTerm(lexer);
//        Log.info("LEFT TERM: " + leftTerm);
        Token token = lexer.nextToken();
//        Log.warn("token: (Should be an operator) " + token);
        if (leftTerm != null && token != null) {
            if (token.getTokenType().equals(TokenType.OPERATOR)) {
                OperatorToken op = (OperatorToken) token;
//                Log.info("op:" +op);
                switch (op.getOperatorType()) {
                    case SUBTRACTION, ADDITION -> {
                        Node rightExp = parseExpression(lexer);
//                        Log.info("RIGHT TERM: " + rightExp);
                        return new BinaryOperatorNode(leftTerm, op, rightExp);
                    }
                }
            }
//            else
//                throw new UnsupportedOperationException("INVALID TOKEN IN EXPRESSION: " + token);
        }
        return leftTerm;
    }

    private static Node parseTerm(Lexer lexer) {
//        Log.info("[][][" + lexer.getCurrToken());
        Node factor = parseFactor(lexer);
//        Log.info(factor);
        Token nextToken = lexer.seekToken();;
//        Log.warn("next token:" + nextToken);
        if (!isEndOfTerm(nextToken)) {
            nextToken = lexer.nextToken();
            if (nextToken.getTokenType().equals(TokenType.OPERATOR)) {
                OperatorToken op = ((OperatorToken) nextToken);
                switch (op.getOperatorType()) {
                    case MULTIPLICATION, DIVISION, REMAINDER -> {
//                        Log.info("op:"+op);
                        if(BracketToken.OPEN.equals(lexer.seekToken()))
                            return new BinaryOperatorNode(factor, op, parseFactor(lexer));
                        else
                            return new BinaryOperatorNode(factor,op,parseTerm(lexer));

                    }
                    default -> throw new UnsupportedOperationException("INVALID TOKEN IN TERM: " + nextToken + " at index " + lexer.getCurrIndex());
                }
            }
            throw new UnsupportedOperationException("INVALID TOKEN IN TERM: " + nextToken + " at index " + lexer.getCurrIndex());
        }
        return factor;
    }

    private static boolean isEndOfTerm(Token nextToken) {
        if (nextToken == null)
            return true;
        return switch (nextToken.getValue()) {
            case "+", "-", ")" -> true;
            default -> false;
        };
    }

    private static Node parseFactor(Lexer lexer) {
        Token token = lexer.nextToken();
//        Log.info("FACT:" + token);
        switch (token.getTokenType()) {
            case NUMBER -> {
                NumberNode n1 = new NumberNode((NumberToken) token);
                if (lexer.seekToken() != null && lexer.seekToken().getValue().equals("^")) {
                    OperatorToken op = (OperatorToken) lexer.nextToken();
                    Token expToken = lexer.nextToken();
                    Node exp = null;
                    if (expToken.getTokenType().equals(TokenType.NUMBER))
                        exp = new NumberNode((NumberToken) expToken);
                    else if(expToken.getValue().equals("("))
                        exp = parseExpression(lexer);
                    if(exp != null)
                        return new BinaryOperatorNode(n1, op, exp);
                    else
                        throw new UnsupportedOperationException("EXPONENTIATION IS MISSING EXPONENT");
                }
                return n1;
            }
            case BRACKET -> {
                if (token.getValue().equals("(")) {
                    return parseExpression(lexer);
                }
            }
            default -> {
                throw new UnsupportedOperationException("INVALID FACTOR TOKEN: " + token + " at index " + lexer.getCurrIndex());
            }
        }
        return null;
    }

}
