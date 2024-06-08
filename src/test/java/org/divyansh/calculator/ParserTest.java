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
        assertEquals("8", parser.evaluate("4^2/2"));
        assertEquals("2", parser.evaluate("4^(1/2)"));
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
        assertEquals("22", parser.evaluate("3!*(6/3*[3/3/{3/3}]) + 10"));
        assertEquals("-112", parser.evaluate("-(3!*(6/3*[3/3/{3/3}])) - 100"));
        assertEquals("48", parser.evaluate("-(-4!/-2*(6/-3*[3/3/{3/3}])) + 4!"));
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
        assertEquals("6", parser.evaluate("((3+1)*2)-2"));
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
        assertEquals("1000000000000000000000000000000000000000000000000000000000000",parser.evaluate("pow(10,60)"));
        assertEquals("0.30103",parser.evaluate("log(10,2)"));
        assertEquals("2",parser.evaluate("log(10,10^2)"));
        assertEquals("5",parser.evaluate("log(2,2*2^4)"));
        assertEquals("3.141593",parser.evaluate("log(PI,PI^PI)"));
    }


    @Test
    void testFunctionsAndOperators(){
        assertEquals("100",parser.evaluate("floor(100.2003)"));
        assertEquals("101",parser.evaluate("ceil(100.2003)"));
        assertEquals("101",parser.evaluate("floor(ceil(100.2003))"));
        assertEquals("-101",parser.evaluate("floor(-100.45)"));
        assertEquals("-100",parser.evaluate("ceil(-100.45)"));

        assertEquals("2100",parser.evaluate("2*pow(10,3)+100"));
        assertEquals("100",parser.evaluate("log(10,pow(10,100))"));
        assertEquals("100",parser.evaluate("log(10,pow(10,100))"));
        assertEquals("-1",parser.evaluate("sig(4*3/-3*4/4%3*100*3^2 - 10*log(10,100))"));
        assertEquals("900",parser.evaluate("abs(4*3/-3*4/4%3*100*3^2)"));
        assertEquals("920",parser.evaluate("abs(4*3/-3*4/4%3*100*3^2 - 10*log(10,100))"));
        assertEquals("1",parser.evaluate("sig(cos(0) * (log(100,100)+100*100+abs(-100) * sig(2!)))"));
        assertEquals("0",parser.evaluate("sig(tan(0) * (log(100,100)+100*100+abs(-100) * sig(2!)))"));
        assertEquals("10101",parser.evaluate("(log(100,100)+100*100+abs(-100) * sig(2!))"));
        assertEquals("105",parser.evaluate("2*log(10,abs(-100))+100 + floor(1.100)"));

    }
    @Test
    void testSingleArgumentFunctions(){
        assertEquals("100",parser.evaluate("floor100.20003"));
        assertEquals("1",parser.evaluate("ceil100.20003/101"));
        assertEquals("101",parser.evaluate("floor(ceil100.20003)"));
        assertEquals("-100",parser.evaluate("ceil-100.45"));
        assertEquals("20",parser.evaluate("sig100*20*sig139123.0"));
        assertThrows(RuntimeException.class,()->parser.evaluate("floorfloor10.3"));
    }
    @Test
    void testImplicitMultiplication(){
        assertEquals("1000",parser.evaluate("10(10(10))"));
        assertEquals("200",parser.evaluate("100log(10,10(10))"));
        assertEquals("0.841471",parser.evaluate("sin(cos0)"));
        assertEquals("10",parser.evaluate("10log(10,10)"));
        assertEquals("40",parser.evaluate("10pow(2,2)"));
        assertEquals("100",parser.evaluate("10log(10,10^10)"));
        assertEquals("40",parser.evaluate("10pow(2,2)log(10,10)"));
    }
}