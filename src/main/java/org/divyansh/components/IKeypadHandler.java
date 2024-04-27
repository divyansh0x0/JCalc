package org.divyansh.components;

import org.divyansh.calculator.tokens.FunctionType;
import org.divyansh.calculator.tokens.OperatorType;

public interface IKeypadHandler {
    void handleNumericKeyPress(int value);
    void handleOperatorKeyPress(OperatorType value);

    void handleFunctionKeyPress(FunctionType value);

    void handleSpecialKeyPress(SpecialButton value);
}
