package org.divyansh.calculator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    Parser parser = new Parser(6);

    @Test
    void testBrackets(){
        assertEquals("6", parser.evaluate("3*(6/3*[3/3/{3/3}])"));
        assertEquals("-2", parser.evaluate("2-(2*2)"));
        assertEquals("24", parser.evaluate("-(-4!/-2*(6/-3*[3/3/{3/3}]))"));
        assertEquals("8", parser.evaluate("([-3*(6-7)]! + 2)"));
    }
    @Test
    void testBinaryOperators(){
        assertEquals("0", parser.evaluate("100 - 100"));
        assertEquals("6", parser.evaluate("3*2"));
        assertEquals("142", parser.evaluate("100 + 8 + 2  + 3^3 + 300/3 + 10/2 - 100"));

    }
    @Test
    void testUnaryOperators(){
        assertEquals("120", parser.evaluate("(3+4-2)!"));
        assertEquals("24", parser.evaluate("4!"));
        assertEquals("0", parser.evaluate("+(-100+100)"));
        assertEquals("200", parser.evaluate("+(100--100)"));
    }
    @Test
    void testAllOperators(){
        assertEquals("12", parser.evaluate("3!*(6/3*[3/3/{3/3}])"));
        assertEquals("-12", parser.evaluate("-(3!*(6/3*[3/3/{3/3}]))"));
        assertEquals("24", parser.evaluate("-(-4!/-2*(6/-3*[3/3/{3/3}]))"));
        assertEquals("24", parser.evaluate("-(-4!/-2*(6/-3*[3/3/{3/3}]))"));

    }

    @Test
    void testOutputFormatting(){
        assertEquals("106.75", parser.evaluate("3 * 9/2^2 + 100"));

    }
    @Test
    void testNumberParsing(){
        assertEquals("4800", parser.evaluate("4.8000 * 10^3"));
        assertEquals("4.8", parser.evaluate("4800 * 10^-3"));
    }
    @Test
    void testPemdas(){
        assertEquals("3", parser.evaluate("9/9*3"));
        assertEquals("27", parser.evaluate("9*9/3"));
        assertEquals("0", parser.evaluate("3%3/3"));
        assertEquals("1", parser.evaluate("3/3%3"));
        assertEquals("-900", parser.evaluate("4*3/-3*4/4%3*100*3^2"));
    }

    @Test
    void testTrigonometryFunctions(){
        assertEquals("1", parser.evaluate("tan(PI/4)"));
        assertEquals("1", parser.evaluate("sin(PI/2)"));
        assertEquals("0", parser.evaluate("sin(300*PI/2)"));
        assertEquals("1", parser.evaluate("cos(2000 * PI)"));
        assertEquals("-1", parser.evaluate("cos(101*PI)"));
        assertEquals("1", parser.evaluate("cot(PI/4)"));
        assertEquals("1", parser.evaluate("sec(4*PI)"));
        assertEquals("1", parser.evaluate("cosec(PI/2)"));

    }

    @Test
    void testTwoArgumentFunctions(){
        assertEquals("1000",parser.evaluate("pow(10,3)"));
        assertEquals("0.30103",parser.evaluate("log(10,2)"));
        assertEquals("2",parser.evaluate("log(10,10^2)"));
        assertEquals("5",parser.evaluate("log(2,2*2^4)"));
        assertEquals("3.141593",parser.evaluate("log(PI,PI^PI)"));
        assertEquals("100",parser.evaluate("log(10,pow(10,100))"));
    }


    @Test
    void testFunctionsAndOperators(){
        assertEquals("1000",parser.evaluate("2*pow(10,3)+100"));

    }
}