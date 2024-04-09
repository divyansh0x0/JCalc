package org.divyansh.calculator;

import material.utils.Log;
import org.divyansh.calculator.nodes.BinaryOperatorNode;
import org.divyansh.calculator.nodes.Node;
import org.divyansh.calculator.nodes.NumberNode;
import org.divyansh.calculator.nodes.UnaryOperatorNode;
import org.divyansh.calculator.tokens.*;

//TODO Parsing of term is going on from right to left if the precedence is not explicitly mentioned EG 9/9*3 give 0.33333333 instead of 3
public class Parser {
    public static String evaluate(String exp) {
        Lexer lexer = new Lexer(exp);
        Node root = parseExpression(lexer);
        Log.info("AST:" + root);
        return stripTrailingZeroes(root.evaluateValue().toPlainString());
    }

    private static String stripTrailingZeroes(String s) {
        return s.contains(".") ? s.replaceAll("0*$", "").replaceAll("\\.$", "") : s;
    }


    private static Node parseExpression(Lexer lexer) {
        Node leftTerm = parseTerm(lexer);
        Token token = lexer.nextToken();
        if (leftTerm != null && token != null) {
            if (!token.equals(BracketToken.CLOSE)) {
                if (token.getTokenType().equals(TokenType.OPERATOR)) {
                    OperatorToken op = (OperatorToken) token;
                    switch (op.getOperatorType()) {
                        case MINUS, PLUS -> {
                            Node rightExp = parseExpression(lexer);
                            return new BinaryOperatorNode(leftTerm, op, rightExp);
                        }
                    }
                } else
                    throw new UnsupportedOperationException("INVALID TOKEN IN EXPRESSION: " + token);
            }
        }
        return leftTerm;
    }

    private static Node parseTerm(Lexer lexer) {
//        Log.info("[][][" + lexer.getCurrToken());
        //        Log.info(leftFactor);
//        Log.warn("next token:" + nextToken);
        Node term = parseFactor(lexer);
        while (!isEndOfTerm(lexer.seekToken())) {
            Token nextToken = lexer.nextToken();
            if (nextToken.getTokenType().equals(TokenType.OPERATOR)) {
                OperatorToken op = ((OperatorToken) nextToken);
                switch (op.getOperatorType()) {
                    case MULTIPLICATION, DIVISION, REMAINDER -> {
//                        Log.info("op:"+op);
                        term = new BinaryOperatorNode(term, op, parseFactor(lexer));
                    }
                    default ->
                            throw new UnsupportedOperationException("INVALID TOKEN IN TERM: " + nextToken + " at index " + lexer.getCurrIndex());
                }
            } else
                throw new UnsupportedOperationException("INVALID TOKEN IN TERM: " + nextToken + " at index " + lexer.getCurrIndex());
        }
        return term;
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
            case BRACKET -> {
                if (token.getValue().equals("(")) {
                    return parseExpression(lexer);
                }
            }
            case OPERATOR -> {
                OperatorToken op = (OperatorToken) token;
                switch (((OperatorToken) token).getOperatorType()) {
                    case MINUS, PLUS -> {
                        return new UnaryOperatorNode(op, parseFactor(lexer));
                    }
                    default ->
                            throw new UnsupportedOperationException("INVALID UNARY OPERATOR: " + token + " at index " + lexer.getCurrIndex());

                }
            }
            default -> {
                throw new UnsupportedOperationException("INVALID FACTOR TOKEN: " + token + " at index " + lexer.getCurrIndex());
            }
        }
        return null;
    }

}
