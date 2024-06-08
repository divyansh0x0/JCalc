package org.divyansh.calculator;

import material.utils.Log;
import org.divyansh.calculator.nodes.*;
import org.divyansh.calculator.tokens.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.util.ArrayList;

public class Parser {

    private final int precision;

    public Parser(int outputPrecision) {
        this.precision = outputPrecision;
    }

    private boolean isParsingArguments;

    /**
     * Evaluates the given expression and returns the result as a string.
     *
     * @param exp The expression to evaluate.
     * @return The result of the evaluation as a string.
     */
    public String evaluate(String exp) {
        // Create a lexer and parse the expression and get the root node
        Node root = getAst(new Lexer(exp));

        // Log the created AST
        Log.info("AST CREATED:\n" + root);

        //

        // Evaluate the value of the root node and strip trailing zeroes and return it
        return stripTrailingZeroes(root.evaluateValue().setScale(precision, RoundingMode.HALF_EVEN).toPlainString());
    }

    public Node getAst(Lexer lexer){
        return parseExpression(lexer);
    }
    /**
     * Strips trailing zeroes from a string representation of a decimal number.
     *
     * @param s The string to strip zeroes from.
     * @return The string with trailing zeroes stripped.
     */
    private @NotNull String stripTrailingZeroes(@NotNull String s) {
        // Check if the string contains a decimal point
        if (s.contains(".")) {
            // Remove trailing zeroes and the decimal point if it becomes unnecessary
            return s.replaceAll("0*$", "").replaceAll("\\.$", "");
        } else {
            // Return the string as is if it doesn't contain a decimal point
            return s;
        }
    }


    /**
     * Parses an expression using the given lexer.
     *
     * @param lexer The lexer used for parsing.
     * @return The parsed expression node.
     */
    private @Nullable Node parseExpression(Lexer lexer) {
        // Parse the initial term
        Node leftTerm = parseTerm(lexer);

        // Get the next token
        Token token = lexer.getNextToken();

        if (leftTerm == null || token == null || token.equals(BracketToken.CLOSE)) {
            return leftTerm;
        }
        // Get and move the index of the lexer to next token
        token = lexer.getAndMoveToNextToken();

        // Check if the token is an operator
        if (TokenType.OPERATOR.equals(token.getTokenType())) {
            // Get the operator token
            OperatorToken op = (OperatorToken) token;

            // Check the operator type
            switch (op.getOperatorType()) {
                case MINUS, PLUS -> {
                    // Parse the right expression
                    Node rightExp = parseExpression(lexer);

                    // Create a binary operator node
                    return new BinaryOperatorNode(leftTerm, op, rightExp);
                }
            }
        } else if (TokenType.COMMA.equals(token.getTokenType())) {
            // If the token is a comma, return the left term
            return leftTerm;
        } else {
            // Throw an exception for an invalid token
            throw new UnsupportedOperationException("INVALID TOKEN IN EXPRESSION: " + token);
        }

        // Return the left term if no more parsing is needed
        return null;
    }

    /**
     * Parses a term in the expression using the given lexer.
     *
     * @param lexer the lexer used for parsing
     * @return the parsed expression node
     */
    private Node parseTerm(Lexer lexer) {
        // Initialize the node with the first parsed factor
        Node term = parseFactor(lexer);

        // Loop until the end of the term is reached
        while (!isEndOfTerm(lexer.getNextToken())) {
            // Get the next token
            Token nextToken = lexer.getNextToken();

            // Check if the token is an operator
            if (nextToken.getTokenType().equals(TokenType.OPERATOR)) {
                //Move to the next token
                // Get the operator token
                OperatorToken op = (OperatorToken) lexer.getAndMoveToNextToken();

                // Check the operator type
                switch (op.getOperatorType()) {
                    case MULTIPLICATION, DIVISION, REMAINDER -> {
                        // Create a binary operator node with the expression and the parsed factor
                        term = new BinaryOperatorNode(term, op, parseFactor(lexer));
                    }
                    case FACTORIAL ->
                        // Create a unary operator node with the expression and the operator
                            term = new UnaryOperatorNode(op, term);
                    default ->
                        // Throw an exception for an invalid operator
                            throw new UnsupportedOperationException("INVALID TOKEN IN TERM: " + nextToken + " at index " + lexer.getCurrIndex() + " in " + lexer);
                }
            } else if (TokenType.FUNCTION.equals(lexer.getNextToken().getTokenType()) || BracketToken.OPEN.equals(lexer.getNextToken()) || TokenType.NUMBER.equals(lexer.getNextToken().getTokenType())) {
                //Fallback to implicit multiplication if there is no operator.
                term = new BinaryOperatorNode(term, new OperatorToken(OperatorType.MULTIPLICATION), parseFactor(lexer));
            } else {
                throw new RuntimeException("INVALID SYNTAX IN TERM: " + nextToken + " at index " + lexer.getCurrIndex() + " in " + lexer);
            }
        }

        // Return the parsed term
        return term;
    }

