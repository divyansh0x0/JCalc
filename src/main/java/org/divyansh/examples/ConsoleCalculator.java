package org.divyansh.examples;

import org.divyansh.calculator.Parser;

import java.util.Scanner;

public class ConsoleCalculator {
    // ANSI escape codes for text formatting
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String ITALIC = "\u001B[3m";
    public static final String UNDERLINE = "\u001B[4m";
    public static final String BLINK = "\u001B[5m";

    // ANSI color codes
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    // Background color codes
    public static final String BG_BLACK = "\u001B[40m";
    public static final String BG_RED = "\u001B[41m";
    public static final String BG_GREEN = "\u001B[42m";
    public static final String BG_YELLOW = "\u001B[43m";
    public static final String BG_BLUE = "\u001B[44m";
    public static final String BG_PURPLE = "\u001B[45m";
    public static final String BG_CYAN = "\u001B[46m";
    public static final String BG_WHITE = "\u001B[47m";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parser parser = new Parser(6);
        String title = """
                     | |/ ___|  / \\  | |   / ___|
                  _  | | |     / _ \\ | |  | |   \s
                 | |_| | |___ / ___ \\| |__| |___\s
                  \\___/ \\____/_/   \\_\\_____\\____|
                """;
        System.out.println(title);

        System.out.println("Welcome to JCalc!");
        System.out.println("Write an expression and press enter to compute it OR write exit to quit");
        while (true) {
            System.out.print(BOLD + CYAN + "> " + RESET);
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println(RED + "Exiting JCalc. Goodbye!" + RESET);
                break;
            }

            try {
                if(input.isBlank()){
                    System.out.println("Write a math expression!!");
                }
                System.out.println("Solving " + input);
                String result = parser.evaluate(input);
                System.out.println(GREEN + "Result: " + BOLD + result + RESET);
            } catch (RuntimeException e) {
                System.out.println(RED + "Error: " + e.getMessage() + RESET);
            }
        }

        scanner.close();
    }
}