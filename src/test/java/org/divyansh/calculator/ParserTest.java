package org.divyansh.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test//

    void evaluate() {
        assertEquals("3", Parser.evaluate("9/9*3"));
        assertEquals("27", Parser.evaluate("9*9/3"));
        assertEquals("12", Parser.evaluate("3!*(6/3*[3/3/{3/3}])"));
        assertEquals("-12", Parser.evaluate("-(3!*(6/3*[3/3/{3/3}]))"));
        assertEquals("24", Parser.evaluate("-(4!/2*(6/-3*[3/3/{3/3}]))"));

        assertEquals("148", Parser.evaluate(" 100 + 8 * 2 *3^3/3^2 + 200/3 * 3/2 - 100"));
        assertEquals("48", Parser.evaluate("100 + (8 * 2) *((3^3)/3^2) - 100"));
        assertEquals("8", Parser.evaluate("2*2*2"));
        assertEquals("200", Parser.evaluate("100 + 100"));

        assertEquals("0", Parser.evaluate("100 - 100"));
        assertEquals("106.75", Parser.evaluate("3 * 9/2^2 + 100"));
        assertEquals("6", Parser.evaluate("3*2"));
    }
}