    /**
     * Checks if the given token marks the end of a term in an expression.
     *
     * @param nextToken The next token in the expression.
     * @return True if the token marks the end of a term, false otherwise.
     */
    private boolean isEndOfTerm(Token nextToken) {
        // If there is no next token, the term ends
        if (nextToken == null) {
            return true;
        }

        // If parsing arguments and the next token is a comma, the term ends
        if (isParsingArguments && nextToken.getTokenType().equals(TokenType.COMMA)) {
            return true;
        }

        // otherwise check for an operator that marks the end of term
        return switch (nextToken.getValue()) {
            case "+", "-", ")" -> true; // End of term for these operators
            default -> false; // No end of term for other operators
        };
    }

    /**
     * Parses a factor in the expression using the given lexer.
     *
     * @param lexer the lexer used for parsing
     * @return the parsed expression node
     */
    private @Nullable Node parseFactor(@NotNull Lexer lexer) {
        // Get the next token
        Token token = lexer.getAndMoveToNextToken();
        if (token == null)
            throw new RuntimeException("NO TOKENS FOUND IN " + lexer + " AT " + lexer.getCurrIndex());
        // Check the token type
        switch (token.getTokenType()) {
            case NUMBER -> {
                // Create a NumberNode with the token
                NumberNode n1 = new NumberNode((NumberToken) token);

                // Check if the next token is an operator
                Token nextToken = lexer.getNextToken();
                if (nextToken != null && nextToken.getTokenType().equals(TokenType.OPERATOR)) {
                    // Get the operator token
                    OperatorToken op = (OperatorToken) nextToken;

                    // Check the operator type
                    switch (op.getOperatorType()) {
                        case EXPONENTIATION -> {
                            // Get the exponentiation operator
                            op = (OperatorToken) lexer.getAndMoveToNextToken();

                            // Parse the exponent
                            Node exp = parseFactor(lexer);

                            // Create a BinaryOperatorNode with the base and exponent
                            return new BinaryOperatorNode(n1, op, exp);
                        }
                        case FACTORIAL -> {
                            // Get the factorial operator
                            op = (OperatorToken) lexer.getAndMoveToNextToken();

                            // Create a UnaryOperatorNode with the base and operator
                            return new UnaryOperatorNode(op, n1);
                        }
                    }

                }

                // Return the NumberNode
                return n1;
            }
            case FUNCTION -> {
                // Get the function token
                FunctionToken functionToken = (FunctionToken) token;

                // Parse the arguments
                Node[] args = parseArgs(lexer);

                // Create a FunctionNode with the function token and arguments
                return new FunctionNode(functionToken, args);
            }
            case BRACKET -> {
                // Check if the token is an open bracket
                if (token.equals(BracketToken.OPEN)) {
                    // Parse the expression inside the brackets
                    Node expression = parseExpression(lexer);

                    // Get the closing bracket
                    lexer.getAndMoveToNextToken();

                    // Return the parsed expression
                    return expression;
                }
            }
            case OPERATOR -> {
                // Get the operator token
                OperatorToken op = (OperatorToken) token;

                // Check the operator type
                switch (op.getOperatorType()) {
                    case MINUS, PLUS -> {
                        // Create a UnaryOperatorNode with the operator and the parsed factor
                        return new UnaryOperatorNode(op, parseFactor(lexer));
                    }
                    default ->
                            throw new UnsupportedOperationException("INVALID UNARY OPERATOR: " + token + " at index " + lexer.getCurrIndex() + " of " + lexer);

                }
            }
            default -> {
                // Throw an exception for an invalid factor token
                throw new UnsupportedOperationException("INVALID FACTOR TOKEN: " + token + " at index " + lexer.getCurrIndex());
            }
        }

        // Return null if no valid factor is found
        return null;
    }

    /**
     * Parses the arguments of a function.
     *
     * @param lexer The lexer used to parse the arguments.
     * @return An array of nodes representing the arguments.
     * @throws UnsupportedOperationException If no arguments are found.
     */
    private Node @NotNull [] parseArgs(@NotNull Lexer lexer) {
        // Get the next nextToken
        Token nextToken = lexer.getNextToken();

        if (nextToken == null) {
            // Throw an exception if no arguments are found
            throw new UnsupportedOperationException("NO ARGS FOUND AT " + lexer.getCurrIndex() + " IN " + lexer);
        }

        // Check if the nextToken is not an open bracket
        if (!nextToken.equals(BracketToken.OPEN)) {
            // If nextToken is not an open bracket then assume it's a single argument function and the argument is a single factor
            return new Node[]{parseFactor(lexer)};
        } else {
            // Skip the open bracket
            lexer.getAndMoveToNextToken();
        }

        // Create a list to store the arguments
        ArrayList<Node> args = new ArrayList<>();

        // Loop until the end of the arguments is reached
        while ((nextToken = lexer.getNextToken()) != null && !nextToken.equals(BracketToken.CLOSE)) {
            // Set the flag to indicate that we are parsing arguments
            isParsingArguments = true;

            // Parse the expression
            Node exp = parseExpression(lexer);

            // Add the expression to the list of arguments
            args.add(exp);
        }

        // Skip the close bracket
        lexer.getAndMoveToNextToken();

        // Reset the flag for argument parsing
        isParsingArguments = false;

        // Return the array of arguments
        return args.toArray(new Node[0]);
    }
}
