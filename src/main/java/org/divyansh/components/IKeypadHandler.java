package org.divyansh.components;

public interface IKeypadHandler {
    void handleNumericKeyPress(int value);
    void handleSpecialKeyPress(SpecialKeyValues value);

}
